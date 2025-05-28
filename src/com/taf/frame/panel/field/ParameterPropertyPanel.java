package com.taf.frame.panel.field;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.taf.logic.field.Parameter;
import com.taf.manager.TypeManager;

public class ParameterPropertyPanel extends FieldPropertyPanel {

	private static final long serialVersionUID = 8925850604078710611L;

	private final JTextField fieldName;
	private final JComboBox<String> typeNames;

	public ParameterPropertyPanel(Parameter parameter) {
		this.setLayout(new GridBagLayout());
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
		fieldName.setText(parameter.getName());
		this.add(fieldName, c);

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
		this.add(typeNames, c);
	}

}
