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
package com.taf.frame.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.taf.annotation.EventMethod;
import com.taf.annotation.Nullable;
import com.taf.event.EventListener;
import com.taf.event.entity.EntityDeletedEvent;
import com.taf.event.entity.EntitySelectedEvent;
import com.taf.event.entity.ParameterTypeChangedEvent;
import com.taf.frame.panel.primary.EntityPrimaryPanelFactory;
import com.taf.frame.panel.primary.EntityPrimaryPropertyPanel;
import com.taf.frame.panel.secondary.EntitySecondaryPanelFactory;
import com.taf.frame.panel.secondary.EntitySecondaryPropertyPanel;
import com.taf.logic.Entity;
import com.taf.logic.constraint.Constraint;
import com.taf.logic.field.Field;
import com.taf.logic.field.Root;
import com.taf.manager.EventManager;
import com.taf.util.Consts;

/**
 * <p>
 * The PropertyPanel is used in the {@link TafPanel} and manages the properties
 * of the selected entity in the {@link FieldTreePanel}.
 * </p>
 *
 * <p>
 * It is composed of two panels:
 * <ul>
 * <li>{@link EntityPrimaryPropertyPanel}, called primary property panel, which
 * is in charge of the entity name and type if it can have one.
 * <li>{@link EntitySecondaryPanelFactory}, called secondary property panel,
 * which is in charge of the entity type if it has one.
 * </ul>
 * </p>
 *
 * <p>
 * When an entity is selected in the {@link FieldTreePanel}, the previous
 * primary and secondary panels are removed (unregistered from the
 * {@link EventManager} and removed from the layout) and replaced according to
 * the new entity. If an entity is deleted, then the panels are simply removed.
 * </p>
 * 
 * <p>
 * When an entity changes type in the primary property panel, the changes are
 * reflected in this panel removing the secondary property panel and replacing
 * it by a new one according to the new type.
 * </p>
 * 
 * @see JPanel
 * @see TafPanel
 * @see FieldTreePanel
 * @see EntityPrimaryPropertyPanel
 * @see EntitySecondaryPropertyPanel
 *
 * @author Adrien Jakubiak
 */
public class PropertyPanel extends JPanel implements EventListener {

	private static final long serialVersionUID = -1873086205248435494L;

	/** The entity. */
	@Nullable
	private Entity entity;

	/** The primary property panel. */
	@Nullable
	private EntityPrimaryPropertyPanel entityPropertyPanel;

	/** The secondary property panel. */
	@Nullable
	private EntitySecondaryPropertyPanel typePropertyPanel;

	/**
	 * Instantiates a new property panel.
	 */
	public PropertyPanel() {
		this.setLayout(new GridBagLayout());
		EventManager.getInstance().registerEventListener(this);

		entity = null;
		entityPropertyPanel = null;
		typePropertyPanel = null;

		updatePanel();
	}

	/**
	 * Handler for {@link EntityDeletedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onEntityDeleted(EntityDeletedEvent event) {
		// Deselect the current entity and its panels if it is the deleted entity
		if (entity != null && entity.equals(event.getEntity())) {
			entity = null;
			entityPropertyPanel = null;
			if (typePropertyPanel != null) {
				EventManager.getInstance().unregisterEventListener(typePropertyPanel);
				typePropertyPanel = null;
			}
			updatePanel();
		}
	}

	/**
	 * Handler for {@link EntitySelectedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onEntitySelected(EntitySelectedEvent event) {
		this.entity = event.getEntity();
		// Don't forget to unregister here since the property panel will change
		if (typePropertyPanel != null) {
			EventManager.getInstance().unregisterEventListener(typePropertyPanel);
		}
		updatePanel();
	}

	/**
	 * Handler for {@link ParameterTypeChangedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onFieldTypeChanged(ParameterTypeChangedEvent event) {
		// The field type changed so the type property panel will also change
		if (typePropertyPanel != null) {
			EventManager.getInstance().unregisterEventListener(typePropertyPanel);
		}
		updatePanel();
	}

	@Override
	public void unregisterComponents() {
		// Unregister type property panel if it isn't already null
		if (typePropertyPanel != null) {
			EventManager.getInstance().unregisterEventListener(typePropertyPanel);
		}
	}

	/**
	 * Updates the panel. Use when a change occurred.
	 */
	private void updatePanel() {
		// Remove everything from panel
		this.removeAll();

		this.setBorder(BorderFactory.createEmptyBorder(Consts.MEDIUM_INSET_GAP, Consts.MEDIUM_INSET_GAP,
				Consts.MEDIUM_INSET_GAP, Consts.MEDIUM_INSET_GAP));

		if (entity != null) {
			GridBagConstraints c = Consts.getDefaultConstraint();
			c.anchor = GridBagConstraints.NORTH;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.insets = new Insets(Consts.MEDIUM_INSET_GAP, 0, Consts.MEDIUM_INSET_GAP, 0);
			c.weighty = 0;
			entityPropertyPanel = EntityPrimaryPanelFactory.createEntityPropertyPanel(entity);
			this.add(entityPropertyPanel, c);

			typePropertyPanel = null;
			if (entity instanceof Root) {
				typePropertyPanel = EntitySecondaryPanelFactory.createRootPropertyPanel();
			} else if (entity instanceof Field) {
				typePropertyPanel = EntitySecondaryPanelFactory.createFieldPropertyPanel(((Field) entity).getType());
			} else if (entity instanceof Constraint) {
				typePropertyPanel = EntitySecondaryPanelFactory.createConstraintPropertyPanel((Constraint) entity);
			}

			boolean noTypeProperty = typePropertyPanel == null;
			c.insets = new Insets(Consts.SMALL_INSET_GAP, 0, Consts.SMALL_INSET_GAP, 0);
			c.gridy = 1;
			c.weighty = noTypeProperty ? 1 : 0;
			JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
			this.add(separator, c);

			if (!noTypeProperty) {
				c.fill = GridBagConstraints.BOTH;
				c.insets = new Insets(Consts.MEDIUM_INSET_GAP, 0, 0, 0);
				c.weighty = 1;
				c.gridy = 2;
				this.add(typePropertyPanel, c);
			}
		}

		// removeAll method invalidates the panel
		this.validate();
	}

}
