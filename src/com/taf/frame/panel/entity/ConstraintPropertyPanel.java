package com.taf.frame.panel.entity;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;

import com.taf.logic.constraint.Constraint;
import com.taf.manager.ConstantManager;

public class ConstraintPropertyPanel extends EntityPropertyPanel {

	private static final long serialVersionUID = 1744588782255512565L;

	public ConstraintPropertyPanel(Constraint constraint) {
		super(constraint);

		GridBagConstraints c = ConstantManager.getDefaultConstraint();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, ConstantManager.SMALL_INSET_GAP, ConstantManager.SMALL_INSET_GAP);
		c.weightx = 0;
		c.weighty = 0;
		JLabel constraintLabel = new JLabel(ConstantManager.CONSTRAINT_NAME_LABEL_TEXT);
		this.add(constraintLabel, c);

		c.insets = new Insets(0, ConstantManager.SMALL_INSET_GAP, ConstantManager.SMALL_INSET_GAP, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		this.add(entityName, c);
	}

}
