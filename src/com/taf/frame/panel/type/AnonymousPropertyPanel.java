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
		minInstanceNumber = 1;
		maxInstanceNumber = 1;
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
		JRadioButton instanceButton = new JRadioButton("Fixed instance number");
		instanceButton.setSelected(!hasMinMax);
		instanceButton.addActionListener(e -> activateFixedInstanceNumber(true));
		this.add(instanceButton, c);

		c.gridx = 0;
		c.gridy = 1;
		this.add(createFixedInstancePanel(hasMinMax), c);

		c.gridx = 0;
		c.gridy = 2;
		JRadioButton minMaxButton = new JRadioButton("Min-max instance number");
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
		JLabel instanceLabel = new JLabel("Instance number");
		panel.add(instanceLabel, c);

		c.gridx = 1;
		c.insets = new Insets(0, 0, 15, 0);
		instanceField = new JFormattedTextField(1);
		instanceField.setEnabled(!hasMinMax);
		instanceField.addPropertyChangeListener("value", this);
		instanceField.setColumns(4);
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
		JLabel minInstanceLabel = new JLabel("Min instance number");
		panel.add(minInstanceLabel, c);

		c.gridx = 1;
		c.insets = new Insets(0, 0, 5, 0);
		minInstanceField = new JFormattedTextField(minInstanceNumber);
		minInstanceField.setColumns(4);
		minInstanceField.addPropertyChangeListener("value", this);
		minInstanceField.setEnabled(hasMinMax);
		panel.add(minInstanceField, c);

		c.insets = new Insets(0, 0, 10, 10);
		c.gridx = 0;
		c.gridy = 1;
		JLabel maxInstanceLabel = new JLabel("Max instance number");
		panel.add(maxInstanceLabel, c);

		c.gridx = 1;
		c.insets = new Insets(0, 0, 10, 0);
		maxInstanceField = new JFormattedTextField(maxInstanceNumber);
		maxInstanceField.setColumns(4);
		maxInstanceField.addPropertyChangeListener("value", this);
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
