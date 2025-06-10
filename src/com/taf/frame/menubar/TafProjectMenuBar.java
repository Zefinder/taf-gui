package com.taf.frame.menubar;

import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.taf.event.ProjectClosedEvent;
import com.taf.frame.MainMenuFrame;
import com.taf.manager.EventManager;
import com.taf.manager.SaveManager;

public class TafProjectMenuBar extends JMenuBar {

	private static final long serialVersionUID = -7423744825543652418L;

	public TafProjectMenuBar() {
		JMenu projectMenu = new JMenu("Project");
		JMenuItem saveItem = new JMenuItem("Save project");
		JMenuItem runMenu = new JMenuItem("Run TAF");
		JMenuItem quitItem = new JMenuItem("Quit");

		JMenu settingsMenu = new JMenu("Settings");
		JMenuItem pathItem = new JMenuItem("Path settings");

		saveItem.addActionListener(e -> save());
		quitItem.addActionListener(e -> quit());

		projectMenu.add(saveItem);
		projectMenu.add(runMenu);
		projectMenu.add(quitItem);

		settingsMenu.add(pathItem);

		this.add(projectMenu);
		this.add(settingsMenu);
	}

	private void save() {
		try {
			SaveManager.getInstance().saveProject();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "An error occured when trying to save...\n" + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void quit() {
		try {
			SaveManager.getInstance().saveProject();
		} catch (IOException e) {
			int answer = JOptionPane.showConfirmDialog(null,
					"An error occured when trying to save... Do you want to quit anyways?\n" + e.getMessage(), "Error",
					JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
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
