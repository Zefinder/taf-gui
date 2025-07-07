package com.taf.frame.dialog;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.taf.manager.ConstantManager;

public class TypeCreationDialog extends InputInformationDialog {

	private static final long serialVersionUID = 8094717940445682259L;

	private static final String DIALOG_TITLE = "Create a new type";

	private final JTextField fieldName;

	private com.taf.logic.field.Type createdType;

	public TypeCreationDialog() {
		this.setTitle(DIALOG_TITLE);

		// TODO Put in InputInformationDialog as they all use it?
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(ConstantManager.HUGE_INSET_GAP, ConstantManager.LARGE_INSET_GAP,
				ConstantManager.SMALL_INSET_GAP, ConstantManager.SMALL_INSET_GAP);
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		JLabel fieldLabel = new JLabel(ConstantManager.NODE_NAME_LABEL_TEXT);
		addComponent(fieldLabel, c);

		c.insets = new Insets(ConstantManager.HUGE_INSET_GAP, ConstantManager.SMALL_INSET_GAP,
				ConstantManager.SMALL_INSET_GAP, ConstantManager.LARGE_INSET_GAP);
		c.gridx = 1;
		fieldName = new JTextField(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		addComponent(fieldName, c);
	}

	@Override
	protected void performAction() {
		String name = fieldName.getText();
		if (!name.isBlank()) {
			createdType = new com.taf.logic.field.Type(name);
			dispose();
		}
	}

	public com.taf.logic.field.Type getField() {
		return createdType;
	}

	@Override
	public void initDialog() {
		createdType = null;
		super.initDialog();
	}

}
