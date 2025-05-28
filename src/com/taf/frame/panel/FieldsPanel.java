package com.taf.frame.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.taf.event.Event;
import com.taf.event.EventListener;
import com.taf.event.EventMethod;
import com.taf.event.FieldNameChangedEvent;
import com.taf.event.FieldSelectedEvent;
import com.taf.event.FieldTypeChangedEvent;
import com.taf.frame.dialog.NodeCreationDialog;
import com.taf.frame.dialog.ParameterCreationDialog;
import com.taf.logic.field.Field;
import com.taf.logic.field.Node;
import com.taf.logic.field.Parameter;
import com.taf.logic.field.Root;
import com.taf.logic.type.AnonymousType;
import com.taf.logic.type.Type;
import com.taf.manager.EventManager;

public class FieldsPanel extends JPanel implements EventListener {

	private static final long serialVersionUID = -8299875121910645683L;
	private static final String DEFAULT_ROOT_NAME = "test_cases";

	private JTree tree;
	private DefaultTreeModel treeModel;
	private JButton addParameterButton;
	private JButton addNodeButton;

	private DefaultMutableTreeNode rootNode;

	public FieldsPanel() {
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
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
		addParameterButton = new JButton("+ Add parameter");
		addParameterButton.addActionListener(e -> addParameter());
		this.add(addParameterButton, c);

		c.insets = new Insets(0, 5, 0, 0);
		c.gridx = 1;
		addNodeButton = new JButton("+ Add node");
		addNodeButton.addActionListener(e -> addNode());
		this.add(addNodeButton, c);

		c.insets = new Insets(5, 0, 0, 0);
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 1;
		Field field = new Root(DEFAULT_ROOT_NAME);
		rootNode = new DefaultMutableTreeNode(new NodeObject(field));
		tree = new JTree(rootNode);
		treeModel = (DefaultTreeModel) tree.getModel();
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(e -> {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			if (node == null) {
				return;
			}

			NodeObject nodeInfo = (NodeObject) node.getUserObject();
			Event event = new FieldSelectedEvent(nodeInfo.getField());
			EventManager.getInstance().fireEvent(event);
			
			if (nodeInfo.isRoot) {				
				System.out.println(nodeInfo.getField());
			}
		});
		JScrollPane treeView = new JScrollPane(tree);
		this.add(treeView, c);
	}

	private DefaultMutableTreeNode getNearestNodeField() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		if (node == null) {
			// In that case, the node will be the root node
			node = rootNode;
		}

		NodeObject nodeInfo = (NodeObject) node.getUserObject();
		Field field = nodeInfo.getField();
		/*
		 * Check if the node is not a parameter. If it is a parameter, then take the
		 * parent.
		 */
		if (field instanceof Parameter) {
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
		Node parent = (Node) nodeInfo.getField();
		
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
		Node parent = (Node) nodeInfo.getField();
		
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
		DefaultMutableTreeNode parameterNode = new DefaultMutableTreeNode(new NodeObject(field), true);
		treeModel.insertNodeInto(parameterNode, node, node.getChildCount());
		
		// If the node is the root and had no children, expand
		tree.expandRow(getNodeRow(node));
	}
	
	@EventMethod
	public void onFieldNameChanged(FieldNameChangedEvent event) {
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
	
	private static class NodeObject {

		private Field field;
		private String fieldName;
		private String typeName;
		private boolean isRoot;

		public NodeObject(Field field) {
			this.field = field;
			refresh();
		}

		public void refresh() {
			this.fieldName = field.getName();
			Type type = field.getType();
			
			if (field instanceof Root) {
				isRoot = true;
			} else {
				isRoot = false;
			}
			
			if (type instanceof AnonymousType) {
				typeName = "node";
			} else {
				typeName = type.getName();
			}
		}
		
		public Field getField() {
			return field;
		}

		@Override
		public String toString() {
			if (isRoot) {
				return fieldName;
			}
			
			return "%s: %s".formatted(fieldName, typeName);
		}

	}

}
