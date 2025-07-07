package com.taf.frame.panel.secondary.type;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.taf.event.Event;
import com.taf.event.entity.creation.ConstraintCreatedEvent;
import com.taf.event.entity.creation.NodeCreatedEvent;
import com.taf.event.entity.creation.ParameterCreatedEvent;
import com.taf.frame.dialog.ConstraintCreationDialog;
import com.taf.frame.dialog.NodeCreationDialog;
import com.taf.frame.dialog.ParameterCreationDialog;
import com.taf.logic.constraint.Constraint;
import com.taf.logic.field.Node;
import com.taf.logic.field.Parameter;
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
			ParameterCreationDialog dialog = new ParameterCreationDialog();
			dialog.initDialog();
			Parameter parameter = dialog.getField();
			if (parameter != null) {
				Event event = new ParameterCreatedEvent(parameter);
				EventManager.getInstance().fireEvent(event);
			}
		});
		this.add(addParameterButton, c);
		
		c.insets = new Insets(ConstantManager.MEDIUM_INSET_GAP, 0, ConstantManager.MEDIUM_INSET_GAP, 0);
		c.gridy = 1;
		JButton addNodeButton = new JButton(ADD_NODE_BUTTON_TEXT);
		addNodeButton.addActionListener(e -> {
			NodeCreationDialog dialog = new NodeCreationDialog();
			dialog.initDialog();
			Node node = dialog.getField();
			if (node != null) {
				Event event = new NodeCreatedEvent(node);
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
