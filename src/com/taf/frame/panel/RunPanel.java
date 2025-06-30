package com.taf.frame.panel;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.taf.frame.panel.run.TafConsolePanel;
import com.taf.frame.panel.run.TafRunComponentPanel;
import com.taf.manager.EventManager;

public class RunPanel extends JPanel {

	private static final long serialVersionUID = -1299815982147468944L;

	private TafConsolePanel consolePanel;
	
	public RunPanel() {
		this.setLayout(new BorderLayout());
		TafRunComponentPanel componentPanel = new TafRunComponentPanel();
		consolePanel = new TafConsolePanel();
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, componentPanel, consolePanel);

		this.add(splitPane);
	}

	public void unregisterConsolePanel() {
		EventManager.getInstance().unregisterEventListener(consolePanel);
	}

}
