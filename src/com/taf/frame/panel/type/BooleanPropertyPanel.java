package com.taf.frame.panel.type;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.taf.logic.type.BooleanType;
import com.taf.manager.ConstantManager;

public class BooleanPropertyPanel extends EntitySecondaryPropertyPanel {

	private static final long serialVersionUID = -9212722796480771296L;

	private static final String FALSE_WEIGHT_LABEL_TEXT = "False weight";
	private static final String TRUE_WEIGHT_LABEL_TEXT = "True weight";

	private JLabel falseWeightLabel;
	private JFormattedTextField falseWeightField;

	private JLabel trueWeightLabel;
	private JFormattedTextField trueWeightField;

	public BooleanPropertyPanel(BooleanType type) {
		GridBagConstraints c = ConstantManager.getDefaultConstraint();
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(0, 0, ConstantManager.SMALL_INSET_GAP, ConstantManager.SMALL_INSET_GAP);
		c.weightx = 0;
		c.weighty = 0;
		falseWeightLabel = new JLabel(FALSE_WEIGHT_LABEL_TEXT);
		addComponent(falseWeightLabel, c);

		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, ConstantManager.SMALL_INSET_GAP, ConstantManager.SMALL_INSET_GAP, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		falseWeightField = new JFormattedTextField(type.getFalseWeight());
		falseWeightField.setColumns(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		falseWeightField.addPropertyChangeListener(ConstantManager.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY,
				evt -> type.editTrueWeight(((Number) falseWeightField.getValue()).intValue()));
		addComponent(falseWeightField, c);

		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(ConstantManager.SMALL_INSET_GAP, 0, 0, ConstantManager.SMALL_INSET_GAP);
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		trueWeightLabel = new JLabel(TRUE_WEIGHT_LABEL_TEXT);
		addComponent(trueWeightLabel, c);

		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(ConstantManager.SMALL_INSET_GAP, ConstantManager.SMALL_INSET_GAP, 0, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		trueWeightField = new JFormattedTextField(type.getTrueWeight());
		trueWeightField.setColumns(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		trueWeightField.addPropertyChangeListener(ConstantManager.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY,
				evt -> type.editTrueWeight(((Number) trueWeightField.getValue()).intValue()));
		addComponent(trueWeightField, c);
		
		// Add an empty panel to force everything to the top
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 0, 0, 0);
		c.weighty = 1;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 2;
		addComponent(new JPanel(), c);
	}

}
