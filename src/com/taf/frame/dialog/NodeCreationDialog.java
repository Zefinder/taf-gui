package com.taf.frame.dialog;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.taf.logic.field.Field;
import com.taf.logic.field.Node;
import com.taf.logic.type.AnonymousType;
import com.taf.manager.ConstantManager;

public class NodeCreationDialog extends InputInformationDialog {

	private static final long serialVersionUID = 8094717940445682259L;
	
	private static final String DIALOG_TITLE = "Create a new node";

	private final JTextField fieldName;

	private Field createdField;

	public NodeCreationDialog() {
		this.setTitle(DIALOG_TITLE);

		// TODO Replace with ConstantManager
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(20, 15, 5, 5);
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		JLabel fieldLabel = new JLabel(ConstantManager.NODE_NAME_LABEL_TEXT);
		addComponent(fieldLabel, c);

		c.insets = new Insets(20, 5, 5, 15);
		c.gridx = 1;
		fieldName = new JTextField(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		addComponent(fieldName, c);
	}

	@Override
	protected void performAction() {
		String name = fieldName.getText();
		if (!name.isBlank()) {
			com.taf.logic.type.Type type = new AnonymousType();
			createdField = new Node(name, type);
			dispose();
		}
	}

	public Field getField() {
		return createdField;
	}

	@Override
	public void initDialog() {
		createdField = null;
		super.initDialog();
	}

}
