package com.taf.frame.panel.secondary.type;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.taf.event.ConstraintCreatedEvent;
import com.taf.event.Event;
import com.taf.frame.dialog.ConstraintCreationDialog;
import com.taf.logic.constraint.Constraint;
import com.taf.manager.ConstantManager;
import com.taf.manager.EventManager;

public class TypeAddEntityPanel extends JPanel {
	
	private static final long serialVersionUID = -903095568123189662L;
	private static final String ADD_PARAMETER_BUTTON_TEXT = "+ Add parameter";
	private static final String ADD_NODE_BUTTON_TEXT = "+ Add node";
	private static final String ADD_CONSTRAINT_BUTTON_TEXT = "+ Add constraint";

	public TypeAddEntityPanel() {
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = ConstantManager.getDefaultConstraint();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridy = 0;
		JButton addParameterButton = new JButton(ADD_PARAMETER_BUTTON_TEXT);
		addParameterButton.addActionListener(e -> {
			// TODO
			ConstraintCreationDialog dialog = new ConstraintCreationDialog();
			dialog.initDialog();
			Constraint constraint = dialog.getConstraint();
			if (constraint != null) {
				Event event = new ConstraintCreatedEvent(constraint);
				EventManager.getInstance().fireEvent(event);
			}
		});
		this.add(addParameterButton, c);
		
		c.insets = new Insets(ConstantManager.MEDIUM_INSET_GAP, 0, ConstantManager.MEDIUM_INSET_GAP, 0);
		c.gridy = 1;
		JButton addNodeButton = new JButton(ADD_NODE_BUTTON_TEXT);
		addNodeButton.addActionListener(e -> {
			// TODO
			ConstraintCreationDialog dialog = new ConstraintCreationDialog();
			dialog.initDialog();
			Constraint constraint = dialog.getConstraint();
			if (constraint != null) {
				Event event = new ConstraintCreatedEvent(constraint);
				EventManager.getInstance().fireEvent(event);
			}
		});
		this.add(addNodeButton, c);
		
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy = 2;
		JButton addConstraintButton = new JButton(ADD_CONSTRAINT_BUTTON_TEXT);
		addConstraintButton.addActionListener(e -> {
			ConstraintCreationDialog dialog = new ConstraintCreationDialog();
			dialog.initDialog();
			Constraint constraint = dialog.getConstraint();
			if (constraint != null) {
				Event event = new ConstraintCreatedEvent(constraint);
				EventManager.getInstance().fireEvent(event);
			}
		});
		this.add(addConstraintButton, c);
	}

}
