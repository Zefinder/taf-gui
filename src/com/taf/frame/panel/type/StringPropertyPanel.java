package com.taf.frame.panel.type;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;
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
			tableModel.addRow(new Object[] { "element", 1 });			
		});
		this.add(addElementButton, c);
		
		c.gridx = 1;
		removeElementButton = new JButton("- Remove Element");
		this.add(removeElementButton, c);
	}

}
