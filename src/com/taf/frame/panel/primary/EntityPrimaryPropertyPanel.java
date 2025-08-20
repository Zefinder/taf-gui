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

import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

import com.taf.annotation.NotEmpty;
import com.taf.event.Event;
import com.taf.event.entity.EntityNameChangedEvent;
import com.taf.frame.panel.PropertyPanel;
import com.taf.frame.panel.secondary.EntitySecondaryPropertyPanel;
import com.taf.logic.Entity;
import com.taf.manager.EventManager;
import com.taf.util.Consts;

/**
 * <p>
 * The EntityPrimaryPropertyPanel (called primary property panel) is used in the
 * {@link PropertyPanel} and manages the informations stored by the entity
 * itself. These informations are:
 * <ul>
 * <li>the entity name, for all entities.
 * <li>the entity type, for some entities.
 * </ul>
 * 
 * Primary property panels are created using the
 * {@link EntityPrimaryPanelFactory} and must have a constructor with one
 * argument, which must be an entity.
 * </p>
 * 
 * <p>
 * The type informations are stored in the {@link EntitySecondaryPropertyPanel}.
 * </p>
 * 
 * @see JPanel
 * @see PropertyPanel
 * @see EntitySecondaryPropertyPanel
 *
 * @author Adrien Jakubiak
 */
public abstract class EntityPrimaryPropertyPanel extends JPanel {

	private static final long serialVersionUID = -202000016437797783L;

	/** The entity name field. */
	protected final JTextField entityName;

	/** The cached entity name. */
	@NotEmpty
	private String name;

	/**
	 * Instantiates a new entity primary property panel.
	 *
	 * @param entity the entity
	 */
	public EntityPrimaryPropertyPanel(Entity entity) {
		this.setLayout(new GridBagLayout());

		this.name = entity.getName();
		
		entityName = new JTextField(Consts.JTEXT_FIELD_DEFAULT_COLUMN);
		entityName.setText(name);
		entityName.addActionListener(e -> {
			// TODO Add not empty check
			// TODO Sanitize input
			String text = entityName.getText();
			updateFieldName(entity, name, text);
			name = entityName.getText();
		});

		// TODO Add the name field in the panel here and not in subclasses
	}

	/**
	 * Update the field name.
	 *
	 * @param entity  the entity
	 * @param oldName the old name
	 * @param newName the new name
	 */
	protected void updateFieldName(Entity entity, String oldName, String newName) {
		if (!newName.isBlank()) {
			entity.setName(newName);

			Event event = new EntityNameChangedEvent(entity, oldName, newName);
			EventManager.getInstance().fireEvent(event);
		}
	}
	
	// TODO Add a method to add a component

}
