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

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.taf.event.Event;
import com.taf.event.RunLocationChangedEvent;
import com.taf.frame.RunFrame;
import com.taf.frame.dialog.SettingsDialog;
import com.taf.manager.EventManager;
import com.taf.manager.RunManager;
import com.taf.manager.SaveManager;
import com.taf.util.Consts;

/**
 * The MainMenuBar is set to the {@link RunFrame} and allows actions such as
 * changing the project run location and TAF's path
 *
 * @see JMenuBar
 * @see RunFrame
 *
 * @author Adrien Jakubiak
 */
public class RunMenuBar extends JMenuBar {

	private static final long serialVersionUID = 5961856479125309084L;

	/** The project menu text. */
	private static final String PROJECT_MENU_TEXT = "Project";

	/** The change project location item text. */
	private static final String CHANGE_PROJECT_LOCATION_ITEM_TEXT = "Change project location";

	/** The settings menu text. */
	private static final String SETTINGS_MENU_TEXT = "Settings";

	/** The path item text. */
	private static final String PATH_ITEM_TEXT = "Path settings";

	private static final String PREPARATION_ERROR_MESSAGE = "Something wrong happened when preparing the new location: ";

	/**
	 * Instantiates a new run menu bar.
	 */
	public RunMenuBar() {
		JMenu projectMenu = new JMenu(PROJECT_MENU_TEXT);
		JMenuItem changeLocationItem = new JMenuItem(CHANGE_PROJECT_LOCATION_ITEM_TEXT);

		JMenu settingsMenu = new JMenu(SETTINGS_MENU_TEXT);
		JMenuItem pathItem = new JMenuItem(PATH_ITEM_TEXT);

		changeLocationItem.addActionListener(e -> {
			int answer = JOptionPane.showConfirmDialog(null, "Do you use the default location?", "Custom location",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if (answer == JOptionPane.YES_OPTION) {
				// Ask if wants to copy
				boolean wantCopy = askCopy();

				try {
					// Remove the run custom location and prepare the run manager
					SaveManager.getInstance().removeRunCustomLocation(wantCopy);
					RunManager.getInstance().prepareRunManager();

					// Send event to tell that location changed
					Event event = new RunLocationChangedEvent();
					EventManager.getInstance().fireEvent(event);
				} catch (IOException e1) {
					Consts.showError(PREPARATION_ERROR_MESSAGE + e1.getMessage());
					e1.printStackTrace();
				}
			} else if (answer == JOptionPane.NO_OPTION) {
				// Show a file chooser and select a directory!
				JFileChooser projectLocationChooser = new JFileChooser();
				projectLocationChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				projectLocationChooser.setMultiSelectionEnabled(false);
				answer = projectLocationChooser.showDialog(null, "Select location");

				if (answer == JFileChooser.APPROVE_OPTION) {
					File newLocation = projectLocationChooser.getSelectedFile();

					if (!newLocation.exists()) {
						newLocation.mkdir();
					}

					try {
						// Ask if want to copy the already existing files
						boolean wantCopy = askCopy();
						SaveManager.getInstance().changeRunCustomLocation(newLocation.getAbsolutePath(), wantCopy);

						// Prepare the run manager
						RunManager.getInstance().prepareRunManager();

						// Send event to tell that location changed
						Event event = new RunLocationChangedEvent();
						EventManager.getInstance().fireEvent(event);
					} catch (IOException e1) {
						Consts.showError(PREPARATION_ERROR_MESSAGE + e1.getMessage());
						e1.printStackTrace();
					}
				} else {
					return;
				}
			}
		});

		pathItem.addActionListener(e -> new SettingsDialog().initDialog());

		projectMenu.add(changeLocationItem);
		settingsMenu.add(pathItem);

		this.add(projectMenu);
		this.add(settingsMenu);
	}

	/**
	 * Ask if the user wants to copy the files to another location.
	 *
	 * @return true if the user wants to copy
	 */
	private boolean askCopy() {
		int answer = JOptionPane.showConfirmDialog(null, "Do you want to copy the setting files?", "Copy?",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		return answer == JOptionPane.YES_OPTION;
	}

}
