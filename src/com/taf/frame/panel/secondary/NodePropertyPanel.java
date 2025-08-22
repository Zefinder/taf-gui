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
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ButtonGroup;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.taf.annotation.EventMethod;
import com.taf.annotation.FactoryObject;
import com.taf.event.entity.NodeTypeChangedEvent;
import com.taf.frame.panel.secondary.type.TypeAddEntityPanel;
import com.taf.logic.field.Node;
import com.taf.logic.type.FieldType;
import com.taf.logic.type.NodeType;
import com.taf.util.Consts;

/**
 * The NodePropertyPanel is a secondary property panel used for {@link Node}s.
 * It allows the user to set the instance number, and the depth number if the
 * node is recursive. It can also set minimum and maximum instance number and
 * depth.
 *
 * @see EntitySecondaryPropertyPanel
 *
 * @author Adrien Jakubiak
 */
@FactoryObject(types = FieldType.class, generate = true)
public class NodePropertyPanel extends EntitySecondaryPropertyPanel implements PropertyChangeListener {

	private static final long serialVersionUID = 293578359212546065L;

	/** The instance number button name. */
	private static final String INSTANCE_NUMBER_BUTTON_NAME = "Fixed instance number";

	/** The minimum maximum instance button name. */
	private static final String MIN_MAX_INSTANCE_BUTTON_NAME = "Min-max instance number";

	/** The depth number button name. */
	private static final String DEPTH_NUMBER_BUTTON_NAME = "Fixed depth number";

	/** The minimum maximum depth button name. */
	private static final String MIN_MAX_DEPTH_BUTTON_NAME = "Min-max depth number";

	/** The instance label text. */
	private static final String INSTANCE_LABEL_TEXT = "Instance number";

	/** The minimum instance label text. */
	private static final String MIN_INSTANCE_LABEL_TEXT = "Min instance number";

	/** The maximum instance label text. */
	private static final String MAX_INSTANCE_LABEL_TEXT = "Max instance number";

	/** The depth label text. */
	private static final String DEPTH_LABEL_TEXT = "Depth number";

	/** The minimum depth label text. */
	private static final String MIN_DEPTH_LABEL_TEXT = "Min depth number";

	/** The maximum depth label text. */
	private static final String MAX_DEPTH_LABEL_TEXT = "Max depth number";

	/** The maximum column number. */
	private static final int MAX_COLUMN_NUMBER = 4;

	/** The type. */
	private NodeType type;

	/** The depth button. */
	private JRadioButton depthButton;

	/** The minimum maximum depth button. */
	private JRadioButton minMaxDepthButton;

	/** The instance field. */
	private JFormattedTextField instanceField;

	/** The minimum instance field. */
	private JFormattedTextField minInstanceField;

	/** The maximum instance field. */
	private JFormattedTextField maxInstanceField;

	/** The depth field. */
	private JFormattedTextField depthField;

	/** The minimum depth field. */
	private JFormattedTextField minDepthField;

	/** The maximum depth field. */
	private JFormattedTextField maxDepthField;

	/** The instance number. */
	private int instanceNumber;

	/** The minimum instance number. */
	private int minInstanceNumber;

	/** The maximum instance number. */
	private int maxInstanceNumber;

	/** The depth number. */
	private int depthNumber;

	/** The minimum depth number. */
	private int minDepthNumber;

	/** The maximum depth number. */
	private int maxDepthNumber;

	/**
	 * Instantiates a new node property panel.
	 *
	 * @param type the type
	 */
	public NodePropertyPanel(NodeType type) {
		this.type = type;

		boolean hasMinMaxInstance = type.hasMinMaxInstance();
		instanceNumber = type.getInstanceNumber();
		minInstanceNumber = type.getMinInstanceNumber();
		maxInstanceNumber = type.getMaxInstanceNumber();

		boolean isRecursive = type.isRecursiveNode();
		boolean hasMinMaxDepth = type.hasMinMaxDepth();
		depthNumber = type.getDepthNumber();
		minDepthNumber = type.getMinDepth();
		maxDepthNumber = type.getMaxDepth();

		GridBagConstraints c = Consts.getDefaultConstraint();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		JRadioButton instanceButton = new JRadioButton(INSTANCE_NUMBER_BUTTON_NAME);
		instanceButton.setSelected(!hasMinMaxInstance);
		instanceButton.addActionListener(e -> activateFixedInstanceNumber(true));
		this.add(instanceButton, c);

		c.gridy = 1;
		this.add(createFixedInstancePanel(hasMinMaxInstance), c);

		c.gridy = 2;
		JRadioButton minMaxInstanceButton = new JRadioButton(MIN_MAX_INSTANCE_BUTTON_NAME);
		minMaxInstanceButton.setSelected(hasMinMaxInstance);
		minMaxInstanceButton.addActionListener(e -> activateFixedInstanceNumber(false));
		this.add(minMaxInstanceButton, c);

		c.gridy = 3;
		this.add(createMinMaxInstancePanel(hasMinMaxInstance), c);

		c.gridy = 4;
		depthButton = new JRadioButton(DEPTH_NUMBER_BUTTON_NAME);
		depthButton.setSelected(!hasMinMaxDepth);
		depthButton.setEnabled(isRecursive);
		depthButton.addActionListener(e -> activateFixedDepthNumber(type.isRecursiveNode(), true));
		this.add(depthButton, c);

		c.gridy = 5;
		this.add(createFixedDepthPanel(isRecursive, hasMinMaxDepth), c);

		c.gridy = 6;
		minMaxDepthButton = new JRadioButton(MIN_MAX_DEPTH_BUTTON_NAME);
		minMaxDepthButton.setSelected(hasMinMaxDepth);
		minMaxDepthButton.setEnabled(isRecursive);
		minMaxDepthButton.addActionListener(e -> activateFixedDepthNumber(type.isRecursiveNode(), false));
		this.add(minMaxDepthButton, c);

		c.gridy = 7;
		this.add(createMinMaxDepthPanel(isRecursive, hasMinMaxDepth), c);

		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(Consts.MEDIUM_INSET_GAP, 0, 0, 0);
		c.gridy = 8;
		JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
		this.add(separator, c);

		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(Consts.HUGE_INSET_GAP, 0, 0, 0);
		c.weightx = 0;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 9;
		this.add(new TypeAddEntityPanel(), c);

		ButtonGroup instanceGroup = new ButtonGroup();
		instanceGroup.add(instanceButton);
		instanceGroup.add(minMaxInstanceButton);

		ButtonGroup depthGroup = new ButtonGroup();
		depthGroup.add(depthButton);
		depthGroup.add(minMaxDepthButton);

	}

	/**
	 * Handler for {@link NodeTypeChangedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onNodeTypeChanged(NodeTypeChangedEvent event) {
		// Only the shown node can change type
		boolean isRecursive = type.isRecursiveNode();
		activateFixedDepthNumber(isRecursive, !type.hasMinMaxDepth());
		depthButton.setEnabled(isRecursive);
		minMaxDepthButton.setEnabled(isRecursive);
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
		} else if (source == depthField) {
			depthNumber = ((Number) depthField.getValue()).intValue();
			updateDepthNumber();
		} else if (source == maxDepthField) {
			maxDepthNumber = ((Number) maxDepthField.getValue()).intValue();
			updateMaxDepthNumber();
		} else if (source == minDepthField) {
			minDepthNumber = ((Number) minDepthField.getValue()).intValue();
			updateMinDepthNumber();
		}
	}

	/**
	 * Activate fixed depth number.
	 *
	 * @param isRecursive true if the node is recursive
	 * @param activate    true if the radio button is on fixed activate
	 */
	private void activateFixedDepthNumber(boolean isRecursive, boolean activate) {
		depthField.setEnabled(isRecursive && activate);
		minDepthField.setEnabled(isRecursive && !activate);
		maxDepthField.setEnabled(isRecursive && !activate);
		type.setMinMaxDepth(!activate);
	}

	/**
	 * Activate fixed instance number.
	 *
	 * @param activate true if the radio button is on fixed activate
	 */
	private void activateFixedInstanceNumber(boolean activate) {
		instanceField.setEnabled(activate);
		minInstanceField.setEnabled(!activate);
		maxInstanceField.setEnabled(!activate);
		type.setMinMaxInstance(!activate);
	}

	/**
	 * Creates the fixed depth panel.
	 *
	 * @param isRecursive true if the node is recursive
	 * @param hasMinMax   true if the node minimum maximum depth
	 * @return the j panel
	 */
	private JPanel createFixedDepthPanel(boolean isRecursive, boolean hasMinMax) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints c = Consts.getDefaultConstraint();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, Consts.MEDIUM_INSET_GAP, Consts.MEDIUM_INSET_GAP);
		c.weightx = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		JLabel depthLabel = new JLabel(DEPTH_LABEL_TEXT);
		panel.add(depthLabel, c);

		c.gridx = 1;
		c.insets = new Insets(0, 0, Consts.LARGE_INSET_GAP, 0);
		depthField = new JFormattedTextField(depthNumber);
		depthField.setEnabled(isRecursive && !hasMinMax);
		depthField.addPropertyChangeListener(Consts.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY, this);
		depthField.setColumns(MAX_COLUMN_NUMBER);
		panel.add(depthField, c);

		return panel;
	}

	/**
	 * Creates the fixed instance panel.
	 *
	 * @param hasMinMax true if the node has minimum maximum instance number
	 * @return the j panel
	 */
	private JPanel createFixedInstancePanel(boolean hasMinMax) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints c = Consts.getDefaultConstraint();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, Consts.MEDIUM_INSET_GAP, Consts.MEDIUM_INSET_GAP);
		c.weightx = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		JLabel instanceLabel = new JLabel(INSTANCE_LABEL_TEXT);
		panel.add(instanceLabel, c);

		c.gridx = 1;
		c.insets = new Insets(0, 0, Consts.LARGE_INSET_GAP, 0);
		instanceField = new JFormattedTextField(instanceNumber);
		instanceField.setEnabled(!hasMinMax);
		instanceField.addPropertyChangeListener(Consts.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY, this);
		instanceField.setColumns(MAX_COLUMN_NUMBER);
		panel.add(instanceField, c);

		return panel;
	}

	/**
	 * Creates the minimum maximum depth panel.
	 *
	 * @param isRecursive true if the node is recursive
	 * @param hasMinMax   true if the node minimum maximum depth
	 * @return the j panel
	 */
	private JPanel createMinMaxDepthPanel(boolean isRecursive, boolean hasMinMax) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints c = Consts.getDefaultConstraint();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, Consts.SMALL_INSET_GAP, Consts.MEDIUM_INSET_GAP);
		c.weightx = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		JLabel minDepthLabel = new JLabel(MIN_DEPTH_LABEL_TEXT);
		panel.add(minDepthLabel, c);

		c.gridx = 1;
		c.insets = new Insets(0, 0, Consts.SMALL_INSET_GAP, 0);
		minDepthField = new JFormattedTextField(minDepthNumber);
		minDepthField.setColumns(MAX_COLUMN_NUMBER);
		minDepthField.addPropertyChangeListener(Consts.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY, this);
		minDepthField.setEnabled(isRecursive && hasMinMax);
		panel.add(minDepthField, c);

		c.insets = new Insets(0, 0, Consts.MEDIUM_INSET_GAP, Consts.MEDIUM_INSET_GAP);
		c.gridx = 0;
		c.gridy = 1;
		JLabel maxDepthLabel = new JLabel(MAX_DEPTH_LABEL_TEXT);
		panel.add(maxDepthLabel, c);

		c.gridx = 1;
		c.insets = new Insets(0, 0, Consts.MEDIUM_INSET_GAP, 0);
		maxDepthField = new JFormattedTextField(maxDepthNumber);
		maxDepthField.setColumns(MAX_COLUMN_NUMBER);
		maxDepthField.addPropertyChangeListener(Consts.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY, this);
		maxDepthField.setEnabled(isRecursive && hasMinMax);
		panel.add(maxDepthField, c);

		return panel;
	}

	/**
	 * Creates the minimum maximum instance panel.
	 *
	 * @param hasMinMax true if the node has minimum maximum instance number
	 * @return the j panel
	 */
	private JPanel createMinMaxInstancePanel(boolean hasMinMax) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints c = Consts.getDefaultConstraint();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, Consts.SMALL_INSET_GAP, Consts.MEDIUM_INSET_GAP);
		c.weightx = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		JLabel minInstanceLabel = new JLabel(MIN_INSTANCE_LABEL_TEXT);
		panel.add(minInstanceLabel, c);

		c.gridx = 1;
		c.insets = new Insets(0, 0, Consts.SMALL_INSET_GAP, 0);
		minInstanceField = new JFormattedTextField(minInstanceNumber);
		minInstanceField.setColumns(MAX_COLUMN_NUMBER);
		minInstanceField.addPropertyChangeListener(Consts.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY, this);
		minInstanceField.setEnabled(hasMinMax);
		panel.add(minInstanceField, c);

		c.insets = new Insets(0, 0, Consts.MEDIUM_INSET_GAP, Consts.MEDIUM_INSET_GAP);
		c.gridx = 0;
		c.gridy = 1;
		JLabel maxInstanceLabel = new JLabel(MAX_INSTANCE_LABEL_TEXT);
		panel.add(maxInstanceLabel, c);

		c.gridx = 1;
		c.insets = new Insets(0, 0, Consts.MEDIUM_INSET_GAP, 0);
		maxInstanceField = new JFormattedTextField(maxInstanceNumber);
		maxInstanceField.setColumns(MAX_COLUMN_NUMBER);
		maxInstanceField.addPropertyChangeListener(Consts.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY, this);
		maxInstanceField.setEnabled(hasMinMax);
		panel.add(maxInstanceField, c);

		return panel;
	}

	/**
	 * Update depth number.
	 */
	private void updateDepthNumber() {
		type.editDepthNumber(depthNumber);
	}

	/**
	 * Update instance number.
	 */
	private void updateInstanceNumber() {
		type.editInstanceNumber(instanceNumber);
	}

	/**
	 * Update maximum depth number.
	 */
	private void updateMaxDepthNumber() {
		type.editMaxDepth(maxDepthNumber);
	}

	/**
	 * Update maximum instance number.
	 */
	private void updateMaxInstanceNumber() {
		type.editMaxInstanceNumber(maxInstanceNumber);
	}

	/**
	 * Update minimum depth number.
	 */
	private void updateMinDepthNumber() {
		type.editMinDepth(minDepthNumber);
	}

	/**
	 * Update minimum instance number.
	 */
	private void updateMinInstanceNumber() {
		type.editMinInstanceNumber(minInstanceNumber);
	}

}
