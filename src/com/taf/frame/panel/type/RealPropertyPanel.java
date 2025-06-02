package com.taf.frame.panel.type;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;

import com.taf.logic.type.RealType;
import com.taf.manager.ConstantManager;

public class RealPropertyPanel extends TypePropertyPanel implements PropertyChangeListener {

	private static final long serialVersionUID = -9035183700723112945L;

	private RealType type;
	private NumberFormat format;

	private double minValue;
	private double maxValue;

	private JCheckBox minBox;
	private JFormattedTextField minField;

	private JCheckBox maxBox;
	private JFormattedTextField maxField;

	public RealPropertyPanel(RealType type) {
		this.type = type;
		format = DecimalFormat.getInstance(Locale.US);

		boolean hasMin = type.hasMinParameter();
		boolean hasMax = type.hasMaxParameter();
		if (hasMin) {
			minValue = type.getMinParameter();
		} else {
			minValue = 0;
		}

		if (hasMax) {
			maxValue = type.getMaxParameter();
		} else {
			maxValue = 10;
		}

		GridBagConstraints c = ConstantManager.getDefaultConstraint();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(0, 0, 5, 5);
		c.weightx = 0;
		c.weighty = 0;
		minBox = new JCheckBox("Min");
		minBox.setSelected(hasMin);
		minBox.addActionListener(e -> activateMin());
		addComponent(minBox, c);

		c.insets = new Insets(0, 5, 5, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		minField = new JFormattedTextField(format);
		minField.setValue(minValue);
		minField.setColumns(20);
		minField.setEnabled(hasMin);
		minField.addPropertyChangeListener("value", this);
		addComponent(minField, c);

		c.insets = new Insets(5, 0, 0, 5);
		c.weighty = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		maxBox = new JCheckBox("Max");
		maxBox.setSelected(hasMax);
		maxBox.addActionListener(e -> activateMax());
		addComponent(maxBox, c);

		c.insets = new Insets(5, 5, 0, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		maxField = new JFormattedTextField(format);
		maxField.setValue(maxValue);
		maxField.setColumns(20);
		maxField.setEnabled(hasMax);
		maxField.addPropertyChangeListener("value", this);
		addComponent(maxField, c);
	}

	private void updateMin() {
		type.editMinParameter(minValue);
	}

	private void updateMax() {
		type.editMaxParameter(maxValue);
	}

	private void activateMin() {
		if (minBox.isSelected()) {
			type.addMinParameter(minValue);
			minField.setEnabled(true);
		} else {
			type.removeMinParameter();
			minField.setEnabled(false);
		}
	}

	private void activateMax() {
		if (maxBox.isSelected()) {
			type.addMaxParameter(maxValue);
			maxField.setEnabled(true);
		} else {
			type.removeMaxParameter();
			maxField.setEnabled(false);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object source = evt.getSource();
		if (source == minField) {
			minValue = ((Number) minField.getValue()).doubleValue();
			updateMin();
		} else if (source == maxField) {
			maxValue = ((Number) maxField.getValue()).doubleValue();
			updateMax();
		}
	}

}
