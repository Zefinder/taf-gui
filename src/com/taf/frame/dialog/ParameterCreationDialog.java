package com.taf.frame.dialog;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.taf.logic.field.Field;
import com.taf.logic.field.Parameter;
import com.taf.manager.TypeManager;

public class ParameterCreationDialog extends InputInformationDialog {

	private static final long serialVersionUID = 33492308978388388L;

	private final JTextField fieldName;
	private final JComboBox<String> typeNames;

	private Field createdField;

	public ParameterCreationDialog() {
		this.setTitle("Create a new parameter");

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(20, 15, 5, 5);
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		JLabel fieldLabel = new JLabel("Parameter name");
		addComponent(fieldLabel, c);

		c.insets = new Insets(20, 5, 5, 15);
		c.gridx = 1;
		fieldName = new JTextField(20);
		addComponent(fieldName, c);

		c.insets = new Insets(5, 15, 5, 5);
		c.gridx = 0;
		c.gridy = 1;
		JLabel typeNameLabel = new JLabel("Parameter type");
		addComponent(typeNameLabel, c);

		c.insets = new Insets(5, 5, 5, 15);
		c.gridx = 1;
		typeNames = new JComboBox<String>(TypeManager.getInstance().getTypeNames().toArray(String[]::new));
		addComponent(typeNames, c);
	}

	protected void performAction() {
		String name = fieldName.getText();
		if (!name.isBlank()) {			
			String typeName = (String) typeNames.getSelectedItem();
			com.taf.logic.type.Type type = TypeManager.getInstance().instanciateTypeFromClassName(typeName);
			createdField = new Parameter(name, type);
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
