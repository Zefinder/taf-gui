package com.taf.frame.panel.entity;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import com.taf.event.Event;
import com.taf.event.FieldTypeChangedEvent;
import com.taf.logic.field.Field;
import com.taf.logic.field.Parameter;
import com.taf.logic.type.Type;
import com.taf.manager.ConstantManager;
import com.taf.manager.EventManager;
import com.taf.manager.TypeManager;

public class ParameterPropertyPanel extends EntityPropertyPanel {

	private static final long serialVersionUID = 8925850604078710611L;

	private static final String TYPE_STRING_FORMAT = "%s%sType";

	private final JComboBox<String> typeNames;

	public ParameterPropertyPanel(Parameter parameter) {
		super(parameter);

		GridBagConstraints c = ConstantManager.getDefaultConstraint();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, ConstantManager.SMALL_INSET_GAP, ConstantManager.SMALL_INSET_GAP);
		c.weightx = 0;
		c.weighty = 0;
		JLabel parameterLabel = new JLabel(ConstantManager.PARAMETER_NAME_LABEL_TEXT);
		this.add(parameterLabel, c);

		c.insets = new Insets(0, ConstantManager.SMALL_INSET_GAP, ConstantManager.SMALL_INSET_GAP, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		this.add(entityName, c);

		c.insets = new Insets(ConstantManager.SMALL_INSET_GAP, 0, 0, ConstantManager.SMALL_INSET_GAP);
		c.weighty = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		JLabel typeNameLabel = new JLabel(ConstantManager.PARAMETER_TYPE_LABEL_TEXT);
		this.add(typeNameLabel, c);

		c.insets = new Insets(ConstantManager.SMALL_INSET_GAP, ConstantManager.SMALL_INSET_GAP, 0, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		typeNames = new JComboBox<String>(TypeManager.getInstance().getTypeNames().toArray(String[]::new));
		String typeName = parameter.getType().getName();
		typeNames.setSelectedItem(typeNameToTypeString(typeName));
		typeNames.addActionListener(e -> updateFieldType(parameter));
		this.add(typeNames, c);
	}

	private String typeNameToTypeString(String typeName) {
		return TYPE_STRING_FORMAT.formatted(typeName.substring(0, 1).toUpperCase(), typeName.substring(1));
	}

	private void updateFieldType(Field field) {
		String typeName = (String) typeNames.getSelectedItem();
		Type type = TypeManager.getInstance().instanciateTypeFromClassName(typeName);
		// TODO Add JOptionPane message to confirm you want to change type

		field.setType(type);
		Event event = new FieldTypeChangedEvent(type);
		EventManager.getInstance().fireEvent(event);
	}

}
