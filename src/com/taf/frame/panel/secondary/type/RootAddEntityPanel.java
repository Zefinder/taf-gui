package com.taf.frame.panel.secondary.type;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JButton;

import com.taf.event.ConstraintCreatedEvent;
import com.taf.event.Event;
import com.taf.frame.dialog.ConstraintCreationDialog;
import com.taf.logic.constraint.Constraint;
import com.taf.manager.ConstantManager;
import com.taf.manager.EventManager;

public class RootAddEntityPanel extends TypeAddEntityPanel {

	private static final long serialVersionUID = -9178603688544702107L;

	private static final String ADD_TYPE_BUTTON_TEXT = "+ Add type";

	public RootAddEntityPanel() {
		super();

		GridBagConstraints c = ConstantManager.getDefaultConstraint();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(ConstantManager.MEDIUM_INSET_GAP, 0, 0, 0);
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridy = 3;
		JButton addTypeButton = new JButton(ADD_TYPE_BUTTON_TEXT);
		addTypeButton.addActionListener(e -> {
			// TODO
			ConstraintCreationDialog dialog = new ConstraintCreationDialog();
			dialog.initDialog();
			Constraint constraint = dialog.getConstraint();
			if (constraint != null) {
				Event event = new ConstraintCreatedEvent(constraint);
				EventManager.getInstance().fireEvent(event);
			}
		});
		this.add(addTypeButton, c);
	}

}
