package com.taf.frame.panel;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.taf.frame.panel.run.TafConsolePanel;
import com.taf.frame.panel.run.TafRunComponentPanel;

public class RunPanel extends JPanel {

	private static final long serialVersionUID = -1299815982147468944L;

	public RunPanel() {
		this.setLayout(new BorderLayout());
		TafRunComponentPanel componentPanel = new TafRunComponentPanel();
		TafConsolePanel consolePanel = new TafConsolePanel();
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, componentPanel, consolePanel);
		
		this.add(splitPane);
	}

}
