/*
 * Copyright or © or Copr.
 * 
 * This software is a computer program whose purpose is to generate random test
 * case from a template file describing the data model.
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software. You can use, modify
 * and/or redistribute the software under the terms of the CeCILL-B license as
 * circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * 
 * As a counterpart to the access to the source code and rights to copy, modify
 * and redistribute granted by the license, users are provided only with a
 * limited warranty and the software's author, the holder of the economic
 * rights, and the successive licensors have only limited liability.
 * 
 * In this respect, the user's attention is drawn to the risks associated with
 * loading, using, modifying and/or developing or reproducing the software by
 * the user in light of its specific status of free software, that may mean that
 * it is complicated to manipulate, and that also therefore means that it is
 * reserved for developers and experienced professionals having in-depth
 * computer knowledge. Users are therefore encouraged to load and test the
 * software's suitability as regards their requirements in conditions enabling
 * the security of their systems and/or data to be ensured and, more generally,
 * to use and operate it in the same conditions as regards security.
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package com.taf.frame.menubar;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.taf.event.ProjectToDeleteEvent;
import com.taf.event.ProjectToImportEvent;
import com.taf.event.ProjectToOpenEvent;
import com.taf.frame.MainMenuFrame;
import com.taf.frame.dialog.SettingsDialog;
import com.taf.manager.EventManager;

/**
 * The MainMenuBar is set to the {@link MainMenuFrame} and allows basic actions
 * such as importing, opening, or deleting a project, and setting TAF's path.
 *
 * @see JMenuBar
 * @see MainMenuFrame
 *
 * @author Adrien Jakubiak
 */
public class MainMenuBar extends JMenuBar {

	private static final long serialVersionUID = 6507562944379900941L;

	/** The project menu text. */
	private static final String PROJECT_MENU_TEXT = "Project";

	/** The import item text. */
	private static final String IMPORT_ITEM_TEXT = "Import project";

	/** The open item text. */
	private static final String OPEN_ITEM_TEXT = "Open project";

	/** The delete item text. */
	private static final String DELETE_ITEM_TEXT = "Delete project";

	/** The settings menu text. */
	private static final String SETTINGS_MENU_TEXT = "Settings";

	/** The path item text. */
	private static final String PATH_ITEM_TEXT = "Path settings";

	/**
	 * Instantiates a new main menu bar.
	 */
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

		pathItem.addActionListener(e -> new SettingsDialog().initDialog());

		projectMenu.add(importItem);
		projectMenu.add(openItem);
		projectMenu.add(deleteItem);

		settingsMenu.add(pathItem);

		this.add(projectMenu);
		this.add(settingsMenu);
	}

	/**
	 * Fires an event to notify that the user wants to import a project.
	 */
	private void importProject() {
		EventManager.getInstance().fireEvent(new ProjectToImportEvent());
	}

	/**
	 * Fires an event to notify that the user wants to open a project.
	 */
	private void openProject() {
		EventManager.getInstance().fireEvent(new ProjectToOpenEvent());
	}

	/**
	 * Fires an event to notify that the user wants to delete a project.
	 */
	private void deleteProject() {
		EventManager.getInstance().fireEvent(new ProjectToDeleteEvent());
	}

}
