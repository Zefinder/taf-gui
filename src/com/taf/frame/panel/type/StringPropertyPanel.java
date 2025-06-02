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
				return column == 1;
			}
		};
		tableModel.setColumnCount(2);
		tableModel.setColumnIdentifiers(new String[] { "Element name", "Weight" });
		tableModel.addTableModelListener(e -> {
			if (e.getType() == TableModelEvent.UPDATE) {
				int row = e.getFirstRow();
				int column = e.getColumn();
				String elementValue = (String) tableModel.getValueAt(row, 0);
				Integer elementWeight = (Integer) tableModel.getValueAt(row, 1);

				// Only for weight column
				if (column == 1) {
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
		TableColumn weightColumn = elementsTable.getColumnModel().getColumn(1);
		weightColumn.setCellEditor(new IntegerEditor());

		JScrollPane scrollPane = new JScrollPane(elementsTable);
		this.add(scrollPane, c);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(10, 5, 0, 5);
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		addElementButton = new JButton("+ Add Element");
		addElementButton.addActionListener(e -> {
			// Get name input
			String elementName = JOptionPane.showInputDialog(null, "Enter the elemnt's name", "Element name",
					JOptionPane.INFORMATION_MESSAGE);

			// Add row to type, check if could be added (not same name)
			if (type.addValue(elementName)) {
				tableModel.addRow(new Object[] { elementName, 1 });
			} else {
				JOptionPane.showMessageDialog(null, "Element name already exists", "Error!", JOptionPane.ERROR_MESSAGE);
			}

		});
		this.add(addElementButton, c);

		c.gridx = 1;
		removeElementButton = new JButton("- Remove Element");
		removeElementButton.addActionListener(e -> {
			int selection = elementsTable.getSelectedRow();
			if (selection != -1) {
				String elementName = (String) tableModel.getValueAt(selection, 0);
				type.removeValue(elementName);
				tableModel.removeRow(selection);
			}
		});
		this.add(removeElementButton, c);
	}

}
