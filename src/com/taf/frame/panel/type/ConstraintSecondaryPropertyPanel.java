package com.taf.frame.panel.type;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.taf.logic.constraint.Constraint;
import com.taf.logic.constraint.parameter.QuantifierType;
import com.taf.logic.constraint.parameter.RangesConstraintParameter.Range;
import com.taf.manager.ConstantManager;
import com.taf.util.EnumEditor;

public class ConstraintSecondaryPropertyPanel extends EntitySecondaryPropertyPanel {

	private static final long serialVersionUID = 7936903848228324275L;

	private static final String EXPRESSIONS_COLUMN_NAME = "Expressions";
	private static final int EXPRESSIONS_COLUMN_INDEX = 0;
	private static final String[] EXPRESSIONS_COLUMN_IDENTIFIERS = new String[] { EXPRESSIONS_COLUMN_NAME };
	private static final String DEFAULT_EXPRESSION = "";

	private static final String QUANTIFIERS_COLUMN_NAME = "Quantifiers";
	private static final int QUANTIFIERS_COLUMN_INDEX = 0;
	private static final String LEFT_RANGE_COLUMN_NAME = "Lower bound";
	private static final int LEFT_RANGE_COLUMN_INDEX = 1;
	private static final String RIGHT_RANGE_COLUMN_NAME = "Upper bound";
	private static final int RIGHT_RANGE_COLUMN_INDEX = 2;
	private static final String TYPE_COLUMN_NAME = "Type";
	private static final int TYPE_COLUMN_INDEX = 3;
	private static final String[] QUANTIFIERS_COLUMN_IDENTIFIERS = new String[] { QUANTIFIERS_COLUMN_NAME,
			LEFT_RANGE_COLUMN_NAME, RIGHT_RANGE_COLUMN_NAME, TYPE_COLUMN_NAME };
	private static final String DEFAULT_QUANTIFIER = "";
	private static final String DEFAULT_LEFT_RANGE = "";
	private static final String DEFAULT_RIGHT_RANGE = "";
	private static final QuantifierType DEFAULT_TYPE = QuantifierType.FORALL;

	private static final String ADD_EXPRESSION_BUTTON_TEXT = "+ Add Expression";
	private static final String REMOVE_EXPRESSION_BUTTON_TEXT = "- Remove Expression";

	private static final String ADD_QUANTIFIER_BUTTON_TEXT = "+ Add Quantifier";
	private static final String REMOVE_QUANTIFIER_BUTTON_TEXT = "- Remove Quantifier";

	private JTable expressionTable;
	private JTable quantifiersTable;

	private JButton addElementButton;
	private JButton removeElementButton;

	private JButton addQuantifierButton;
	private JButton removeQuantifierButton;

	public ConstraintSecondaryPropertyPanel(Constraint constraint) {
		GridBagConstraints c = ConstantManager.getDefaultConstraint();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		this.add(createExpressionTablePanel(constraint), c);

		c.insets = new Insets(ConstantManager.LARGE_INSET_GAP, 0, 0, 0);
		c.gridy = 1;
		this.add(createQuantifiersTablePanel(constraint), c);
	}

	private JPanel createExpressionTablePanel(Constraint constraint) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints c = ConstantManager.getDefaultConstraint();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		// Create a table model with two columns, only the values column is editable
		DefaultTableModel expressionTableModel = new DefaultTableModel();
		expressionTableModel.setColumnCount(EXPRESSIONS_COLUMN_IDENTIFIERS.length);
		expressionTableModel.setColumnIdentifiers(EXPRESSIONS_COLUMN_IDENTIFIERS);
		expressionTableModel.addTableModelListener(e -> {
			if (e.getType() == TableModelEvent.UPDATE) {
				int row = e.getFirstRow();
				String expression = (String) expressionTableModel.getValueAt(row, EXPRESSIONS_COLUMN_INDEX);
				constraint.editExpression(row, expression);
			}
		});

		// Add elements to the model
		for (String expression : constraint.getExpressions()) {
			expressionTableModel.addRow(new String[] { expression });
		}

		// Create table
		expressionTable = new JTable(expressionTableModel);
		expressionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(expressionTable);
		panel.add(scrollPane, c);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(ConstantManager.MEDIUM_INSET_GAP, ConstantManager.SMALL_INSET_GAP, 0,
				ConstantManager.SMALL_INSET_GAP);
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		addElementButton = new JButton(ADD_EXPRESSION_BUTTON_TEXT);
		addElementButton.addActionListener(e -> {
			constraint.addExpression(DEFAULT_EXPRESSION);
			expressionTableModel.addRow(new String[] { DEFAULT_EXPRESSION });
		});
		panel.add(addElementButton, c);

		c.gridx = 1;
		removeElementButton = new JButton(REMOVE_EXPRESSION_BUTTON_TEXT);
		removeElementButton.addActionListener(e -> {
			int selection = expressionTable.getSelectedRow();
			if (selection != -1) {
				constraint.removeExpression(selection);
				expressionTableModel.removeRow(selection);
			}
		});
		panel.add(removeElementButton, c);

		return panel;
	}

	private JPanel createQuantifiersTablePanel(Constraint constraint) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints c = ConstantManager.getDefaultConstraint();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		// Create a table model with two columns, only the values column is editable
		DefaultTableModel quantifiersTableModel = new DefaultTableModel();
		quantifiersTableModel.setColumnCount(QUANTIFIERS_COLUMN_IDENTIFIERS.length);
		quantifiersTableModel.setColumnIdentifiers(QUANTIFIERS_COLUMN_IDENTIFIERS);
		quantifiersTableModel.addTableModelListener(e -> {
			if (e.getType() == TableModelEvent.UPDATE) {
				int row = e.getFirstRow();
				int column = e.getColumn();
				Object value = quantifiersTableModel.getValueAt(row, column);

				switch (column) {
				case QUANTIFIERS_COLUMN_INDEX:
					constraint.editQuantifier(row, (String) value);
					break;

				case LEFT_RANGE_COLUMN_INDEX:
					constraint.editLeftRange(row, (String) value);
					break;

				case RIGHT_RANGE_COLUMN_INDEX:
					constraint.editRightRange(row, (String) value);
					break;

				case TYPE_COLUMN_INDEX:
					constraint.editQuantifierType(row, (QuantifierType) value);
					break;
				}
			}
		});

		// TODO Add elements to the model

		List<String> quantifiers = constraint.getQuantifiers();
		List<Range> ranges = constraint.getRanges();
		List<QuantifierType> types = constraint.getTypes();
		for (int i = 0; i < quantifiers.size(); i++) {
			String quantifier = quantifiers.get(i);
			Range range = ranges.get(i);
			QuantifierType type = types.get(i);
			quantifiersTableModel.addRow(new Object[] { quantifier, range.getLeft(), range.getRight(), type });
		}

		// Create table
		quantifiersTable = new JTable(quantifiersTableModel);
		quantifiersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		TableColumn typeColumn = quantifiersTable.getColumnModel().getColumn(TYPE_COLUMN_INDEX);
		typeColumn.setCellEditor(new EnumEditor<QuantifierType>(QuantifierType.values(), QuantifierType.FORALL));

		JScrollPane scrollPane = new JScrollPane(quantifiersTable);
		panel.add(scrollPane, c);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(ConstantManager.MEDIUM_INSET_GAP, ConstantManager.SMALL_INSET_GAP, 0,
				ConstantManager.SMALL_INSET_GAP);
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		addQuantifierButton = new JButton(ADD_QUANTIFIER_BUTTON_TEXT);
		addQuantifierButton.addActionListener(e -> {
			constraint.addQuantifier(DEFAULT_QUANTIFIER, DEFAULT_LEFT_RANGE, DEFAULT_RIGHT_RANGE, DEFAULT_TYPE);
			quantifiersTableModel
					.addRow(new Object[] { DEFAULT_QUANTIFIER, DEFAULT_LEFT_RANGE, DEFAULT_RIGHT_RANGE, DEFAULT_TYPE });
		});
		panel.add(addQuantifierButton, c);

		c.gridx = 1;
		removeQuantifierButton = new JButton(REMOVE_QUANTIFIER_BUTTON_TEXT);
		removeQuantifierButton.addActionListener(e -> {
			int selection = quantifiersTable.getSelectedRow();
			if (selection != -1) {
				constraint.removeQuantifier(selection);
				quantifiersTableModel.removeRow(selection);
			}
		});
		panel.add(removeQuantifierButton, c);

		return panel;
	}
}
