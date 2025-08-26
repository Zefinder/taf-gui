/*
 * Copyright or © or Copr.
 * 
 * This software is a computer program whose purpose is to generate random test
 * case from a template file describing the data model.
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software. You can use, modify
 * and/or redistribute the software under the terms of the CeCILL-B license as
 * circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * 
 * As a counterpart to the access to the source code and rights to copy, modify
 * and redistribute granted by the license, users are provided only with a
 * limited warranty and the software's author, the holder of the economic
 * rights, and the successive licensors have only limited liability.
 * 
 * In this respect, the user's attention is drawn to the risks associated with
 * loading, using, modifying and/or developing or reproducing the software by
 * the user in light of its specific status of free software, that may mean that
 * it is complicated to manipulate, and that also therefore means that it is
 * reserved for developers and experienced professionals having in-depth
 * computer knowledge. Users are therefore encouraged to load and test the
 * software's suitability as regards their requirements in conditions enabling
 * the security of their systems and/or data to be ensured and, more generally,
 * to use and operate it in the same conditions as regards security.
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package com.taf.util;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.taf.logic.Entity;
import com.taf.logic.field.Node;
import com.taf.logic.field.Root;
import com.taf.logic.field.Type;
import com.taf.manager.TypeManager;

/**
 * A TafTree is a {@link JTree} which contains {@link EntityNode}s.
 * 
 * @see JTree
 * @see EntityNode
 *
 * @author Adrien Jakubiak
 */
public class TafTree extends JTree {

	private static final long serialVersionUID = -5527696749564423484L;

	/**
	 * Creates a deep copy of this node.
	 *
	 * @param node the node
	 * @return the default mutable tree node
	 */
	private static DefaultMutableTreeNode cloneNode(DefaultMutableTreeNode node) {
		HashSet<EntityNode> registeredNodes = new HashSet<EntityNode>();
		registeredNodes.add((EntityNode) node.getUserObject());
		return cloneNode(node, registeredNodes);
	}

	private static DefaultMutableTreeNode cloneNode(DefaultMutableTreeNode node, Set<EntityNode> registeredNodes) {
		EntityNode entityNode = (EntityNode) node.getUserObject();
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(entityNode, entityNode.isType());
		for (int iChildren = node.getChildCount(), i = 0; i < iChildren; i++) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
			EntityNode childEntityNode = (EntityNode) child.getUserObject();
			// If the node has already been registered, then the user object should be
			// equal.
			if (registeredNodes.contains(childEntityNode)) {
				newNode.add((DefaultMutableTreeNode) child.clone());
			} else {
				registeredNodes.add(childEntityNode);
				newNode.add(cloneNode(child, registeredNodes));
			}
		}
		return newNode;
	}

	/** The entity to tree node map. */
	private final Map<Entity, DefaultMutableTreeNode> entityToTreeNodeMap;

	/** The default tree model. */
	private final DefaultTreeModel defaultModel;

	/**
	 * Instantiates a new TAF tree.
	 */
	public TafTree(Root root, boolean fieldsOnly) {
		entityToTreeNodeMap = EntityNode.initTreeNodes(root, fieldsOnly);
		defaultModel = (DefaultTreeModel) treeModel;

		// Set default root
		defaultModel.setRoot(entityToTreeNodeMap.get(root));

		// Set to single selection
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		// Use the TAF cell renderer
		setCellRenderer(new TafTreeCellRenderer());
	}

	/**
	 * Returns the node from the entity.
	 *
	 * @param entity the entity
	 * @return the node from the entity
	 */
	public DefaultMutableTreeNode getNode(Entity entity) {
		return entityToTreeNodeMap.get(entity);
	}

	/**
	 * <p>
	 * Instantiates all {@link Node}s that are in the tree. This means that a node
	 * that makes a reference to a type will get all elements of that type.
	 * </p>
	 * 
	 * <p>
	 * This does not take into account the depth for pure recursive nodes (nodes in
	 * type <code>a</code> having type <code>a</code>). Even if all nodes will be
	 * present, the behavior is undefined for nested pure recursive nodes.
	 * </p>
	 */
	public void instantiateNodes() {
		TypeManager typeInstance = TypeManager.getInstance();
		// Move all children tree nodes to the nodes
		for (String typeName : typeInstance.getCustomNodeTypeSet()) {
			Type type = typeInstance.getTypeFromName(typeName);
			DefaultMutableTreeNode typeNode = entityToTreeNodeMap.get(type);
			for (Node node : typeInstance.getNodesImplementingType(typeName)) {
				DefaultMutableTreeNode nodeNode = entityToTreeNodeMap.get(node);
				Enumeration<TreeNode> children = typeNode.children();
				while (children.hasMoreElements()) {
					DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();
					nodeNode.add(cloneNode(child));
				}
			}
			defaultModel.removeNodeFromParent(typeNode);
		}

		// For refs, do the same thing but without removing the node
		// Get all nodes with a reference
		Set<Node> nodesWithRef = typeInstance.getCustomNodeRefSet().stream()
				.map(nodeName -> typeInstance.getNodeFromName(nodeName)).filter(node -> node.hasRef()).collect(Collectors.toSet());
		for (Node node : nodesWithRef) {
			Node refNode = typeInstance.getNodeFromName(node.getTypeName());
			DefaultMutableTreeNode nodeNode = entityToTreeNodeMap.get(node);
			DefaultMutableTreeNode refNodeNode = entityToTreeNodeMap.get(refNode);
			Enumeration<TreeNode> children = refNodeNode.children();
			while (children.hasMoreElements()) {
				DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();
				nodeNode.add(cloneNode(child));
			}
		}
		
		defaultModel.reload();
	}

	/**
	 * Puts an entity in the tree to the parent node.
	 *
	 * @param parent the parent
	 * @param entity the entity
	 */
	public void putNode(DefaultMutableTreeNode parent, Entity entity) {
		boolean isType = entity instanceof Type;
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new EntityNode(entity), isType);
		entityToTreeNodeMap.put(entity, newNode);

		// Update tree
		defaultModel.insertNodeInto(newNode, parent, parent.getChildCount());
		expandRow(getNodeRow(parent));
		defaultModel.nodeChanged(parent);
	}

	public void removeNode(Entity entity) {
		entityToTreeNodeMap.remove(entity);
	}

	/**
	 * Returns the node row in the tree.
	 *
	 * @param node the node
	 * @return the node row
	 */
	private int getNodeRow(DefaultMutableTreeNode node) {
		TreePath path = new TreePath(node.getPath());
		return getRowForPath(path);
	}

}
