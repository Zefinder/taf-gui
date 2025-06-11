package com.taf.frame.dialog;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.taf.logic.constraint.Constraint;
import com.taf.manager.ConstantManager;

public class ConstraintCreationDialog extends InputInformationDialog {

	private static final long serialVersionUID = 1054772104893408576L;

	private static final String DIALOG_TITLE = "Create a new constraint";

	private final JTextField constraintName;

	private Constraint createdConstraint;

	public ConstraintCreationDialog() {
		this.setTitle(DIALOG_TITLE);

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(ConstantManager.HUGE_INSET_GAP, ConstantManager.LARGE_INSET_GAP,
				ConstantManager.SMALL_INSET_GAP, ConstantManager.SMALL_INSET_GAP);
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		JLabel constraintLabel = new JLabel(ConstantManager.CONSTRAINT_NAME_LABEL_TEXT);
		addComponent(constraintLabel, c);

		c.insets = new Insets(ConstantManager.HUGE_INSET_GAP, ConstantManager.SMALL_INSET_GAP,
				ConstantManager.SMALL_INSET_GAP, ConstantManager.LARGE_INSET_GAP);
		c.gridx = 1;
		constraintName = new JTextField(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		addComponent(constraintName, c);
	}

	@Override
	protected void performAction() {
		String name = constraintName.getText();
		if (!name.isBlank()) {
			createdConstraint = new Constraint(name.strip());
			dispose();
		}
	}

	// TODO Replace by optional
	public Constraint getConstraint() {
		return createdConstraint;
	}

	@Override
	public void initDialog() {
		createdConstraint = null;
		super.initDialog();
	}

}
