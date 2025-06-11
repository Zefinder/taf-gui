package com.taf.frame.panel.type;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.taf.logic.type.StringType;
import com.taf.manager.ConstantManager;
import com.taf.util.IntegerEditor;

public class StringPropertyPanel extends TypePropertyPanel {

	private static final long serialVersionUID = -6799435160247338364L;

	private static final String ELEMENT_NAME_COLUMN_NAME = "Element name";
	private static final String WEIGHT_COLUMN_NAME = "Weight";
	private static final int ELEMENT_NAME_COLUMN_INDEX = 0;
	private static final int WEIGHT_COLUMN_INDEX = 1;
	private static final String[] COLUMN_IDENTIFIERS = new String[] { ELEMENT_NAME_COLUMN_NAME, WEIGHT_COLUMN_NAME };

	private static final String ADD_ELEMENT_BUTTON_TEXT = "+ Add Element";
	private static final String REMOVE_ELEMENT_BUTTON_TEXT = "- Remove Element";

	private static final String INPUT_DIALOG_TITLE = "Element name";
	private static final String INPUT_DIALOG_MESSAGE = "Enter the elemnt's name";
	private static final String ERROR_MESSAGE = "Element name already exists";

	private JTable elementsTable;
	private JButton addElementButton;
	private JButton removeElementButton;

	public StringPropertyPanel(StringType type) {
		GridBagConstraints c = ConstantManager.getDefaultConstraint();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		// Create a table model with two columns, only the values column is editable
		DefaultTableModel tableModel = new DefaultTableModel() {
			private static final long serialVersionUID = -5844213424498676249L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == WEIGHT_COLUMN_INDEX;
			}
		};
		tableModel.setColumnCount(COLUMN_IDENTIFIERS.length);
		tableModel.setColumnIdentifiers(COLUMN_IDENTIFIERS);
		tableModel.addTableModelListener(e -> {
			if (e.getType() == TableModelEvent.UPDATE) {
				int row = e.getFirstRow();
				int column = e.getColumn();
				String elementValue = (String) tableModel.getValueAt(row, ELEMENT_NAME_COLUMN_INDEX);
				Integer elementWeight = (Integer) tableModel.getValueAt(row, WEIGHT_COLUMN_INDEX);

				// Only for weight column
				if (column == WEIGHT_COLUMN_INDEX) {
					type.setWeight(elementValue, elementWeight);
				}
			}
		});

		// Add elements to the model
		for (Entry<String, Integer> entry : type.getValues()) {
			tableModel.addRow(new Object[] { entry.getKey(), entry.getValue() });
		}

		// Create table
		elementsTable = new JTable(tableModel);
		elementsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Set column 1's editor as integer editor
		TableColumn weightColumn = elementsTable.getColumnModel().getColumn(WEIGHT_COLUMN_INDEX);
		weightColumn.setCellEditor(new IntegerEditor());

		JScrollPane scrollPane = new JScrollPane(elementsTable);
		this.add(scrollPane, c);

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
		addElementButton = new JButton(ADD_ELEMENT_BUTTON_TEXT);
		addElementButton.addActionListener(e -> {
			// Get name input
			String elementName = JOptionPane.showInputDialog(null, INPUT_DIALOG_MESSAGE, INPUT_DIALOG_TITLE,
					JOptionPane.INFORMATION_MESSAGE);

			// Add row to type, check if could be added (not same name)
			if (type.addValue(elementName)) {
				tableModel.addRow(new Object[] { elementName, ConstantManager.DEFAULT_WEIGHT_VALUE });
			} else {
				ConstantManager.showError(ERROR_MESSAGE);
			}

		});
		this.add(addElementButton, c);

		c.gridx = 1;
		removeElementButton = new JButton(REMOVE_ELEMENT_BUTTON_TEXT);
		removeElementButton.addActionListener(e -> {
			int selection = elementsTable.getSelectedRow();
			if (selection != -1) {
				String elementName = (String) tableModel.getValueAt(selection, ELEMENT_NAME_COLUMN_INDEX);
				type.removeValue(elementName);
				tableModel.removeRow(selection);
			}
		});
		this.add(removeElementButton, c);
	}

}
