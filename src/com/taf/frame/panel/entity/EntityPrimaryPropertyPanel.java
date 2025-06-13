package com.taf.frame.panel.entity;

import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

import com.taf.event.Event;
import com.taf.event.EntityNameChangedEvent;
import com.taf.logic.Entity;
import com.taf.manager.ConstantManager;
import com.taf.manager.EventManager;

public abstract class EntityPrimaryPropertyPanel extends JPanel {

	private static final long serialVersionUID = -202000016437797783L;

	protected final JTextField entityName;
	private String name;
	
	public EntityPrimaryPropertyPanel(Entity entity) {
		this.setLayout(new GridBagLayout());

		this.name = entity.getName();

		entityName = new JTextField(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		entityName.setText(name);
		entityName.addActionListener(e -> {
			String text = entityName.getText();
			updateFieldName(entity, name, text);
			name = entityName.getText();
		});
	}

	protected void updateFieldName(Entity entity, String oldName, String newName) {
		if (!newName.isBlank()) {
			entity.setName(newName);
			
			Event event = new EntityNameChangedEvent(oldName, newName);
			EventManager.getInstance().fireEvent(event);
		}
	}

	
}
