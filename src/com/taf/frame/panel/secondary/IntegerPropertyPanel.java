/*
 * Copyright or © or Copr.
 * 
 * This software is a computer program whose purpose is to generate random test
 * case from a template file describing the data model.
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software. You can use, modify
 * and/or redistribute the software under the terms of the CeCILL-B license as
 * circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * 
 * As a counterpart to the access to the source code and rights to copy, modify
 * and redistribute granted by the license, users are provided only with a
 * limited warranty and the software's author, the holder of the economic
 * rights, and the successive licensors have only limited liability.
 * 
 * In this respect, the user's attention is drawn to the risks associated with
 * loading, using, modifying and/or developing or reproducing the software by
 * the user in light of its specific status of free software, that may mean that
 * it is complicated to manipulate, and that also therefore means that it is
 * reserved for developers and experienced professionals having in-depth
 * computer knowledge. Users are therefore encouraged to load and test the
 * software's suitability as regards their requirements in conditions enabling
 * the security of their systems and/or data to be ensured and, more generally,
 * to use and operate it in the same conditions as regards security.
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package com.taf.frame.panel.secondary;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

import com.taf.annotation.FactoryObject;
import com.taf.logic.type.FieldType;
import com.taf.logic.type.IntegerType;
import com.taf.logic.type.parameter.DistributionType;
import com.taf.util.Consts;

/**
 * The IntegerPropertyPanel is a secondary property panel used for
 * {@link IntegerType}. It allows the user to set the minimum and maximum value
 * of the parameter, and the distribution.
 * 
 * @see EntitySecondaryPropertyPanel
 * @see DistributionPanel
 *
 * @author Adrien Jakubiak
 */
@FactoryObject(types = "com.taf.logic.type.FieldType", generate = true)
public class IntegerPropertyPanel extends EntitySecondaryPropertyPanel implements PropertyChangeListener {

	private static final long serialVersionUID = -9035183700723112945L;

	/** The distribution label text. */
	private static final String DISTRIBUTION_LABEL_TEXT = "Distribution";

	/** The entity type. */
	private IntegerType type;

	/** The minimum value. */
	private long minValue;

	/** The maximum value. */
	private long maxValue;

	/** The minimum label. */
	private JLabel minLabel;

	/** The minimum field. */
	private JFormattedTextField minField;

	/** The maximum label. */
	private JLabel maxLabel;

	/** The maximum field. */
	private JFormattedTextField maxField;

	/** The distribution label. */
	private JLabel distributionLabel;

	/** The distribution box. */
	private JComboBox<DistributionType> distributionBox;

	/** The distribution panel. */
	private DistributionPanel distributionPanel;

	/**
	 * Instantiates a new integer property panel.
	 *
	 * @param type the type
	 */
	public IntegerPropertyPanel(FieldType fieldType) {
		this.type = (IntegerType) fieldType;
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
		minField = new JFormattedTextField(minValue);
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
		c.gridheight = 1;
		c.gridx = 1;
		maxField = new JFormattedTextField(maxValue);
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

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object source = evt.getSource();
		if (source == minField) {
			minValue = ((Number) minField.getValue()).longValue();
			updateMin();
		} else if (source == maxField) {
			maxValue = ((Number) maxField.getValue()).longValue();
			updateMax();
		}
	}

	/**
	 * Update the maximum value.
	 */
	private void updateMax() {
		type.editMax(maxValue);
	}

	/**
	 * Update the minimum value.
	 */
	private void updateMin() {
		type.editMin(minValue);
	}

}
