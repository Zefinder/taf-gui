package com.taf.frame.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.taf.event.ConstraintCreatedEvent;
import com.taf.event.EntityDeletedEvent;
import com.taf.event.EntityNameChangedEvent;
import com.taf.event.EntitySelectedEvent;
import com.taf.event.Event;
import com.taf.event.EventListener;
import com.taf.event.EventMethod;
import com.taf.event.FieldTypeChangedEvent;
import com.taf.frame.dialog.NodeCreationDialog;
import com.taf.frame.dialog.ParameterCreationDialog;
import com.taf.frame.popup.TreeEntityPopupMenu;
import com.taf.logic.Entity;
import com.taf.logic.constraint.Constraint;
import com.taf.logic.field.Field;
import com.taf.logic.field.Node;
import com.taf.logic.field.Root;
import com.taf.manager.ConstantManager;
import com.taf.manager.EventManager;

public class FieldTreePanel extends JPanel implements EventListener {

	private static final long serialVersionUID = -8299875121910645683L;
	private static final String ADD_PARAMETER_BUTTON_TEXT = "+ Add parameter";
	private static final String ADD_NODE_BUTTON_TEXT = "+ Add node";

	private JTree tree;
	private DefaultTreeModel treeModel;
	private JButton addParameterButton;
	private JButton addNodeButton;

	private DefaultMutableTreeNode rootNode;

	public FieldTreePanel(Root root) {
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEmptyBorder(ConstantManager.SMALL_INSET_GAP, ConstantManager.SMALL_INSET_GAP,
				ConstantManager.SMALL_INSET_GAP, ConstantManager.SMALL_INSET_GAP));
		EventManager.getInstance().registerEventListener(this);

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 0, 0, 5);
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		addParameterButton = new JButton(ADD_PARAMETER_BUTTON_TEXT);
		addParameterButton.addActionListener(e -> addParameter());
		this.add(addParameterButton, c);

		c.insets = new Insets(0, ConstantManager.SMALL_INSET_GAP, 0, 0);
		c.gridx = 1;
		addNodeButton = new JButton(ADD_NODE_BUTTON_TEXT);
		addNodeButton.addActionListener(e -> addNode());
		this.add(addNodeButton, c);

		c.insets = new Insets(ConstantManager.SMALL_INSET_GAP, 0, 0, 0);
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 1;
		rootNode = new DefaultMutableTreeNode(new NodeObject(root));
		initTreeNodes(rootNode, root);
		tree = new JTree(rootNode);
		treeModel = (DefaultTreeModel) tree.getModel();
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(e -> {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			if (node == null) {
				return;
			}

			NodeObject nodeInfo = (NodeObject) node.getUserObject();
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
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

					NodeObject nodeObject = (NodeObject) node.getUserObject();
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

	private void initTreeNodes(DefaultMutableTreeNode parentNode, Node node) {
		for (Field field : node.getFieldList()) {
			DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(new NodeObject(field));
			parentNode.add(treeNode);

			if (field instanceof Node) {
				initTreeNodes(treeNode, (Node) field);
			}
		}

		for (Constraint constraint : node.getConstraintList()) {
			DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(new NodeObject(constraint));
			parentNode.add(treeNode);
		}
	}

	private DefaultMutableTreeNode getNearestNodeField() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		if (node == null) {
			// In that case, the node will be the root node
			node = rootNode;
		}

		NodeObject nodeInfo = (NodeObject) node.getUserObject();
		Entity entity = nodeInfo.getEntity();
		/*
		 * Check if the node is not a parameter. If it is a parameter, then take the
		 * parent.
		 */
		if (!(entity instanceof Node)) {
			node = (DefaultMutableTreeNode) node.getParent();
		}

		return node;
	}

	private int getNodeRow(DefaultMutableTreeNode node) {
		TreePath path = new TreePath(node.getPath());
		return tree.getRowForPath(path);
	}

	private void addParameter() {
		DefaultMutableTreeNode node = getNearestNodeField();
		NodeObject nodeInfo = (NodeObject) node.getUserObject();
		Node parent = (Node) nodeInfo.getEntity(); // Sure that it is a node

		// Call the dialog to create a parameter
		ParameterCreationDialog dialog = new ParameterCreationDialog();
		dialog.initDialog();
		Field field = dialog.getField();
		if (field == null) {
			return;
		}

		// Add to the field
		parent.addField(field);

		// Add to the tree node
		DefaultMutableTreeNode parameterNode = new DefaultMutableTreeNode(new NodeObject(field), false);
		treeModel.insertNodeInto(parameterNode, node, node.getChildCount());

		// If the node is the root and had no children, expand
		tree.expandRow(getNodeRow(node));
	}

	private void addNode() {
		DefaultMutableTreeNode node = getNearestNodeField();
		NodeObject nodeInfo = (NodeObject) node.getUserObject();
		Node parent = (Node) nodeInfo.getEntity(); // Sure that it is a node

		// Call the dialog to create a parameter
		NodeCreationDialog dialog = new NodeCreationDialog();
		dialog.initDialog();
		Field field = dialog.getField();
		if (field == null) {
			return;
		}

		// Add to the field
		parent.addField(field);

		// Add to the tree node
		DefaultMutableTreeNode nodeNode = new DefaultMutableTreeNode(new NodeObject(field), true);
		treeModel.insertNodeInto(nodeNode, node, node.getChildCount());

		// If the node is the root and had no children, expand
		tree.expandRow(getNodeRow(node));
	}

	public void addConstraint(DefaultMutableTreeNode node, Constraint constraint) {
		DefaultMutableTreeNode constraintNode = new DefaultMutableTreeNode(new NodeObject(constraint), false);
		treeModel.insertNodeInto(constraintNode, node, node.getChildCount());
		tree.expandRow(getNodeRow(node));
	}

	@Override
	public void unregisterComponents() {
		// No inner listeners
	}

	@EventMethod
	public void onConstraintCreated(ConstraintCreatedEvent event) {
		Constraint constraint = event.getConstraint();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

		// TODO Find somewhere else to put the constraint in the node?
		NodeObject nodeObject = (NodeObject) node.getUserObject();
		Node a = (Node) nodeObject.getEntity();
		a.addConstraint(constraint);

		addConstraint(node, constraint);
		treeModel.nodeChanged(node);
	}

	@EventMethod
	public void onEntityNameChanged(EntityNameChangedEvent event) {
		// Editable node is the selected one
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		NodeObject nodeObject = (NodeObject) node.getUserObject();
		nodeObject.refresh();
		treeModel.nodeChanged(node);
	}

	@EventMethod
	public void onTypeNameChanged(FieldTypeChangedEvent event) {
		// Editable node is the selected one
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		NodeObject nodeObject = (NodeObject) node.getUserObject();
		nodeObject.refresh();
		treeModel.nodeChanged(node);
	}

	@EventMethod
	public void onEntityDeleted(EntityDeletedEvent event) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		treeModel.removeNodeFromParent(node);
	}

	private static class NodeObject {

		private static final String DISPLAY_FORMAT = "%s: %s";

		private Entity entity;
		private String entityName;
		private String typeName;
		private boolean isRoot;

		public NodeObject(Entity entity) {
			this.entity = entity;
			refresh();
		}

		public void refresh() {
			entityName = entity.getName();
			typeName = entity.getEntityTypeName();
			isRoot = entity instanceof Root;
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
