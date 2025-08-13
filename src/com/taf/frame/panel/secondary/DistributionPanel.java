package com.taf.frame.panel.secondary;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.taf.logic.type.NumericalType;
import com.taf.logic.type.RealType;
import com.taf.logic.type.parameter.RangesParameter.Range;
import com.taf.util.Consts;
import com.taf.util.DoubleEditor;
import com.taf.util.IntegerEditor;

public class DistributionPanel extends JPanel {

	private static final long serialVersionUID = 2022873000380007989L;

	private static final String BORDER_TEXT = "Distribution parameters";

	private static final String UNIFORM_CARD_NAME = "UNIFORM";
	private static final String NORMAL_CARD_NAME = "NORMAL";
	private static final String INTERVAL_CARD_NAME = "INTERVAL";

	private static final String MEAN_LABEL_TEXT = "Mean";
	private static final String VARIANCE_LABEL_TEXT = "Variance";
	private static final String MEAN_VARIANCE_BUTTON_TEXT = "Auto mean and variance";

	private static final String LOWER_BOUND_COLUMN_NAME = "Lower bound";
	private static final int LOWER_BOUND_COLUMN_INDEX = 0;
	private static final String UPPER_BOUND_COLUMN_NAME = "Upper bound";
	private static final int UPPER_BOUND_COLUMN_INDEX = 1;
	private static final String WEIGHT_COLUMN_NAME = "Weight";
	private static final int WEIGHT_COLUMN_INDEX = 2;
	private static final String[] QUANTIFIERS_COLUMN_IDENTIFIERS = new String[] { LOWER_BOUND_COLUMN_NAME,
			UPPER_BOUND_COLUMN_NAME, WEIGHT_COLUMN_NAME };
	private static final int DEFAULT_LOWER_BOUND = Consts.DEFAULT_MIN_VALUE;
	private static final int DEFAULT_UPPER_BOUND = Consts.DEFAULT_MAX_VALUE;
	private static final int DEFAULT_WEIGHT = Consts.DEFAULT_WEIGHT_VALUE;

	private static final String ADD_INTERVAL_BUTTON_TEXT = "+ Add Interval";
	private static final String REMOVE_INTERVAL_BUTTON_TEXT = "- Remove Interval";

	public DistributionPanel(NumericalType type) {
		this.setLayout(new CardLayout());

		this.add(buildUniformPanel(), UNIFORM_CARD_NAME);
		this.add(buildNormalPanel(type), NORMAL_CARD_NAME);
		this.add(buildIntervalPanel(type), INTERVAL_CARD_NAME);

		this.setBorder(BorderFactory.createTitledBorder(BORDER_TEXT));
	}

	private JPanel buildUniformPanel() {
		// Uniform has nothing to display
		JPanel panel = new JPanel();
		return panel;
	}

	private JPanel buildNormalPanel(NumericalType type) {
		double mean = type.getMean();
		double variance = type.getVariance();

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = Consts.getDefaultConstraint();
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.weighty = 0;
		c.insets = new Insets(Consts.SMALL_INSET_GAP, 0, 0, Consts.SMALL_INSET_GAP);
		JLabel meanLabel = new JLabel(MEAN_LABEL_TEXT);
		panel.add(meanLabel, c);

		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP, 0, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		JFormattedTextField meanField = new JFormattedTextField(DecimalFormat.getInstance(Locale.US));
		meanField.setValue(mean);
		meanField.setColumns(Consts.JTEXT_FIELD_DEFAULT_COLUMN);
		meanField.addPropertyChangeListener(Consts.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY,
				evt -> type.editMean(((Number) meanField.getValue()).doubleValue()));
		panel.add(meanField, c);

		c.anchor = GridBagConstraints.LINE_END;
		c.weighty = 0;
		c.insets = new Insets(Consts.SMALL_INSET_GAP, 0, 0, Consts.SMALL_INSET_GAP);
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		JLabel varianceLabel = new JLabel(VARIANCE_LABEL_TEXT);
		panel.add(varianceLabel, c);

		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP, 0, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		JFormattedTextField varianceField = new JFormattedTextField(DecimalFormat.getInstance(Locale.US));
		varianceField.setValue(variance);
		varianceField.setColumns(Consts.JTEXT_FIELD_DEFAULT_COLUMN);
		varianceField.addPropertyChangeListener(Consts.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY,
				evt -> type.editVariance(((Number) varianceField.getValue()).doubleValue()));
		panel.add(varianceField, c);
		
		c.anchor = GridBagConstraints.NORTH;
		c.weightx = 0;
		c.weighty = 1;
		c.insets = new Insets(Consts.MEDIUM_INSET_GAP, 0, 0, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 2;
		JButton autoMeanVariance = new JButton(MEAN_VARIANCE_BUTTON_TEXT);
		autoMeanVariance.addActionListener(e -> {
			double min = type.getMinNumber().doubleValue();
			double max = type.getMaxNumber().doubleValue();
			double autoMean = (max + min) / 2;
			double autoVariance = (max - min) / 4;
			meanField.setValue(autoMean);
			varianceField.setValue(autoVariance);
		});
		panel.add(autoMeanVariance, c);
		
		return panel;
	}

	private JPanel buildIntervalPanel(NumericalType type) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints c = Consts.getDefaultConstraint();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		// Create a table model with two columns, only the values column is editable
		DefaultTableModel intervalTableModel = new DefaultTableModel();
		intervalTableModel.setColumnCount(QUANTIFIERS_COLUMN_IDENTIFIERS.length);
		intervalTableModel.setColumnIdentifiers(QUANTIFIERS_COLUMN_IDENTIFIERS);
		intervalTableModel.addTableModelListener(e -> {
			if (e.getType() == TableModelEvent.UPDATE) {
				int row = e.getFirstRow();
				int column = e.getColumn();
				Number value = (Number) intervalTableModel.getValueAt(row, column);

				switch (column) {
				case LOWER_BOUND_COLUMN_INDEX:
					type.editLowerBound(row, value);
					break;

				case UPPER_BOUND_COLUMN_INDEX:
					type.editUpperBound(row, value);
					break;

				case WEIGHT_COLUMN_INDEX:
					type.editWeight(row, value.intValue());
					break;
				}
			}
		});

		List<Range> ranges = type.getRanges();
		int[] weights = type.getWeights();
		for (int i = 0; i < ranges.size(); i++) {
			Range range = ranges.get(i);
			int weight = weights[i];
			Object[] row;
			if (type instanceof RealType) {
				row = new Object[] { range.getLowerBound().doubleValue(), range.getUpperBound().doubleValue(), weight };
			} else {
				row = new Object[] { range.getLowerBound().longValue(), range.getUpperBound().longValue(), weight };
			}
			intervalTableModel.addRow(row);
		}

		// Create table
		JTable intervalTable = new JTable(intervalTableModel);
		intervalTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Lower and upper bounds are doubles for real values, integers otherwise
		TableColumn lowerBoundColumn = intervalTable.getColumnModel().getColumn(LOWER_BOUND_COLUMN_INDEX);
		TableColumn upperBoundColumn = intervalTable.getColumnModel().getColumn(UPPER_BOUND_COLUMN_INDEX);
		if (type instanceof RealType) {
			lowerBoundColumn.setCellEditor(new DoubleEditor());
			upperBoundColumn.setCellEditor(new DoubleEditor());
		} else {
			lowerBoundColumn.setCellEditor(new IntegerEditor());
			upperBoundColumn.setCellEditor(new IntegerEditor());
		}

		// Weights are integers
		TableColumn weightColumn = intervalTable.getColumnModel().getColumn(WEIGHT_COLUMN_INDEX);
		weightColumn.setCellEditor(new IntegerEditor());

		JScrollPane scrollPane = new JScrollPane(intervalTable);
		panel.add(scrollPane, c);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(Consts.MEDIUM_INSET_GAP, Consts.SMALL_INSET_GAP, 0,
				Consts.SMALL_INSET_GAP);
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		JButton addIntervalButton = new JButton(ADD_INTERVAL_BUTTON_TEXT);
		addIntervalButton.addActionListener(e -> {
			type.addInterval(DEFAULT_LOWER_BOUND, DEFAULT_UPPER_BOUND, DEFAULT_WEIGHT);
			intervalTableModel.addRow(new Object[] { DEFAULT_LOWER_BOUND, DEFAULT_UPPER_BOUND, DEFAULT_WEIGHT });
		});
		panel.add(addIntervalButton, c);

		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		JButton removeIntervalButton = new JButton(REMOVE_INTERVAL_BUTTON_TEXT);
		removeIntervalButton.addActionListener(e -> {
			int selection = intervalTable.getSelectedRow();
			if (selection != -1) {
				type.removeInterval(selection);
				intervalTableModel.removeRow(selection);
			}
		});
		panel.add(removeIntervalButton, c);

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
