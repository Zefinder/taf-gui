package com.taf.frame.popup;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.taf.event.ProjectToDeleteEvent;
import com.taf.event.ProjectToImportEvent;
import com.taf.event.ProjectToOpenEvent;
import com.taf.manager.EventManager;

public class ProjectPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 3680615409452198572L;

	private static final String IMPORT_ITEM_TEXT = "Import project";
	private static final String OPEN_ITEM_TEXT = "Open project";
	private static final String DELETE_ITEM_TEXT = "Delete project";

	public ProjectPopupMenu() {
		JMenuItem importItem = new JMenuItem(IMPORT_ITEM_TEXT);
		JMenuItem openItem = new JMenuItem(OPEN_ITEM_TEXT);
		JMenuItem deleteItem = new JMenuItem(DELETE_ITEM_TEXT);

		importItem.addActionListener(e -> importProject());
		openItem.addActionListener(e -> open());
		deleteItem.addActionListener(e -> delete());

		this.add(importItem);
		this.add(openItem);
		this.add(deleteItem);
	}

	private void importProject() {
		EventManager.getInstance().fireEvent(new ProjectToImportEvent());
	}

	private void open() {
		EventManager.getInstance().fireEvent(new ProjectToOpenEvent());
	}

	private void delete() {
		EventManager.getInstance().fireEvent(new ProjectToDeleteEvent());
	}

}
