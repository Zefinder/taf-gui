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
import com.taf.manager.ConstantManager;
import com.taf.manager.EventManager;

public class FieldTreePanel extends JPanel implements EventListener {

	private static final long serialVersionUID = -8299875121910645683L;

	private JTree tree;
	private DefaultTreeModel treeModel;

	private DefaultMutableTreeNode rootNode;
	private DefaultMutableTreeNode cachedNode;

	private Map<Entity, DefaultMutableTreeNode> entityToTreeNodeMap;

	public FieldTreePanel(Root root) {
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEmptyBorder(ConstantManager.SMALL_INSET_GAP, ConstantManager.SMALL_INSET_GAP,
				ConstantManager.SMALL_INSET_GAP, ConstantManager.SMALL_INSET_GAP));
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
			};
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
		};
		tree.addMouseListener(rightClickListener);
		tree.addMouseMotionListener(rightClickListener);

		JScrollPane treeView = new JScrollPane(tree);
		this.add(treeView, c);
	}

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

	private int getNodeRow(DefaultMutableTreeNode node) {
		TreePath path = new TreePath(node.getPath());
		return tree.getRowForPath(path);
	}

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

	@Override
	public void unregisterComponents() {
		// No inner listeners
	}

	@EventMethod
	public void onTypeCreated(TypeCreatedEvent event) {
		addEntity(event.getType());
	}

	@EventMethod
	public void onParameterCreated(ParameterCreatedEvent event) {
		addEntity(event.getParameter());
	}

	@EventMethod
	public void onNodeCreated(NodeCreatedEvent event) {
		addEntity(event.getNode());
	}

	@EventMethod
	public void onConstraintCreated(ConstraintCreatedEvent event) {
		addEntity(event.getConstraint());
	}

	@EventMethod
	public void onEntityNameChanged(EntityNameChangedEvent event) {
		DefaultMutableTreeNode treeNode = entityToTreeNodeMap.get(event.getEntity());
		NodeObject nodeObject = (NodeObject) treeNode.getUserObject();
		nodeObject.refresh();
		treeModel.nodeChanged(cachedNode);
	}

	@EventMethod
	public void onFieldTypeChanged(FieldTypeChangedEvent event) {
		DefaultMutableTreeNode treeNode = entityToTreeNodeMap.get(event.getField());
		NodeObject nodeObject = (NodeObject) treeNode.getUserObject();
		nodeObject.refresh();
		treeModel.nodeChanged(treeNode);
	}

	@EventMethod
	public void onNodeTypeChanged(NodeTypeChangedEvent event) {
		DefaultMutableTreeNode treeNode = entityToTreeNodeMap.get(event.getNode());
		NodeObject nodeObject = (NodeObject) treeNode.getUserObject();
		nodeObject.refresh();
		treeModel.nodeChanged(treeNode);
	}

	@EventMethod
	public void onEntityDeleted(EntityDeletedEvent event) {
		removeEntity(event.getEntity());
	}

	private static class NodeObject {

		private static final String DISPLAY_FORMAT = "%s: %s";

		private Entity entity;
		private String entityName;
		private String typeName;
		private boolean isRoot;
		private boolean isType;

		public NodeObject(Entity entity) {
			this.entity = entity;
			refresh();
		}

		public void refresh() {
			entityName = entity.getName();
			typeName = entity.getEntityTypeName();
			isRoot = entity instanceof Root;
			isType = entity instanceof Type;
		}

		public Entity getEntity() {
			return entity;
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
