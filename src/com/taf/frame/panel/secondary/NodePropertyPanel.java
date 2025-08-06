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

import com.taf.event.EventMethod;
import com.taf.event.entity.NodeTypeChangedEvent;
import com.taf.frame.panel.secondary.type.TypeAddEntityPanel;
import com.taf.logic.type.NodeType;
import com.taf.util.Consts;

public class NodePropertyPanel extends EntitySecondaryPropertyPanel implements PropertyChangeListener {

	private static final long serialVersionUID = 293578359212546065L;

	private static final String INSTANCE_NUMBER_BUTTON_NAME = "Fixed instance number";
	private static final String MIN_MAX_INSTANCE_BUTTON_NAME = "Min-max instance number";
	private static final String DEPTH_NUMBER_BUTTON_NAME = "Fixed depth number";
	private static final String MIN_MAX_DEPTH_BUTTON_NAME = "Min-max depth number";
	private static final String INSTANCE_LABEL_TEXT = "Instance number";
	private static final String MIN_INSTANCE_LABEL_TEXT = "Min instance number";
	private static final String MAX_INSTANCE_LABEL_TEXT = "Max instance number";
	private static final String DEPTH_LABEL_TEXT = "Depth number";
	private static final String MIN_DEPTH_LABEL_TEXT = "Min depth number";
	private static final String MAX_DEPTH_LABEL_TEXT = "Max depth number";

	private static final int MAX_COLUMN_NUMBER = 4;

	private NodeType type;

	private JRadioButton depthButton;
	private JRadioButton minMaxDepthButton;

	private JFormattedTextField instanceField;
	private JFormattedTextField minInstanceField;
	private JFormattedTextField maxInstanceField;

	private JFormattedTextField depthField;
	private JFormattedTextField minDepthField;
	private JFormattedTextField maxDepthField;

	private int instanceNumber;
	private int minInstanceNumber;
	private int maxInstanceNumber;

	private int depthNumber;
	private int minDepthNumber;
	private int maxDepthNumber;

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

	private JPanel createFixedDepthPanel(boolean isRecusrive, boolean hasMinMax) {
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
		depthField.setEnabled(isRecusrive && !hasMinMax);
		depthField.addPropertyChangeListener(Consts.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY, this);
		depthField.setColumns(MAX_COLUMN_NUMBER);
		panel.add(depthField, c);

		return panel;
	}

	private JPanel createMinMaxDepthPanel(boolean isRecusrive, boolean hasMinMax) {
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
		minDepthField.setEnabled(isRecusrive && hasMinMax);
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
		maxDepthField.setEnabled(isRecusrive && hasMinMax);
		panel.add(maxDepthField, c);

		return panel;
	}

	private void activateFixedInstanceNumber(boolean activate) {
		instanceField.setEnabled(activate);
		minInstanceField.setEnabled(!activate);
		maxInstanceField.setEnabled(!activate);
		type.setMinMaxInstance(!activate);
	}

	private void activateFixedDepthNumber(boolean isRecursive, boolean activate) {
		depthField.setEnabled(isRecursive && activate);
		minDepthField.setEnabled(isRecursive && !activate);
		maxDepthField.setEnabled(isRecursive && !activate);
		type.setMinMaxDepth(!activate);
	}

	private void updateInstanceNumber() {
		type.editInstanceNumber(instanceNumber);
	}

	private void updateMinInstanceNumber() {
		type.editMinInstanceNumber(minInstanceNumber);
	}

	private void updateMaxInstanceNumber() {
		type.editMaxInstanceNumber(maxInstanceNumber);
	}

	private void updateDepthNumber() {
		type.editDepthNumber(depthNumber);
	}

	private void updateMinDepthNumber() {
		type.editMinDepth(minDepthNumber);
	}

	private void updateMaxDepthNumber() {
		type.editMaxDepth(maxDepthNumber);
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

	@EventMethod
	public void onNodeTypeChanged(NodeTypeChangedEvent event) {
		// Only the shown node can change type
		boolean isRecursive = type.isRecursiveNode();
		activateFixedDepthNumber(isRecursive, !type.hasMinMaxDepth());
		depthButton.setEnabled(isRecursive);
		minMaxDepthButton.setEnabled(isRecursive);
	}

}
