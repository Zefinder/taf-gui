package com.taf.frame.popup;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.taf.event.ProjectToImportEvent;
import com.taf.manager.EventManager;

public class ProjectImportPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 565738512731617988L;
	private static final String IMPORT_ITEM_TEXT = "Import project";

	public ProjectImportPopupMenu() {
		JMenuItem importItem = new JMenuItem(IMPORT_ITEM_TEXT);
		importItem.addActionListener(e -> importProject());
		this.add(importItem);
	}

	private void importProject() {
		EventManager.getInstance().fireEvent(new ProjectToImportEvent());
	}
}
