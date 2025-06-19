package com.taf.frame.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.taf.event.EntitySelectedEvent;
import com.taf.event.EventListener;
import com.taf.event.EventMethod;
import com.taf.event.FieldTypeChangedEvent;
import com.taf.frame.panel.entity.EntityPrimaryPanelFactory;
import com.taf.frame.panel.entity.EntityPrimaryPropertyPanel;
import com.taf.frame.panel.type.EntitySecondaryPanelFactory;
import com.taf.frame.panel.type.EntitySecondaryPropertyPanel;
import com.taf.logic.Entity;
import com.taf.logic.constraint.Constraint;
import com.taf.logic.field.Field;
import com.taf.logic.field.Root;
import com.taf.manager.ConstantManager;
import com.taf.manager.EventManager;

public class PropertyPanel extends JPanel implements EventListener {

	private static final long serialVersionUID = -1873086205248435494L;

	private Entity entity;
	private EntityPrimaryPropertyPanel entityPropertyPanel;
	private EntitySecondaryPropertyPanel typePropertyPanel;

	public PropertyPanel() {
		this.setLayout(new GridBagLayout());
		EventManager.getInstance().registerEventListener(this);

		entity = null;
		entityPropertyPanel = null;
		typePropertyPanel = null;

		updatePanel();
	}

	private void updatePanel() {
		// Remove everything from panel
		this.removeAll();

		this.setBorder(BorderFactory.createEmptyBorder(ConstantManager.MEDIUM_INSET_GAP,
				ConstantManager.MEDIUM_INSET_GAP, ConstantManager.MEDIUM_INSET_GAP, ConstantManager.MEDIUM_INSET_GAP));

		if (entity != null) {
			GridBagConstraints c = ConstantManager.getDefaultConstraint();
			c.anchor = GridBagConstraints.NORTH;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.insets = new Insets(ConstantManager.MEDIUM_INSET_GAP, 0, ConstantManager.MEDIUM_INSET_GAP, 0);
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
			c.insets = new Insets(ConstantManager.SMALL_INSET_GAP, 0, ConstantManager.SMALL_INSET_GAP, 0);
			c.gridy = 1;
			c.weighty = noTypeProperty ? 1 : 0;
			JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
			this.add(separator, c);

			if (!noTypeProperty) {
				c.fill = GridBagConstraints.BOTH;
				c.insets = new Insets(ConstantManager.MEDIUM_INSET_GAP, 0, 0, 0);
				c.weighty = 1;
				c.gridy = 2;
				this.add(typePropertyPanel, c);
			}
		}

		// removeAll method invalidates
		this.validate();
	}
	
	@Override
	public void unregisterComponents() {
		// No inner listeners
	}

	@EventMethod
	public void onEntitySelected(EntitySelectedEvent event) {
		this.entity = event.getEntity();
		updatePanel();
	}

	@EventMethod
	public void onFieldTypeChanged(FieldTypeChangedEvent event) {
		updatePanel();
	}

}
