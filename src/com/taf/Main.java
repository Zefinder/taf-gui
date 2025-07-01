package com.taf;

import javax.swing.SwingUtilities;

import com.taf.frame.MainMenuFrame;
import com.taf.manager.Manager;

public class Main {

	public static void main(String[] args) {
		Manager.initAllManagers();
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	new MainMenuFrame().initFrame();
		    }
		});
	}
}
