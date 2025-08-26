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

import java.util.HashMap;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import com.taf.logic.Entity;
import com.taf.logic.constraint.Constraint;
import com.taf.logic.field.Field;
import com.taf.logic.field.Node;
import com.taf.logic.field.Root;
import com.taf.logic.field.Type;

/**
 * <p>
 * The EntityNode stores an entity and important attributes to display in the
 * tree.
 * </p>
 * 
 * <p>
 * The displayed name follow this rule: [name]: [type]. For entities that have
 * no type, the [type] is replaced by the entity name. For instance,
 * <code>a: node</code> represents a node called <code>a</code> with no type,
 * when <code>b: super_type</code> represents a node called <code>b</code> that
 * is of type <code>super_type</code>
 * </p>
 * 
 * @author Adrien Jakubiak
 */
public class EntityNode {

	/** The display format. */
	private static final String DISPLAY_FORMAT = "%s: %s";

	/**
	 * Initializes the tree nodes starting with the root node
	 *
	 * @param root       the root node
	 * @param fieldsOnly true if needs to register only fields
	 * @return the map
	 */
	public static final Map<Entity, DefaultMutableTreeNode> initTreeNodes(Root root, boolean fieldsOnly) {
		Map<Entity, DefaultMutableTreeNode> entityToTreeNodeMap = new HashMap<Entity, DefaultMutableTreeNode>();
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new EntityNode(root), true);
		entityToTreeNodeMap.put(root, rootNode);
		for (Type type : root.getTypeSet()) {
			DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(new EntityNode(type), true);
			rootNode.add(treeNode);
			entityToTreeNodeMap.put(type, treeNode);
			initTreeNodes(treeNode, type, fieldsOnly, entityToTreeNodeMap);
		}

		for (Field field : root.getFieldSet()) {
			DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(new EntityNode(field), field instanceof Node);
			rootNode.add(treeNode);
			entityToTreeNodeMap.put(field, treeNode);

			if (field instanceof Node) {
				initTreeNodes(treeNode, (Node) field, fieldsOnly, entityToTreeNodeMap);
			}
		}

		if (!fieldsOnly) {
			for (Constraint constraint : root.getConstraintSet()) {
				DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(new EntityNode(constraint), false);
				rootNode.add(treeNode);
				entityToTreeNodeMap.put(constraint, treeNode);
			}
		}
		return entityToTreeNodeMap;
	}

	/**
	 * Initializes the tree nodes.
	 *
	 * @param parentNode the parent node
	 * @param node       the node
	 */
	private static final void initTreeNodes(DefaultMutableTreeNode parentNode, Type node, boolean fieldsOnly,
			Map<Entity, DefaultMutableTreeNode> entityToTreeNodeMap) {
		for (Field field : node.getFieldSet()) {
			DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(new EntityNode(field), field instanceof Node);
			parentNode.add(treeNode);
			entityToTreeNodeMap.put(field, treeNode);

			if (field instanceof Node) {
				initTreeNodes(treeNode, (Node) field, fieldsOnly, entityToTreeNodeMap);
			}
		}

		if (!fieldsOnly) {
			for (Constraint constraint : node.getConstraintSet()) {
				DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(new EntityNode(constraint), false);
				parentNode.add(treeNode);
				entityToTreeNodeMap.put(constraint, treeNode);
			}
		}
	}

	/** The entity. */
	private Entity entity;

	/** The entity name. */
	private String entityName;

	/** The type name. */
	private String typeName;

	/** True if the entity is a root node. */
	private boolean isRoot;

	/** True if the entity is a type. */
	private boolean isType;

	/**
	 * Instantiates a new node object.
	 *
	 * @param entity the entity
	 */
	public EntityNode(Entity entity) {
		this.entity = entity;
		refresh();
	}

	/**
	 * Returns the stored entity.
	 *
	 * @return the entity
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * Checks if the entity is root.
	 *
	 * @return true if the entity is root
	 */
	public boolean isRoot() {
		return isRoot;
	}

	/**
	 * Checks if the entity is a type.
	 *
	 * @return true if the entity is a type
	 */
	public boolean isType() {
		return isType;
	}

	/**
	 * Refreshes the node with the entity attributes.
	 */
	public void refresh() {
		entityName = entity.getName();
		typeName = entity.getEntityTypeName();
		isRoot = entity instanceof Root;
		isType = entity instanceof Type;
	}

	@Override
	public String toString() {
		if (isRoot) {
			return entityName;
		}

		return DISPLAY_FORMAT.formatted(entityName, typeName);
	}

}