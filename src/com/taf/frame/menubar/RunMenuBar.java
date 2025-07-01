package com.taf.frame.menubar;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.taf.frame.dialog.SettingsDialog;

public class RunMenuBar extends JMenuBar {

	private static final long serialVersionUID = 5961856479125309084L;

	private static final String PROJECT_MENU_TEXT = "Project";
	private static final String RETURN_ITEM_TEXT = "Return to project";
	private static final String SETTINGS_MENU_TEXT = "Settings";
	private static final String PATH_ITEM_TEXT = "Path settings";

	public RunMenuBar() {
		JMenu projectMenu = new JMenu(PROJECT_MENU_TEXT);
		JMenuItem returnItem = new JMenuItem(RETURN_ITEM_TEXT);
		
		JMenu settingsMenu = new JMenu(SETTINGS_MENU_TEXT);
		JMenuItem pathItem = new JMenuItem(PATH_ITEM_TEXT);

		returnItem.addActionListener(e -> {});
		pathItem.addActionListener(e -> new SettingsDialog().initDialog());

		projectMenu.add(returnItem);
		settingsMenu.add(pathItem);

		this.add(projectMenu);
		this.add(settingsMenu);
	}

}
