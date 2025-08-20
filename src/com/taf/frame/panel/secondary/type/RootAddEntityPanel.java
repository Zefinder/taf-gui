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
import java.awt.Insets;

import javax.swing.JButton;

import com.taf.event.Event;
import com.taf.event.entity.creation.TypeCreatedEvent;
import com.taf.frame.dialog.TypeCreationDialog;
import com.taf.frame.panel.secondary.RootPropertyPanel;
import com.taf.logic.constraint.Constraint;
import com.taf.logic.field.Node;
import com.taf.logic.field.Parameter;
import com.taf.logic.field.Type;
import com.taf.manager.EventManager;
import com.taf.util.Consts;

/**
 * The RootAddEntityPanel is used in the secondary property panel for the root
 * node. It shows a list of buttons to add {@link Parameter}s, {@link Node}s,
 * {@link Constraint}s and {@link Type}s.
 * 
 * @see TypeAddEntityPanel
 * @see RootPropertyPanel
 *
 * @author Adrien Jakubiak
 */
public class RootAddEntityPanel extends TypeAddEntityPanel {

	private static final long serialVersionUID = -9178603688544702107L;

	/** The add type button text. */
	private static final String ADD_TYPE_BUTTON_TEXT = "+ Add type";

	/**
	 * Instantiates a new root add entity panel.
	 */
	public RootAddEntityPanel() {
		super();

		GridBagConstraints c = Consts.getDefaultConstraint();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(Consts.MEDIUM_INSET_GAP, 0, 0, 0);
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridy = 3;
		JButton addTypeButton = new JButton(ADD_TYPE_BUTTON_TEXT);
		addTypeButton.addActionListener(e -> {
			TypeCreationDialog dialog = new TypeCreationDialog();
			dialog.initDialog();
			Type type = dialog.getField();
			if (type != null) {
				Event event = new TypeCreatedEvent(type);
				EventManager.getInstance().fireEvent(event);
			}
		});
		this.add(addTypeButton, c);
	}

}
