package com.taf.frame.menubar;

import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.taf.event.ProjectClosedEvent;
import com.taf.frame.MainMenuFrame;
import com.taf.manager.ConstantManager;
import com.taf.manager.EventManager;
import com.taf.manager.SaveManager;

public class TafProjectMenuBar extends JMenuBar {

	private static final long serialVersionUID = -7423744825543652418L;

	private static final String PROJECT_MENU_TEXT = "Project";
	private static final String SAVE_ITEM_TEXT = "Save project";
	private static final String RUN_ITEM_TEXT = "Run TAF";
	private static final String QUIT_ITEM_TEXT = "Quit";
	private static final String SETTINGS_MENU_TEXT = "Settings";
	private static final String PATH_ITEM_TEXT = "Path settings";

	private static final String ERROR_DIALOG_TEXT = "An error occured when trying to save...\n";
	private static final String ERROR_INPUT_DIALOG_TITLE = "Error!";
	private static final String ERROR_INPUT_DIALOG_TEXT = "An error occured when trying to save... Do you want to quit anyways?\n";

	public TafProjectMenuBar() {
		JMenu projectMenu = new JMenu(PROJECT_MENU_TEXT);
		JMenuItem saveItem = new JMenuItem(SAVE_ITEM_TEXT);
		JMenuItem runItem = new JMenuItem(RUN_ITEM_TEXT);
		JMenuItem quitItem = new JMenuItem(QUIT_ITEM_TEXT);

		JMenu settingsMenu = new JMenu(SETTINGS_MENU_TEXT);
		JMenuItem pathItem = new JMenuItem(PATH_ITEM_TEXT);

		saveItem.addActionListener(e -> save());
		quitItem.addActionListener(e -> quit());

		projectMenu.add(saveItem);
		projectMenu.add(runItem);
		projectMenu.add(quitItem);

		settingsMenu.add(pathItem);

		this.add(projectMenu);
		this.add(settingsMenu);
	}

	private void save() {
		try {
			SaveManager.getInstance().saveProject();
		} catch (IOException e) {
			ConstantManager.showError(ERROR_DIALOG_TEXT + e.getMessage());
		}
	}

	private void quit() {
		try {
			SaveManager.getInstance().saveProject();
		} catch (IOException e) {
			int answer = JOptionPane.showConfirmDialog(null, ERROR_INPUT_DIALOG_TEXT + e.getMessage(),
					ERROR_INPUT_DIALOG_TITLE, JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
			if (answer != JOptionPane.YES_OPTION) {
				return;
			}
		}

		// TODO Quit project and change frame
		MainMenuFrame frame = new MainMenuFrame();
		frame.initFrame();
		EventManager.getInstance().fireEvent(new ProjectClosedEvent());
	}

}
