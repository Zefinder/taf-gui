package com.taf.frame.panel.type;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import com.taf.logic.constraint.Constraint;
import com.taf.manager.ConstantManager;

public class ConstraintSecondaryPropertyPanel extends EntitySecondaryPropertyPanel {

	private static final long serialVersionUID = 7936903848228324275L;

	private static final String EXPRESSIONS_COLUMN_NAME = "Expressions";
//	private static final String WEIGHT_COLUMN_NAME = "Weight";
	private static final int EXPRESSIONS_COLUMN_INDEX = 0;
//	private static final int WEIGHT_COLUMN_INDEX = 1;
	private static final String[] COLUMN_IDENTIFIERS = new String[] { EXPRESSIONS_COLUMN_NAME };

	private static final String ADD_EXPRESSION_BUTTON_TEXT = "+ Add Expression";
	private static final String REMOVE_EXPRESSION_BUTTON_TEXT = "- Remove Expression";;
	private static final String ERROR_MESSAGE = "Element name already exists";

	private JTable expressionTable;
	private JButton addElementButton;
	private JButton removeElementButton;

	// TODO Create a method for the expression table and one method for the
	// quantifier table!
	public ConstraintSecondaryPropertyPanel(Constraint constraint) {
		GridBagConstraints c = ConstantManager.getDefaultConstraint();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		this.add(createExpressionTablePanel(constraint), c);
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
		expressionTableModel.setColumnCount(COLUMN_IDENTIFIERS.length);
		expressionTableModel.setColumnIdentifiers(COLUMN_IDENTIFIERS);
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
			// TODO Constant empty string
			constraint.addExpression("");
			expressionTableModel.addRow(new String[] { "" });
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
}
