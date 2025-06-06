package com.taf.frame;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.taf.frame.panel.ProjectChooserPanel;
import com.taf.manager.Manager;


public class MainMenuFrame extends JFrame {

	private static final long serialVersionUID = 1313545451015862976L;

	public MainMenuFrame() {
		this.setTitle("TAF project explorer");
		this.setSize(400, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		this.setLayout(new BorderLayout());
		this.add(new ProjectChooserPanel());
		
		this.setVisible(false);
	}

	public void initFrame() {
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		Manager.initAllManagers();
		new MainMenuFrame().initFrame();
	}
	
}
