package com.taf.frame.panel.field;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.taf.event.Event;
import com.taf.event.FieldNameChangedEvent;
import com.taf.logic.field.Field;
import com.taf.manager.EventManager;

public abstract class FieldPropertyPanel extends JPanel {

	private static final long serialVersionUID = -202000016437797783L;

	private final JTextField fieldName;
	private String name;
	
	public FieldPropertyPanel(Field field) {
		this.setLayout(new GridBagLayout());

		this.name = field.getName();

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 0, 5, 5);
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		JLabel fieldLabel = new JLabel("Parameter name");
		this.add(fieldLabel, c);

		c.insets = new Insets(0, 5, 5, 0);
		c.gridx = 1;
		fieldName = new JTextField(20);
		fieldName.setText(name);
		fieldName.addActionListener(e -> {
			String text = fieldName.getText();
			updateFieldName(field, name, text);
			name = fieldName.getText();
		});
		this.add(fieldName, c);
	}

	protected void updateFieldName(Field field, String oldName, String newName) {
		if (!newName.isBlank()) {
			field.setName(newName);
			
			Event event = new FieldNameChangedEvent(oldName, newName);
			EventManager.getInstance().fireEvent(event);
		}
	}

	
}
