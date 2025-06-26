package com.taf.frame;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;

import com.taf.exception.ParseException;
import com.taf.frame.panel.RunPanel;
import com.taf.manager.Manager;
import com.taf.manager.RunManager;
import com.taf.manager.SaveManager;

public class RunFrame extends JFrame {

	private static final long serialVersionUID = -2445180809943024092L;

	private static final String FRAME_NAME = "Run TAF";
	
	public RunFrame() {
		this.setTitle(FRAME_NAME);
		this.setSize(1000, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		
		this.setLayout(new BorderLayout());
		this.add(new RunPanel());
		
		this.setVisible(false);
	}
	
	public void initFrame() {
		this.setVisible(true);
	}
	
	public static void main(String[] args) throws IOException, ParseException {
		Manager.initAllManagers();
		SaveManager.getInstance().openProject("test.taf");
		RunManager.getInstance().prepareRunManager();
		new RunFrame().initFrame();
	}

}
