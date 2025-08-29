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
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.taf.annotation.FactoryObject;
import com.taf.logic.constraint.Constraint;
import com.taf.logic.constraint.parameter.QuantifierType;
import com.taf.logic.constraint.parameter.RangesConstraintParameter.Range;
import com.taf.util.Consts;
import com.taf.util.EnumEditor;

/**
 * The ConstraintPropertyPanel is a secondary property panel used for
 * {@link Constraint}s. It allows the user to add expressions and quantifiers to
 * the constraint.
 *
 * @author Adrien Jakubiak
 */
@FactoryObject(types = "com.taf.logic.constraint.Constraint", generate = false)
public class ConstraintPropertyPanel extends EntitySecondaryPropertyPanel {

	private static final long serialVersionUID = 7936903848228324275L;

	/** The expressions column name. */
	private static final String EXPRESSIONS_COLUMN_NAME = "Expressions";

	/** The expressions column index. */
	private static final int EXPRESSIONS_COLUMN_INDEX = 0;

	/** The expressions column identifiers. */
	private static final String[] EXPRESSIONS_COLUMN_IDENTIFIERS = new String[] { EXPRESSIONS_COLUMN_NAME };

	/** The default expression. */
	private static final String DEFAULT_EXPRESSION = "";

	/** The quantifiers column name. */
	private static final String QUANTIFIERS_COLUMN_NAME = "Quantifiers";

	/** The quantifiers column index. */
	private static final int QUANTIFIERS_COLUMN_INDEX = 0;

	/** The left range column name. */
	private static final String LEFT_RANGE_COLUMN_NAME = "Lower bound";

	/** The left range column index. */
	private static final int LEFT_RANGE_COLUMN_INDEX = 1;

	/** The right range column name. */
	private static final String RIGHT_RANGE_COLUMN_NAME = "Upper bound";

	/** The right range column index. */
	private static final int RIGHT_RANGE_COLUMN_INDEX = 2;

	/** The type column name. */
	private static final String TYPE_COLUMN_NAME = "Type";

	/** The type column index. */
	private static final int TYPE_COLUMN_INDEX = 3;

	/** The quantifiers column identifiers. */
	private static final String[] QUANTIFIERS_COLUMN_IDENTIFIERS = new String[] { QUANTIFIERS_COLUMN_NAME,
			LEFT_RANGE_COLUMN_NAME, RIGHT_RANGE_COLUMN_NAME, TYPE_COLUMN_NAME };

	/** The default quantifier. */
	private static final String DEFAULT_QUANTIFIER = "";

	/** The default left range. */
	private static final String DEFAULT_LEFT_RANGE = "";

	/** The default right range. */
	private static final String DEFAULT_RIGHT_RANGE = "";

	/** The default type. */
	private static final QuantifierType DEFAULT_TYPE = QuantifierType.FORALL;

	/** The add expression button text. */
	private static final String ADD_EXPRESSION_BUTTON_TEXT = "+ Add Expression";

	/** The remove expression button text. */
	private static final String REMOVE_EXPRESSION_BUTTON_TEXT = "- Remove Expression";

	/** The add quantifier button text. */
	private static final String ADD_QUANTIFIER_BUTTON_TEXT = "+ Add Quantifier";

	/** The remove quantifier button text. */
	private static final String REMOVE_QUANTIFIER_BUTTON_TEXT = "- Remove Quantifier";

	/** The expression table. */
	private JTable expressionTable;

	/** The quantifiers table. */
	private JTable quantifiersTable;

	/** The add element button. */
	private JButton addElementButton;

	/** The remove element button. */
	private JButton removeElementButton;

	/** The add quantifier button. */
	private JButton addQuantifierButton;

	/** The remove quantifier button. */
	private JButton removeQuantifierButton;

	/**
	 * Instantiates a new constraint secondary property panel.
	 *
	 * @param constraint the constraint
	 */
	public ConstraintPropertyPanel(Constraint constraint) {
		GridBagConstraints c = Consts.getDefaultConstraint();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		this.add(createExpressionTablePanel(constraint), c);

		c.insets = new Insets(Consts.LARGE_INSET_GAP, 0, 0, 0);
		c.gridy = 1;
		this.add(createQuantifiersTablePanel(constraint), c);
	}

	/**
	 * Creates the expression table panel.
	 *
	 * @param constraint the constraint
	 * @return the j panel
	 */
	private JPanel createExpressionTablePanel(Constraint constraint) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints c = Consts.getDefaultConstraint();
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
		c.insets = new Insets(Consts.MEDIUM_INSET_GAP, Consts.SMALL_INSET_GAP, 0, Consts.SMALL_INSET_GAP);
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

	/**
	 * Creates the quantifiers table panel.
	 *
	 * @param constraint the constraint
	 * @return the j panel
	 */
	private JPanel createQuantifiersTablePanel(Constraint constraint) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints c = Consts.getDefaultConstraint();
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

		List<String> quantifiers = constraint.getQuantifiers();
		List<Range> ranges = constraint.getRanges();
		List<QuantifierType> types = constraint.getTypes();
		for (int i = 0; i < quantifiers.size(); i++) {
			String quantifier = quantifiers.get(i);
			Range range = ranges.get(i);
			QuantifierType type = types.get(i);
			quantifiersTableModel
					.addRow(new Object[] { quantifier, range.getLowerBound(), range.getUpperBound(), type });
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
		c.insets = new Insets(Consts.MEDIUM_INSET_GAP, Consts.SMALL_INSET_GAP, 0, Consts.SMALL_INSET_GAP);
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
