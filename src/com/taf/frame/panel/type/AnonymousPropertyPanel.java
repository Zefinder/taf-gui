package com.taf.frame.panel.type;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ButtonGroup;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.taf.logic.type.AnonymousType;
import com.taf.manager.ConstantManager;

public class AnonymousPropertyPanel extends TypePropertyPanel implements PropertyChangeListener {

	private static final long serialVersionUID = 293578359212546065L;

	private static final String INSTANCE_NUMBER_BUTTON_NAME = "Fixed instance number";
	private static final String MIN_MAX_INSTANCE_BUTTON_NAME = "Min-max instance number";
	private static final String INSTANCE_LABEL_TEXT = "Instance number";
	private static final String MIN_INSTANCE_LABEL_TEXT = "Min instance number";
	private static final String MAX_INSTANCE_LABEL_TEXT = "Max instance number";

	private static final int MAX_COLUMN_NUMBER = 4;

	private AnonymousType type;

	private JFormattedTextField instanceField;
	private JFormattedTextField minInstanceField;
	private JFormattedTextField maxInstanceField;

	private int instanceNumber;
	private int minInstanceNumber;
	private int maxInstanceNumber;

	public AnonymousPropertyPanel(AnonymousType type) {
		this.type = type;

		boolean hasMinMax = type.hasMinMaxInstance();
		instanceNumber = type.getInstanceNumberParameter();
		minInstanceNumber = ConstantManager.DEFAULT_MIN_INSTANCE_NUMBER;
		maxInstanceNumber = ConstantManager.DEFAULT_MAX_INSTANCE_NUMBER;
		if (type.hasMinMaxInstance()) {
			minInstanceNumber = type.getMinInstanceParameter();
			maxInstanceNumber = type.getMaxInstanceParameter();
		}

		GridBagConstraints c = ConstantManager.getDefaultConstraint();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		JRadioButton instanceButton = new JRadioButton(INSTANCE_NUMBER_BUTTON_NAME);
		instanceButton.setSelected(!hasMinMax);
		instanceButton.addActionListener(e -> activateFixedInstanceNumber(true));
		this.add(instanceButton, c);

		c.gridx = 0;
		c.gridy = 1;
		this.add(createFixedInstancePanel(hasMinMax), c);

		c.gridx = 0;
		c.gridy = 2;
		JRadioButton minMaxButton = new JRadioButton(MIN_MAX_INSTANCE_BUTTON_NAME);
		minMaxButton.setSelected(hasMinMax);
		minMaxButton.addActionListener(e -> activateFixedInstanceNumber(false));
		this.add(minMaxButton, c);

		c.weighty = 1;
		c.gridy = 3;
		this.add(createMinMaxInstancePanel(hasMinMax), c);

		ButtonGroup group = new ButtonGroup();
		group.add(instanceButton);
		group.add(minMaxButton);
	}

	private JPanel createFixedInstancePanel(boolean hasMinMax) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints c = ConstantManager.getDefaultConstraint();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 10, 10);
		c.weightx = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		JLabel instanceLabel = new JLabel(INSTANCE_LABEL_TEXT);
		panel.add(instanceLabel, c);

		c.gridx = 1;
		c.insets = new Insets(0, 0, 15, 0);
		instanceField = new JFormattedTextField(ConstantManager.DEFAULT_INSTANCE_NUMBER);
		instanceField.setEnabled(!hasMinMax);
		instanceField.addPropertyChangeListener(ConstantManager.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY, this);
		instanceField.setColumns(MAX_COLUMN_NUMBER);
		panel.add(instanceField, c);

		return panel;
	}

	private JPanel createMinMaxInstancePanel(boolean hasMinMax) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints c = ConstantManager.getDefaultConstraint();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 5, 10);
		c.weightx = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		JLabel minInstanceLabel = new JLabel(MIN_INSTANCE_LABEL_TEXT);
		panel.add(minInstanceLabel, c);

		c.gridx = 1;
		c.insets = new Insets(0, 0, 5, 0);
		minInstanceField = new JFormattedTextField(minInstanceNumber);
		minInstanceField.setColumns(MAX_COLUMN_NUMBER);
		minInstanceField.addPropertyChangeListener(ConstantManager.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY, this);
		minInstanceField.setEnabled(hasMinMax);
		panel.add(minInstanceField, c);

		c.insets = new Insets(0, 0, 10, 10);
		c.gridx = 0;
		c.gridy = 1;
		JLabel maxInstanceLabel = new JLabel(MAX_INSTANCE_LABEL_TEXT);
		panel.add(maxInstanceLabel, c);

		c.gridx = 1;
		c.insets = new Insets(0, 0, 10, 0);
		maxInstanceField = new JFormattedTextField(maxInstanceNumber);
		maxInstanceField.setColumns(MAX_COLUMN_NUMBER);
		maxInstanceField.addPropertyChangeListener(ConstantManager.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY, this);
		maxInstanceField.setEnabled(hasMinMax);
		panel.add(maxInstanceField, c);

		return panel;
	}

	private void activateFixedInstanceNumber(boolean activate) {
		instanceField.setEnabled(activate);
		minInstanceField.setEnabled(!activate);
		maxInstanceField.setEnabled(!activate);

		if (activate) {
			type.removeMinMaxInstanceParameter();
		} else {
			type.addMinMaxInstanceParameter(minInstanceNumber, maxInstanceNumber);
		}
	}

	private void updateInstanceNumber() {
		type.editInstanceNumberParameter(instanceNumber);
	}

	private void updateMinInstanceNumber() {
		type.editMinInstanceParameter(minInstanceNumber);
	}

	private void updateMaxInstanceNumber() {
		type.editMaxInstanceParameter(maxInstanceNumber);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object source = evt.getSource();
		if (source == instanceField) {
			instanceNumber = ((Number) instanceField.getValue()).intValue();
			updateInstanceNumber();
		} else if (source == minInstanceField) {
			minInstanceNumber = ((Number) minInstanceField.getValue()).intValue();
			updateMinInstanceNumber();
		} else if (source == maxInstanceField) {
			maxInstanceNumber = ((Number) maxInstanceField.getValue()).intValue();
			updateMaxInstanceNumber();
		}
	}

}
