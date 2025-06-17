package com.taf;

import com.taf.frame.MainMenuFrame;
import com.taf.manager.Manager;

public class Main {

	public static void main(String[] args) {
		Manager.initAllManagers();
		new MainMenuFrame().initFrame();
	}
}
