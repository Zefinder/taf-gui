package com.taf.frame.panel.run;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class TafRunComponentPanel extends JPanel {

	private static final long serialVersionUID = -8474576169732949803L;

	public TafRunComponentPanel() {
		SettingsPanel settingsPanel  = new SettingsPanel();
		ActiveTreePanel treePanel = new ActiveTreePanel();
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, settingsPanel, treePanel);
		
		this.add(splitPane);
	}

}
