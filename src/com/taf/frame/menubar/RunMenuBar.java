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
import com.taf.frame.dialog.SettingsDialog;
import com.taf.manager.ConstantManager;
import com.taf.manager.EventManager;
import com.taf.manager.RunManager;
import com.taf.manager.SaveManager;

public class RunMenuBar extends JMenuBar {

	private static final long serialVersionUID = 5961856479125309084L;

	private static final String PROJECT_MENU_TEXT = "Project";
	private static final String CHANGE_PROJECT_LOCATION_ITEM_TEXT = "Change project location";
	private static final String SETTINGS_MENU_TEXT = "Settings";
	private static final String PATH_ITEM_TEXT = "Path settings";

	private static final String PREPARATION_ERROR_MESSAGE = "Something wrong happened when preparing the new location: ";

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
					ConstantManager.showError(PREPARATION_ERROR_MESSAGE + e1.getMessage());
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
						ConstantManager.showError(PREPARATION_ERROR_MESSAGE + e1.getMessage());
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

	private boolean askCopy() {
		int answer = JOptionPane.showConfirmDialog(null, "Do you want to copy the setting files?", "Copy?",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		return answer == JOptionPane.YES_OPTION;
	}

}
