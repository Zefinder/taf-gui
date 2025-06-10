package com.taf.frame.panel.field;

import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

import com.taf.event.Event;
import com.taf.event.FieldNameChangedEvent;
import com.taf.logic.field.Field;
import com.taf.manager.ConstantManager;
import com.taf.manager.EventManager;

public abstract class FieldPropertyPanel extends JPanel {

	private static final long serialVersionUID = -202000016437797783L;
	protected static final String FIELD_LABEL_TEXT = "Parameter name";

	protected final JTextField fieldName;
	private String name;
	
	public FieldPropertyPanel(Field field) {
		this.setLayout(new GridBagLayout());

		this.name = field.getName();

		fieldName = new JTextField(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		fieldName.setText(name);
		fieldName.addActionListener(e -> {
			String text = fieldName.getText();
			updateFieldName(field, name, text);
			name = fieldName.getText();
		});
	}

	protected void updateFieldName(Field field, String oldName, String newName) {
		if (!newName.isBlank()) {
			field.setName(newName);
			
			Event event = new FieldNameChangedEvent(oldName, newName);
			EventManager.getInstance().fireEvent(event);
		}
	}

	
}
