package com.taf.frame.panel.field;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import com.taf.event.Event;
import com.taf.event.FieldTypeChangedEvent;
import com.taf.logic.field.Field;
import com.taf.logic.field.Parameter;
import com.taf.logic.type.Type;
import com.taf.manager.EventManager;
import com.taf.manager.TypeManager;

public class ParameterPropertyPanel extends FieldPropertyPanel {

	private static final long serialVersionUID = 8925850604078710611L;

	private final JComboBox<String> typeNames;

	public ParameterPropertyPanel(Parameter parameter) {
		super(parameter);
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 0, 5, 5);
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;

		c.insets = new Insets(5, 0, 0, 5);
		c.gridx = 0;
		c.gridy = 1;
		JLabel typeNameLabel = new JLabel("Parameter type");
		this.add(typeNameLabel, c);

		c.insets = new Insets(5, 5, 0, 0);
		c.gridx = 1;
		typeNames = new JComboBox<String>(TypeManager.getInstance().getTypeNames().toArray(String[]::new));
		String typeName = parameter.getType().getName();
		typeNames.setSelectedItem(typeName.substring(0, 1).toUpperCase() + typeName.substring(1) + "Type");
		typeNames.addActionListener(e -> updateFieldType(parameter));
		this.add(typeNames, c);
	}
	
	private void updateFieldType(Field field) {
		String typeName = (String) typeNames.getSelectedItem();
		Type type = TypeManager.getInstance().instanciateType(typeName);
		// TODO Add JOptionPane message to confirm you want to change type
		
		field.setType(type);
		Event event = new FieldTypeChangedEvent(type);
		EventManager.getInstance().fireEvent(event);
	}

}
