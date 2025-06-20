package com.taf.frame.menubar;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.taf.event.ProjectToDeleteEvent;
import com.taf.event.ProjectToImportEvent;
import com.taf.event.ProjectToOpenEvent;
import com.taf.manager.EventManager;

public class MainMenuBar extends JMenuBar {
	
	private static final long serialVersionUID = 6507562944379900941L;
	
	private static final String PROJECT_MENU_TEXT = "Project";
	private static final String IMPORT_ITEM_TEXT = "Import project";
	private static final String OPEN_ITEM_TEXT = "Open project";
	private static final String DELETE_ITEM_TEXT = "Delete project";
	private static final String SETTINGS_MENU_TEXT = "Settings";
	private static final String PATH_ITEM_TEXT = "Path settings";


	public MainMenuBar() {
		JMenu projectMenu = new JMenu(PROJECT_MENU_TEXT);
		JMenuItem importItem = new JMenuItem(IMPORT_ITEM_TEXT);
		JMenuItem openItem = new JMenuItem(OPEN_ITEM_TEXT);
		JMenuItem deleteItem = new JMenuItem(DELETE_ITEM_TEXT);

		JMenu settingsMenu = new JMenu(SETTINGS_MENU_TEXT);
		JMenuItem pathItem = new JMenuItem(PATH_ITEM_TEXT);
		
		importItem.addActionListener(e -> importProject());
		openItem.addActionListener(e -> openProject());
		deleteItem.addActionListener(e -> deleteProject());
		
		projectMenu.add(importItem);
		projectMenu.add(openItem);
		projectMenu.add(deleteItem);
		
		settingsMenu.add(pathItem);
		
		this.add(projectMenu);
		this.add(settingsMenu);
	}
	
	private void importProject() {
		EventManager.getInstance().fireEvent(new ProjectToImportEvent());
	}
	
	private void openProject() {
		EventManager.getInstance().fireEvent(new ProjectToOpenEvent());
	}
	
	private void deleteProject() {
		EventManager.getInstance().fireEvent(new ProjectToDeleteEvent());
	}

}
