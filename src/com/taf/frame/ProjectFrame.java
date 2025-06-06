package com.taf.frame;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;

import com.taf.exception.ParseException;
import com.taf.frame.menubar.TafProjectMenuBar;
import com.taf.frame.panel.TafPanel;
import com.taf.logic.field.Root;
import com.taf.manager.Manager;
import com.taf.manager.SaveManager;

public class ProjectFrame extends JFrame {

	private static final long serialVersionUID = 1724446330461662942L;

	public ProjectFrame(Root root) {
		this.setTitle("TAF GUI");
		this.setSize(650, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		this.setJMenuBar(new TafProjectMenuBar());
		
		this.setLayout(new BorderLayout());
		this.add(new TafPanel(root));
		
		this.setVisible(false);
	}
	
	public void initFrame() {
		this.setVisible(true);
	}
	
	public static void main(String[] args) throws IOException, ParseException {
		Manager.initAllManagers();
		Root root = SaveManager.getInstance().openProject("test.taf");
		new ProjectFrame(root).initFrame();
	}

}
