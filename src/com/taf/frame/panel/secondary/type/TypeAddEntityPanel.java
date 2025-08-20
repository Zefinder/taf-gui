/*
 * Copyright or © or Copr.
 * 
 * This software is a computer program whose purpose is to generate random test
 * case from a template file describing the data model.
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software. You can use, modify
 * and/or redistribute the software under the terms of the CeCILL-B license as
 * circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * 
 * As a counterpart to the access to the source code and rights to copy, modify
 * and redistribute granted by the license, users are provided only with a
 * limited warranty and the software's author, the holder of the economic
 * rights, and the successive licensors have only limited liability.
 * 
 * In this respect, the user's attention is drawn to the risks associated with
 * loading, using, modifying and/or developing or reproducing the software by
 * the user in light of its specific status of free software, that may mean that
 * it is complicated to manipulate, and that also therefore means that it is
 * reserved for developers and experienced professionals having in-depth
 * computer knowledge. Users are therefore encouraged to load and test the
 * software's suitability as regards their requirements in conditions enabling
 * the security of their systems and/or data to be ensured and, more generally,
 * to use and operate it in the same conditions as regards security.
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
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
import com.taf.frame.panel.secondary.NodePropertyPanel;
import com.taf.frame.panel.secondary.TypePropertyPanel;
import com.taf.logic.constraint.Constraint;
import com.taf.logic.field.Node;
import com.taf.logic.field.Parameter;
import com.taf.manager.EventManager;
import com.taf.util.Consts;

/**
 * The TypeAddEntityPanel is used in the secondary property panel for types. It
 * shows a list of buttons to add {@link Parameter}s, {@link Node}s and
 * {@link Constraint}s.
 * 
 * @see JPanel
 * @see TypePropertyPanel
 * @see NodePropertyPanel
 *
 * @author Adrien Jakubiak
 */
public class TypeAddEntityPanel extends JPanel {

	private static final long serialVersionUID = -903095568123189662L;

	/** The add parameter button text. */
	private static final String ADD_PARAMETER_BUTTON_TEXT = "+ Add parameter";

	/** The add node button text. */
	private static final String ADD_NODE_BUTTON_TEXT = "+ Add node";

	/** The add constraint button text. */
	private static final String ADD_CONSTRAINT_BUTTON_TEXT = "+ Add constraint";

	/**
	 * Instantiates a new type add entity panel.
	 */
	public TypeAddEntityPanel() {
		this.setLayout(new GridBagLayout());

		GridBagConstraints c = Consts.getDefaultConstraint();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridy = 0;
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

		c.insets = new Insets(Consts.MEDIUM_INSET_GAP, 0, Consts.MEDIUM_INSET_GAP, 0);
		c.gridy = 1;
		JButton addParameterButton = new JButton(ADD_PARAMETER_BUTTON_TEXT);
		addParameterButton.addActionListener(e -> {
			ParameterCreationDialog dialog = new ParameterCreationDialog();
			dialog.initDialog();
			Parameter parameter = dialog.getField();
			if (parameter != null) {
				Event event = new ParameterCreatedEvent(parameter);
				EventManager.getInstance().fireEvent(event);
			}
		});
		this.add(addParameterButton, c);

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
