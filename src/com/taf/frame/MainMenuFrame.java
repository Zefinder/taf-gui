package com.taf.frame;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.taf.event.EventListener;
import com.taf.event.EventMethod;
import com.taf.event.ProjectOpenedEvent;
import com.taf.frame.menubar.MainMenuBar;
import com.taf.frame.panel.ProjectChooserPanel;
import com.taf.manager.EventManager;


public class MainMenuFrame extends JFrame implements EventListener {

	private static final long serialVersionUID = 1313545451015862976L;
	private static final String FRAME_NAME = "TAF project explorer";

	private ProjectChooserPanel chooserPanel;
	
	public MainMenuFrame() {
		this.setTitle(FRAME_NAME);
		this.setSize(400, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		this.setLayout(new BorderLayout());
		this.setJMenuBar(new MainMenuBar());
		
		chooserPanel = new ProjectChooserPanel();
		this.add(chooserPanel);
		
		EventManager.getInstance().registerEventListener(this);
		
		this.setVisible(false);
	}

	public void initFrame() {
		this.setVisible(true);
	}
	
	@Override
	public void unregisterComponents() {
		EventManager.getInstance().unregisterEventListener(chooserPanel);
	}
	
	@EventMethod
	public void onProjectOpened(ProjectOpenedEvent event) {
		EventManager.getInstance().unregisterEventListener(this);
		this.dispose();
	}
	
}
