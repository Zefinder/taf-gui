package com.taf.frame.dialog;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.taf.logic.field.Field;
import com.taf.logic.field.Parameter;
import com.taf.manager.ConstantManager;
import com.taf.manager.TypeManager;

public class ParameterCreationDialog extends InputInformationDialog {

	private static final long serialVersionUID = 33492308978388388L;

	private static final String DIALOG_TITLE = "Create a new parameter";

	private final JTextField fieldName;
	private final JComboBox<String> typeNames;

	private Field createdField;

	public ParameterCreationDialog() {
		this.setTitle(DIALOG_TITLE);

		// TODO Replace with ConstantManager
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(ConstantManager.HUGE_INSET_GAP, ConstantManager.LARGE_INSET_GAP,
				ConstantManager.SMALL_INSET_GAP, ConstantManager.SMALL_INSET_GAP);
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		JLabel fieldLabel = new JLabel(ConstantManager.PARAMETER_NAME_LABEL_TEXT);
		addComponent(fieldLabel, c);

		c.insets = new Insets(ConstantManager.HUGE_INSET_GAP, ConstantManager.SMALL_INSET_GAP,
				ConstantManager.SMALL_INSET_GAP, ConstantManager.LARGE_INSET_GAP);
		c.gridx = 1;
		fieldName = new JTextField(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		addComponent(fieldName, c);

		c.insets = new Insets(5, ConstantManager.LARGE_INSET_GAP, ConstantManager.SMALL_INSET_GAP,
				ConstantManager.SMALL_INSET_GAP);
		c.gridx = 0;
		c.gridy = 1;
		JLabel typeNameLabel = new JLabel(ConstantManager.PARAMETER_TYPE_LABEL_TEXT);
		addComponent(typeNameLabel, c);

		c.insets = new Insets(ConstantManager.SMALL_INSET_GAP, ConstantManager.SMALL_INSET_GAP,
				ConstantManager.SMALL_INSET_GAP, ConstantManager.LARGE_INSET_GAP);
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
