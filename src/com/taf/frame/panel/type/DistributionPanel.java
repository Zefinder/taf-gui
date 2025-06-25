package com.taf.frame.panel.type;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.taf.logic.type.IntegerType;
import com.taf.manager.ConstantManager;

public class DistributionPanel extends JPanel {

	private static final long serialVersionUID = 2022873000380007989L;

	private static final String BORDER_TEXT = "Distribution parameters";
	
	private static final String UNIFORM_CARD_NAME = "UNIFORM";
	private static final String NORMAL_CARD_NAME = "NORMAL";
	private static final String INTERVAL_CARD_NAME = "INTERVAL";

	private static final String MEAN_LABEL_TEXT = "Mean";
	private static final String VARIANCE_LABEL_TEXT = "Variance";

	public DistributionPanel(IntegerType type) {
		// TODO Create 3 panels (one per distribution) stored in this as a card layout.
		// Create methods to show the different cards
		this.setLayout(new CardLayout());

		this.add(buildUniformPanel(), UNIFORM_CARD_NAME);
		this.add(buildNormalPanel(type), NORMAL_CARD_NAME);
		this.add(buildIntervalPanel(), INTERVAL_CARD_NAME);
		
		this.setBorder(BorderFactory.createTitledBorder(BORDER_TEXT));
	}

	private JPanel buildUniformPanel() {
		// Uniform has nothing to display
		JPanel panel = new JPanel();
		return panel;
	}

	private JPanel buildNormalPanel(IntegerType type) {
		double mean = type.getMean();
		double variance = type.getVariance();

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = ConstantManager.getDefaultConstraint();
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.weighty = 0;
		c.insets = new Insets(ConstantManager.SMALL_INSET_GAP, 0, 0, ConstantManager.SMALL_INSET_GAP);
		JLabel meanLabel = new JLabel(MEAN_LABEL_TEXT);
		panel.add(meanLabel, c);

		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(ConstantManager.SMALL_INSET_GAP, ConstantManager.SMALL_INSET_GAP, 0, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		JFormattedTextField meanField = new JFormattedTextField(DecimalFormat.getInstance(Locale.US));
		meanField.setValue(mean);
		meanField.setColumns(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		meanField.addPropertyChangeListener(ConstantManager.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY,
				evt -> type.editMean(((Number) meanField.getValue()).doubleValue()));
		panel.add(meanField, c);
		
		c.anchor = GridBagConstraints.LINE_END;
		c.weighty = 1;
		c.insets = new Insets(ConstantManager.SMALL_INSET_GAP, 0, 0, ConstantManager.SMALL_INSET_GAP);
		c.gridwidth = 1;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 1;
		JLabel varianceLabel = new JLabel(VARIANCE_LABEL_TEXT);
		panel.add(varianceLabel, c);

		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(ConstantManager.SMALL_INSET_GAP, ConstantManager.SMALL_INSET_GAP, 0, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		JFormattedTextField varianceField = new JFormattedTextField(DecimalFormat.getInstance(Locale.US));
		varianceField.setValue(variance);
		varianceField.setColumns(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		varianceField.addPropertyChangeListener(ConstantManager.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY,
				evt -> type.editMean(((Number) varianceField.getValue()).doubleValue()));
		panel.add(varianceField, c);

		return panel;
	}

	private JPanel buildIntervalPanel() {
		JPanel panel = new JPanel();
		JLabel label = new JLabel("Prout");
		panel.add(label);
		return panel;
	}

	public void showUniformPanel() {
		CardLayout cl = (CardLayout) (this.getLayout());
		cl.show(this, UNIFORM_CARD_NAME);
	}

	public void showNormalPanel() {
		CardLayout cl = (CardLayout) (this.getLayout());
		cl.show(this, NORMAL_CARD_NAME);
	}

	public void showIntervalPanel() {
		CardLayout cl = (CardLayout) (this.getLayout());
		cl.show(this, INTERVAL_CARD_NAME);
	}

}
