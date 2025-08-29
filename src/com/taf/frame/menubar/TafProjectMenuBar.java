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

import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.taf.event.Event;
import com.taf.event.ProjectRunOpenedEvent;
import com.taf.frame.MainMenuFrame;
import com.taf.frame.ProjectFrame;
import com.taf.frame.RunFrame;
import com.taf.frame.dialog.SettingsDialog;
import com.taf.manager.EventManager;
import com.taf.manager.RunManager;
import com.taf.manager.SaveManager;
import com.taf.util.Consts;

/**
 * The MainMenuBar is set to the {@link ProjectFrame} and allows actions such as
 * saving, exporting, or closing a project, setting TAF's path, and running a
 * project.
 *
 * @see JMenuBar
 * @see ProjectFrame
 * @see RunFrame
 *
 * @author Adrien Jakubiak
 */
public class TafProjectMenuBar extends JMenuBar {

	private static final long serialVersionUID = -7423744825543652418L;

	/** The project menu text. */
	private static final String PROJECT_MENU_TEXT = "Project";
	
	/** The save item text. */
	private static final String SAVE_ITEM_TEXT = "Save project";

	/** The export item text. */
	private static final String EXPORT_ITEM_TEXT = "Export project";

	/** The run item text. */
	private static final String RUN_ITEM_TEXT = "Run TAF";

	/** The quit item text. */
	private static final String QUIT_ITEM_TEXT = "Close project";

	/** The settings menu text. */
	private static final String SETTINGS_MENU_TEXT = "Settings";

	/** The path item text. */
	private static final String PATH_ITEM_TEXT = "Path settings";

	private static final String ERROR_SAVE_DIALOG_TEXT = "An error occured when trying to save...\n";

	private static final String ERROR_EXPORT_DIALOG_TEXT = "An error occured when trying to export...\n";

	/**
	 * Instantiates a new taf project menu bar.
	 */
	public TafProjectMenuBar() {
		JMenu projectMenu = new JMenu(PROJECT_MENU_TEXT);
		JMenuItem saveItem = new JMenuItem(SAVE_ITEM_TEXT);
		JMenuItem exportItem = new JMenuItem(EXPORT_ITEM_TEXT);
		JMenuItem runItem = new JMenuItem(RUN_ITEM_TEXT);
		JMenuItem quitItem = new JMenuItem(QUIT_ITEM_TEXT);

		JMenu settingsMenu = new JMenu(SETTINGS_MENU_TEXT);
		JMenuItem pathItem = new JMenuItem(PATH_ITEM_TEXT);

		saveItem.addActionListener(e -> save());
		exportItem.addActionListener(e -> export());
		runItem.addActionListener(e -> run());
		quitItem.addActionListener(e -> quit());

		pathItem.addActionListener(e -> new SettingsDialog().initDialog());

		projectMenu.add(saveItem);
		projectMenu.add(exportItem);
		projectMenu.add(runItem);
		projectMenu.add(quitItem);

		settingsMenu.add(pathItem);

		this.add(projectMenu);
		this.add(settingsMenu);
	}

	/**
	 * Asks if the user wants to save.
	 *
	 * @return the {@link JOptionPane} answer
	 */
	private int askSave() {
		int answer = JOptionPane.showConfirmDialog(null, Consts.SAVE_DIALOG_TEXT, Consts.SAVE_DIALOG_TITLE, JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
		return answer;
	}

	/**
	 * Exports the project to XML.
	 */
	private void export() {
		int save = askSave();
		if (save != JOptionPane.YES_OPTION && save != JOptionPane.NO_OPTION) {
			return;
		}

		try {
			SaveManager.getInstance().exportToXML(save == JOptionPane.YES_OPTION);
		} catch (IOException e) {
			Consts.showError(ERROR_EXPORT_DIALOG_TEXT + e.getMessage());
		}
	}

	/**
	 * Quits and closes the project.
	 */
	private void quit() {
		int save = askSave();
		if (save != JOptionPane.YES_OPTION && save != JOptionPane.NO_OPTION) {
			return;
		}

		MainMenuFrame frame = new MainMenuFrame();
		frame.initFrame();
		try {
			SaveManager.getInstance().closeProject(save == JOptionPane.YES_OPTION);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Runs the project.
	 */
	private void run() {
		int save = askSave();
		if (save == JOptionPane.YES_OPTION) {
			save();
		}

		try {
			RunManager.getInstance().prepareRunManager();
			RunFrame runFrame = new RunFrame();
			runFrame.initFrame();
			Event event = new ProjectRunOpenedEvent();
			EventManager.getInstance().fireEvent(event);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves the opened project.
	 */
	private void save() {
		try {
			SaveManager.getInstance().saveProject();
		} catch (IOException e) {
			Consts.showError(ERROR_SAVE_DIALOG_TEXT + e.getMessage());
		}
	}

}
