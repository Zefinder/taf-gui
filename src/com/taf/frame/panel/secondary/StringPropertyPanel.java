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

import com.taf.annotation.FactoryObject;
import com.taf.logic.type.FieldType;
import com.taf.logic.type.StringType;
import com.taf.util.Consts;
import com.taf.util.IntegerEditor;

/**
 * The StringPropertyPanel is a secondary property panel used for
 * {@link StringType}. It allows the user to add and remove string values and
 * set their weights.
 * 
 * @see EntitySecondaryPropertyPanel
 *
 * @author Adrien Jakubiak
 */
@FactoryObject(types = FieldType.class, generate = true)
public class StringPropertyPanel extends EntitySecondaryPropertyPanel {

	private static final long serialVersionUID = -6799435160247338364L;

	private static final String ERROR_MESSAGE = "Element name already exists";

	/** The element's name column name. */
	private static final String ELEMENT_NAME_COLUMN_NAME = "Element name";

	/** The weight column name. */
	private static final String WEIGHT_COLUMN_NAME = "Weight";

	/** The element's name column index. */
	private static final int ELEMENT_NAME_COLUMN_INDEX = 0;

	/** The weight column index. */
	private static final int WEIGHT_COLUMN_INDEX = 1;

	/** The column identifiers. */
	private static final String[] COLUMN_IDENTIFIERS = new String[] { ELEMENT_NAME_COLUMN_NAME, WEIGHT_COLUMN_NAME };

	/** The add element button text. */
	private static final String ADD_ELEMENT_BUTTON_TEXT = "+ Add Element";

	/** The remove element button text. */
	private static final String REMOVE_ELEMENT_BUTTON_TEXT = "- Remove Element";

	/** The input dialog title. */
	private static final String INPUT_DIALOG_TITLE = "Element name";

	/** The input dialog message. */
	private static final String INPUT_DIALOG_MESSAGE = "Enter the elemnt's name";

	/** The elements table. */
	private JTable elementsTable;

	/** The add element button. */
	private JButton addElementButton;

	/** The remove element button. */
	private JButton removeElementButton;

	/**
	 * Instantiates a new string property panel.
	 *
	 * @param type the type
	 */
	public StringPropertyPanel(StringType type) {
		GridBagConstraints c = Consts.getDefaultConstraint();
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
		c.insets = new Insets(Consts.MEDIUM_INSET_GAP, Consts.SMALL_INSET_GAP, 0, Consts.SMALL_INSET_GAP);
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
				tableModel.addRow(new Object[] { elementName, Consts.DEFAULT_WEIGHT_VALUE });
			} else {
				Consts.showError(ERROR_MESSAGE);
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
