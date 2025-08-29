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
package com.taf.frame.panel.primary;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import com.taf.annotation.FactoryObject;
import com.taf.event.Event;
import com.taf.event.entity.ParameterTypeChangedEvent;
import com.taf.exception.EntityCreationException;
import com.taf.logic.Entity;
import com.taf.logic.field.Parameter;
import com.taf.logic.type.FieldType;
import com.taf.logic.type.ParameterTypeFactory;
import com.taf.manager.EventManager;
import com.taf.util.Consts;

/**
 * The ParameterPropertyPanel is a primary property panel that represent a
 * parameter. It allows the user to edit the parameter name and its type.
 * 
 * @see EntityPrimaryPropertyPanel
 * 
 * @author Adrien Jakubiak
 */
@FactoryObject(types = "com.taf.logic.Entity", generate = true)
public class ParameterPropertyPanel extends EntityPrimaryPropertyPanel {

	private static final long serialVersionUID = 8925850604078710611L;

	/** The type names. */
	private final JComboBox<String> typeNames;

	/**
	 * Instantiates a new parameter property panel.
	 *
	 * @param parameter the parameter
	 */
	public ParameterPropertyPanel(Entity entity) {
		super(entity);
		
		Parameter parameter = (Parameter) entity;

		GridBagConstraints c = Consts.getDefaultConstraint();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP);
		c.weightx = 0;
		c.weighty = 0;
		JLabel parameterLabel = new JLabel(Consts.PARAMETER_NAME_LABEL_TEXT);
		this.add(parameterLabel, c);

		c.insets = new Insets(0, Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		this.add(entityName, c);

		c.insets = new Insets(Consts.SMALL_INSET_GAP, 0, 0, Consts.SMALL_INSET_GAP);
		c.weighty = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		JLabel typeNameLabel = new JLabel(Consts.PARAMETER_TYPE_LABEL_TEXT);
		this.add(typeNameLabel, c);

		c.insets = new Insets(Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP, 0, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		typeNames = new JComboBox<String>(FieldType.PARAMETER_TYPE_SET.toArray(String[]::new));
		String typeName = parameter.getType().getName();
		typeNames.setSelectedItem(typeName);
		typeNames.addActionListener(e -> updateFieldType(parameter));
		this.add(typeNames, c);
	}

	/**
	 * Update parameter type.
	 *
	 * @param parameter the parameter
	 */
	private void updateFieldType(Parameter parameter) {
		String typeName = (String) typeNames.getSelectedItem();
		try {
			FieldType type = ParameterTypeFactory.createFieldType(typeName);
			parameter.setType(type);
			Event event = new ParameterTypeChangedEvent(parameter, type);
			EventManager.getInstance().fireEvent(event);
		} catch (EntityCreationException e) {
			// Ignore
		}
	}

}
