package com.taf.frame.panel;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class TafPanel extends JPanel {

	private static final long serialVersionUID = -7098545217788512796L;

	public TafPanel() {
		this.setLayout(new BorderLayout());
		FieldsPanel fieldsPanel = new FieldsPanel();
		PropertyPanel propertyPanel = new PropertyPanel();
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, fieldsPanel, propertyPanel);
		
		this.add(splitPane);
	}

}
