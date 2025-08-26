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
package com.taf.frame.panel.run;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;

import com.taf.logic.field.Root;
import com.taf.manager.SaveManager;
import com.taf.util.Consts;
import com.taf.util.EntityNode;
import com.taf.util.TafTree;

/**
 * The ActiveTreePanel is used in the {@link TafRunComponentPanel} to display
 * the parameters and nodes and allow the user to know which Python code he must
 * perform to get access to a certain entity.
 *
 * @see JPanel
 * @see TafRunComponentPanel
 *
 * @author Adrien Jakubiak
 */
public class ActiveTreePanel extends JPanel {

	private static final long serialVersionUID = 5350785957881515443L;

	/** The copy button text. */
	private static final String COPY_BUTTON_TEXT = "Copy command";
	
	/** The root node name. */
	private static final String ROOT_NODE_NAME = "root_node";

	/** The access node by name function format. */
	private static final String ACCESS_NODE_BY_NAME_FUNCTION_FORMAT = ".get_child_n(\"%s\", <optional_index>)";
	
	/** The access parameter by name function format. */
	private static final String ACCESS_PARAMETER_BY_NAME_FUNCTION_FORMAT = ".get_parameter_n(\"%s\", <optional_index>).values[0]";
	
	/** The number of instances name. */
	private static final String NB_INSTANCES_NAME = "nb_instances";
	
	/** The value variable name. */
	private static final String VALUE_VARIABLE_NAME = "value";
	
	/**
	 * Prints the node path command.
	 *
	 * @param treeNode the tree node
	 * @return the the path
	 */
	private static String printNodePathCommand(DefaultMutableTreeNode treeNode) {
		String path = "";
		if (treeNode.isRoot()) {
			path = ROOT_NODE_NAME;
		} else {
			path = printNodePathCommand((DefaultMutableTreeNode) treeNode.getParent());
		
			EntityNode node = (EntityNode) treeNode.getUserObject();
			String entityName = node.getEntity().getName();
			if (node.isType()) {
				path += ACCESS_NODE_BY_NAME_FUNCTION_FORMAT.formatted(entityName);
			} else {
				path += ACCESS_PARAMETER_BY_NAME_FUNCTION_FORMAT.formatted(entityName);
			}
		}

		return path;

	}

	/** The TAF tree to display nodes and parameters. */
	private TafTree tree;

	/**
	 * Instantiates a new active tree panel.
	 */
	public ActiveTreePanel() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = Consts.getDefaultConstraint();

		// Definition here to use in tree
		JTextField commandField = new JTextField(Consts.JTEXT_FIELD_DEFAULT_COLUMN);

		// Add the tree
		c.gridwidth = GridBagConstraints.REMAINDER;
		Root root = SaveManager.getInstance().getProjectRoot();
		tree = new TafTree(root, true);
		tree.instantiateNodes();
		tree.addTreeSelectionListener(e -> {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			EntityNode node = (EntityNode) selectedNode.getUserObject();
			String nodePath = printNodePathCommand(selectedNode);
			if (!node.isRoot() && node.isType()) {
				nodePath = "%s = %s.%s".formatted(NB_INSTANCES_NAME, nodePath, NB_INSTANCES_NAME);
			} else {
				nodePath = VALUE_VARIABLE_NAME + " = " + nodePath;
			}

			commandField.setText(nodePath);
		});
		JScrollPane treeView = new JScrollPane(tree);
		this.add(treeView, c);

		// Add the text field with the Python command
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP, 0, Consts.SMALL_INSET_GAP);
		c.weighty = 0;
		c.gridy = 1;
		commandField.setEditable(false);
		this.add(commandField, c);

		// Add the copy button
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(Consts.SMALL_INSET_GAP, 0, Consts.SMALL_INSET_GAP, 0);
		c.gridy = 2;
		JButton copyButton = new JButton(COPY_BUTTON_TEXT);
		copyButton.addActionListener(e -> {
			Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
			cb.setContents(new StringSelection(commandField.getText()), null);
		});
		this.add(copyButton, c);
	}

}
