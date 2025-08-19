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
package com.taf.frame.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.taf.event.Event;
import com.taf.event.EventListener;
import com.taf.event.EventMethod;
import com.taf.event.entity.EntityDeletedEvent;
import com.taf.event.entity.EntityNameChangedEvent;
import com.taf.event.entity.EntitySelectedEvent;
import com.taf.event.entity.FieldTypeChangedEvent;
import com.taf.event.entity.NodeTypeChangedEvent;
import com.taf.event.entity.creation.ConstraintCreatedEvent;
import com.taf.event.entity.creation.NodeCreatedEvent;
import com.taf.event.entity.creation.ParameterCreatedEvent;
import com.taf.event.entity.creation.TypeCreatedEvent;
import com.taf.frame.popup.TreeEntityPopupMenu;
import com.taf.logic.Entity;
import com.taf.logic.constraint.Constraint;
import com.taf.logic.field.Field;
import com.taf.logic.field.Node;
import com.taf.logic.field.Root;
import com.taf.logic.field.Type;
import com.taf.manager.EventManager;
import com.taf.manager.TypeManager;
import com.taf.util.Consts;

/**
 * <p>
 * The FieldTreePanel is used in the {@link TafPanel} and manages the entities
 * that were added in the project.
 * </p>
 * 
 * <p>
 * It is composed of a {@link JTree} which contains {@link NodeObject}s. The
 * displayed name follow this rule: [name]: [type]. For entities that have no
 * type, the [type] is replaced by the entity name. For instance,
 * <code>a: node</code> represents a node called <code>a</code> with no type,
 * when <code>b: super_type</code> represents a node called <code>b</code> that
 * is of type <code>super_type</code>.
 * </p>
 * 
 * <p>
 * The icon of the tree node depends on the entity: if the entity is an instance
 * of {@link Type}, then the icon will be a little folder. Else it will be a
 * white page.
 * </p>
 * 
 * <p>
 * When an entity is selected in the tree, the {@link PropertyPanel} will be
 * notified to display the entity informations. If the entity is modified in the
 * {@link PropertyPanel}, the changes will be reflected in the tree.
 * </p>
 * 
 * <p>
 * When an entity is deleted, its node is deleted, the {@link PropertyPanel} is
 * notified as well as the {@link TypeManager}.
 * </p>
 * 
 * @see JPanel
 * @see TafPanel
 * @see PropertyPanel
 *
 * @author Adrien Jakubiak
 */
public class FieldTreePanel extends JPanel implements EventListener {

	private static final long serialVersionUID = -8299875121910645683L;

	/** The tree with all project entities. */
	private JTree tree;

	/** The tree model. */
	private DefaultTreeModel treeModel;

	/** The root node. */
	private DefaultMutableTreeNode rootNode;

	/** The cached node. */
	private DefaultMutableTreeNode cachedNode;

	/** The entity to tree node map. */
	private Map<Entity, DefaultMutableTreeNode> entityToTreeNodeMap;

	/**
	 * Instantiates a new field tree panel.
	 *
	 * @param root the root
	 */
	public FieldTreePanel(Root root) {
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEmptyBorder(Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP,
				Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP));
		EventManager.getInstance().registerEventListener(this);
		entityToTreeNodeMap = new HashMap<Entity, DefaultMutableTreeNode>();

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		rootNode = new DefaultMutableTreeNode(new NodeObject(root));
		initTreeNodes(rootNode, root);
		tree = new JTree(rootNode);
		treeModel = (DefaultTreeModel) tree.getModel();
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setCellRenderer(new DefaultTreeCellRenderer() {
			private static final long serialVersionUID = 3600625563246633955L;

			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
					boolean leaf, int row, boolean hasFocus) {
				super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
				NodeObject nodeObject = (NodeObject) node.getUserObject();
				Icon icon;
				if (nodeObject.isType) {
					if (expanded) {
						icon = getDefaultOpenIcon();
					} else {
						icon = getDefaultClosedIcon();
					}
				} else {
					icon = getDefaultLeafIcon();
				}

				Color backgroundColor;
				if (hasFocus) {
					backgroundColor = getBackgroundSelectionColor();
				} else {
					backgroundColor = getBackgroundNonSelectionColor();
				}

				setIcon(icon);
				setBackground(backgroundColor);
				return this;
			}

		});
		tree.addTreeSelectionListener(e -> {
			cachedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			if (cachedNode == null) {
				return;
			}

			NodeObject nodeInfo = (NodeObject) cachedNode.getUserObject();
			if (nodeInfo.isRoot) {
				System.out.println(nodeInfo.entity.toString());
			}

			Event event = new EntitySelectedEvent(nodeInfo.getEntity());
			EventManager.getInstance().fireEvent(event);
		});
		MouseAdapter rightClickListener = new MouseAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();

				int selRow = tree.getRowForLocation(x, y);

				if (selRow != -1) {
					tree.setSelectionRow(selRow);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				selectRow(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				int selRow = selectRow(e);

				if (selRow != -1) {
					int x = e.getX();
					int y = e.getY();

					NodeObject nodeObject = (NodeObject) cachedNode.getUserObject();
					Entity entity = nodeObject.getEntity();

					// Show popup if not root
					if (!(entity instanceof Root)) {
						JPopupMenu menu = new TreeEntityPopupMenu(entity);
						menu.show(tree, x, y);
					}
				}
			}

			private int selectRow(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					int x = e.getX();
					int y = e.getY();

					int selRow = tree.getRowForLocation(x, y);

					if (selRow != -1) {
						tree.setSelectionRow(selRow);
						return selRow;
					}
				}

				return -1;
			}
		};
		tree.addMouseListener(rightClickListener);
		tree.addMouseMotionListener(rightClickListener);

		JScrollPane treeView = new JScrollPane(tree);
		this.add(treeView, c);
	}

	/**
	 * Handler for {@link ConstraintCreatedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onConstraintCreated(ConstraintCreatedEvent event) {
		addEntity(event.getConstraint());
	}

	/**
	 * Handler for {@link EntityDeletedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onEntityDeleted(EntityDeletedEvent event) {
		removeEntity(event.getEntity());
	}

	/**
	 * Handler for {@link EntityNameChangedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onEntityNameChanged(EntityNameChangedEvent event) {
		DefaultMutableTreeNode treeNode = entityToTreeNodeMap.get(event.getEntity());
		NodeObject nodeObject = (NodeObject) treeNode.getUserObject();
		nodeObject.refresh();
		treeModel.nodeChanged(cachedNode);
	}

	/**
	 * Handler for {@link FieldTypeChangedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onFieldTypeChanged(FieldTypeChangedEvent event) {
		DefaultMutableTreeNode treeNode = entityToTreeNodeMap.get(event.getField());
		NodeObject nodeObject = (NodeObject) treeNode.getUserObject();
		nodeObject.refresh();
		treeModel.nodeChanged(treeNode);
	}

	/**
	 * Handler for {@link NodeCreatedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onNodeCreated(NodeCreatedEvent event) {
		addEntity(event.getNode());
	}

	/**
	 * Handler for {@link NodeTypeChangedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onNodeTypeChanged(NodeTypeChangedEvent event) {
		DefaultMutableTreeNode treeNode = entityToTreeNodeMap.get(event.getNode());
		NodeObject nodeObject = (NodeObject) treeNode.getUserObject();
		nodeObject.refresh();
		treeModel.nodeChanged(treeNode);
	}

	/**
	 * Handler for {@link ParameterCreatedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onParameterCreated(ParameterCreatedEvent event) {
		addEntity(event.getParameter());
	}

	/**
	 * Handler for {@link TypeCreatedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onTypeCreated(TypeCreatedEvent event) {
		addEntity(event.getType());
	}

	@Override
	public void unregisterComponents() {
		// No inner listeners
	}

	/**
	 * Adds an entity to the tree.
	 *
	 * @param entity the entity
	 */
	private void addEntity(Entity entity) {
		// We assume that the cached node will be the parent
		if (cachedNode != null) {
			NodeObject parentObject = (NodeObject) cachedNode.getUserObject();
			Entity parent = parentObject.getEntity();
			if (parent instanceof Type) {
				addEntity((Type) parent, entity);
			}
		}
	}

	/**
	 * Adds an entity to the tree to its parent.
	 *
	 * @param parent the parent
	 * @param entity the entity
	 */
	private void addEntity(Type parent, Entity entity) {
		// Add entity to parent type
		parent.addEntity(entity);

		// Update tree
		boolean isType = entity instanceof Type;
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new NodeObject(entity), isType);
		treeModel.insertNodeInto(newNode, cachedNode, cachedNode.getChildCount());
		tree.expandRow(getNodeRow(cachedNode));
		treeModel.nodeChanged(cachedNode);

		// Add to map
		entityToTreeNodeMap.put(entity, newNode);
	}

	/**
	 * Returns the node row in the tree.
	 *
	 * @param node the node
	 * @return the node row
	 */
	private int getNodeRow(DefaultMutableTreeNode node) {
		TreePath path = new TreePath(node.getPath());
		return tree.getRowForPath(path);
	}

	/**
	 * Initializes the tree nodes.
	 *
	 * @param parentNode the parent node
	 * @param node       the node
	 */
	private void initTreeNodes(DefaultMutableTreeNode parentNode, Type node) {
		if (node instanceof Root) {
			Root root = (Root) node;
			for (Type type : root.getTypeSet()) {
				DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(new NodeObject(type), true);
				parentNode.add(treeNode);
				entityToTreeNodeMap.put(type, treeNode);
				initTreeNodes(treeNode, type);
			}
		}

		for (Field field : node.getFieldSet()) {
			DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(new NodeObject(field), field instanceof Node);
			parentNode.add(treeNode);
			entityToTreeNodeMap.put(field, treeNode);

			if (field instanceof Node) {
				initTreeNodes(treeNode, (Node) field);
			}
		}

		for (Constraint constraint : node.getConstraintSet()) {
			DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(new NodeObject(constraint), false);
			parentNode.add(treeNode);
			entityToTreeNodeMap.put(constraint, treeNode);
		}
	}

	/**
	 * Removes an entity of the tree.
	 *
	 * @param entity the entity
	 */
	private void removeEntity(Entity entity) {
		DefaultMutableTreeNode treeNodeToRemove = entityToTreeNodeMap.get(entity);
		treeModel.removeNodeFromParent(treeNodeToRemove);
		treeModel.nodeChanged(treeNodeToRemove.getParent());

		// If the cached node is the node to remove, then reset the cached node
		if (treeNodeToRemove.equals(cachedNode)) {
			cachedNode = null;
		}

		// Remove from map
		entityToTreeNodeMap.remove(entity);
	}

	/**
	 * The NodeObject stores an entity and important attributes to display in the
	 * tree.
	 *
	 * @author Adrien Jakubiak
	 */
	private static class NodeObject {

		/** The display format. */
		private static final String DISPLAY_FORMAT = "%s: %s";

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
		public NodeObject(Entity entity) {
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

}
