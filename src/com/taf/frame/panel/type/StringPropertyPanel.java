package com.taf.frame.panel.type;

import java.awt.GridBagConstraints;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.taf.logic.type.StringType;

public class StringPropertyPanel extends TypePropertyPanel {

	private static final long serialVersionUID = -6799435160247338364L;

	private StringType type;
	
	private JTable elementsTable;

	public StringPropertyPanel(StringType type) {
		this.type = type;
		
		this.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 20));
		
		GridBagConstraints c = getDefaultConstraint();
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnCount(2);
		tableModel.setColumnIdentifiers(new String[] {"Element name", "Weight"});
		tableModel.addRow(new String[] {"Element", "1"});
		elementsTable = new JTable(tableModel);
		
		JScrollPane scrollPane = new JScrollPane(elementsTable);
		addComponent(scrollPane, c);
	}

}
