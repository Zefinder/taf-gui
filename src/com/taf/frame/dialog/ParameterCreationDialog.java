package com.taf.frame.dialog;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.taf.logic.field.Parameter;
import com.taf.manager.TypeManager;
import com.taf.util.Consts;

public class ParameterCreationDialog extends InputInformationDialog {

	private static final long serialVersionUID = 33492308978388388L;

	private static final String DIALOG_TITLE = "Create a new parameter";

	private final JTextField fieldName;
	private final JComboBox<String> typeNames;

	private Parameter createdParameter;

	public ParameterCreationDialog() {
		this.setTitle(DIALOG_TITLE);

		// TODO Replace with ConstantManager
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(Consts.HUGE_INSET_GAP, Consts.LARGE_INSET_GAP,
				Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP);
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		JLabel fieldLabel = new JLabel(Consts.PARAMETER_NAME_LABEL_TEXT);
		addComponent(fieldLabel, c);

		c.insets = new Insets(Consts.HUGE_INSET_GAP, Consts.SMALL_INSET_GAP,
				Consts.SMALL_INSET_GAP, Consts.LARGE_INSET_GAP);
		c.gridx = 1;
		fieldName = new JTextField(Consts.JTEXT_FIELD_DEFAULT_COLUMN);
		addComponent(fieldName, c);

		c.insets = new Insets(5, Consts.LARGE_INSET_GAP, Consts.SMALL_INSET_GAP,
				Consts.SMALL_INSET_GAP);
		c.gridx = 0;
		c.gridy = 1;
		JLabel typeNameLabel = new JLabel(Consts.PARAMETER_TYPE_LABEL_TEXT);
		addComponent(typeNameLabel, c);

		c.insets = new Insets(Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP,
				Consts.SMALL_INSET_GAP, Consts.LARGE_INSET_GAP);
		c.gridx = 1;
		typeNames = new JComboBox<String>(TypeManager.getInstance().getParameterTypeNames().toArray(String[]::new));
		addComponent(typeNames, c);
	}

	@Override
	protected void performAction() {
		String name = fieldName.getText();
		if (!name.isBlank()) {
			String typeName = (String) typeNames.getSelectedItem();
			com.taf.logic.type.FieldType type = TypeManager.getInstance().instanciateTypeFromClassName(typeName);
			createdParameter = new Parameter(name, type);
			dispose();
		}
	}

	public Parameter getField() {
		return createdParameter;
	}

	@Override
	public void initDialog() {
		createdParameter = null;
		super.initDialog();
	}

}
