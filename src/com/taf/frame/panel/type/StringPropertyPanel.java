package com.taf.frame.panel.type;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.taf.logic.type.StringType;
import com.taf.manager.ConstantManager;
import com.taf.util.IntegerEditor;

public class StringPropertyPanel extends TypePropertyPanel {

	private static final long serialVersionUID = -6799435160247338364L;

	private StringType type;

	private JTable elementsTable;
	private JButton addElementButton;
	private JButton removeElementButton;

	public StringPropertyPanel(StringType type) {
		this.type = type;

		GridBagConstraints c = ConstantManager.getDefaultConstraint();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		// Create a table model with two columns
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnCount(2);
		tableModel.setColumnIdentifiers(new String[] { "Element name", "Weight" });

		// Create table and set column 1's editor as integer editor
		elementsTable = new JTable(tableModel);
		TableColumn nameColumn = elementsTable.getColumnModel().getColumn(0);
//		nameColumn.setMaxWidth(100);
		// TODO Set preferred size as 20 rows and 2 columns or borderlayout center

		TableColumn weightColumn = elementsTable.getColumnModel().getColumn(1);
		weightColumn.setCellEditor(new IntegerEditor());
//		weightColumn.setMaxWidth(100);

//		tableModel.addRow(new Object[] { "a", 1 });
//		tableModel.addRow(new Object[] { "a", 1 });
//		tableModel.addRow(new Object[] { "a", 1 });

		JScrollPane scrollPane = new JScrollPane(elementsTable);
		System.out.println(elementsTable.getPreferredSize());
		System.out.println(elementsTable.getPreferredScrollableViewportSize());
		elementsTable.setPreferredScrollableViewportSize(new Dimension(150, 200));
		addComponent(scrollPane, c);

		c.insets = new Insets(5, 5, 0, 5);
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridy = 1;
		addElementButton = new JButton("+ Add Element");
		addElementButton.addActionListener(e -> {
			tableModel.addRow(new Object[] { "test", 2 });
			System.out.println(elementsTable.getPreferredSize());
			System.out.println(elementsTable.getPreferredScrollableViewportSize());
		});
		addComponent(addElementButton, c);

		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		removeElementButton = new JButton("- Remove Element");
		addComponent(removeElementButton, c);
	}

}
