package com.taf.frame.panel.secondary;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

import com.taf.logic.type.RealType;
import com.taf.logic.type.parameter.DistributionType;
import com.taf.util.Consts;

public class RealPropertyPanel extends EntitySecondaryPropertyPanel implements PropertyChangeListener {

	private static final long serialVersionUID = -9035183700723112945L;

	private static final String DISTRIBUTION_LABEL_TEXT = "Distribution";
	
	private RealType type;
	private NumberFormat format;

	private double minValue;
	private double maxValue;

	private JLabel minLabel;
	private JFormattedTextField minField;

	private JLabel maxLabel;
	private JFormattedTextField maxField;
	
	private JLabel distributionLabel;
	private JComboBox<DistributionType> distributionBox;

	private DistributionPanel distributionPanel;

	public RealPropertyPanel(RealType type) {
		this.type = type;
		format = DecimalFormat.getInstance(Locale.US);
		minValue = type.getMin();
		maxValue = type.getMax();
		
		GridBagConstraints c = Consts.getDefaultConstraint();
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(0, 0, Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP);
		c.weightx = 0;
		c.weighty = 0;
		minLabel = new JLabel(Consts.MIN_TEXT);
		addComponent(minLabel, c);

		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		minField = new JFormattedTextField(format);
		minField.setValue(minValue);
		minField.setColumns(Consts.JTEXT_FIELD_DEFAULT_COLUMN);
		minField.addPropertyChangeListener(Consts.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY, this);
		addComponent(minField, c);

		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(Consts.SMALL_INSET_GAP, 0, 0, Consts.SMALL_INSET_GAP);
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		maxLabel = new JLabel(Consts.MAX_TEXT);
		addComponent(maxLabel, c);

		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP, 0, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		maxField = new JFormattedTextField(format);
		maxField.setValue(maxValue);
		maxField.setColumns(Consts.JTEXT_FIELD_DEFAULT_COLUMN);
		maxField.addPropertyChangeListener(Consts.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY, this);
		addComponent(maxField, c);
		
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(Consts.MEDIUM_INSET_GAP, 0, 0, Consts.SMALL_INSET_GAP);
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 2;
		distributionLabel = new JLabel(DISTRIBUTION_LABEL_TEXT);
		addComponent(distributionLabel, c);

		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(Consts.MEDIUM_INSET_GAP, Consts.SMALL_INSET_GAP, 0, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		distributionBox = new JComboBox<DistributionType>(DistributionType.values());
		distributionBox.addActionListener(e -> {
			DistributionType distributionType = (DistributionType) distributionBox.getSelectedItem();
			switch (distributionType) {
			case UNIFORM:
				distributionPanel.showUniformPanel();
				break;

			case NORMAL:
				distributionPanel.showNormalPanel();
				break;

			case INTERVAL:
				distributionPanel.showIntervalPanel();
				break;
			}
			type.setDistribution(distributionType);
		});
		addComponent(distributionBox, c);

		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(Consts.MEDIUM_INSET_GAP, 0, 0, Consts.SMALL_INSET_GAP);
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 3;
		distributionPanel = new DistributionPanel(type);
		distributionBox.setSelectedItem(type.getDistribution());
		addComponent(distributionPanel, c);
	}

	private void updateMin() {
		type.editMinNumber(minValue);
	}

	private void updateMax() {
		type.editMaxNumber(maxValue);
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
