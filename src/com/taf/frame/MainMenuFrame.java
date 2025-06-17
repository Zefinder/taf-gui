package com.taf.frame;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.taf.event.EventListener;
import com.taf.event.EventMethod;
import com.taf.event.ProjectOpenedEvent;
import com.taf.frame.panel.ProjectChooserPanel;
import com.taf.manager.EventManager;


public class MainMenuFrame extends JFrame implements EventListener {

	private static final long serialVersionUID = 1313545451015862976L;
	private static final String FRAME_NAME = "TAF project explorer";

	
	public MainMenuFrame() {
		this.setTitle(FRAME_NAME);
		this.setSize(400, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		this.setLayout(new BorderLayout());
		this.add(new ProjectChooserPanel());
		
		EventManager.getInstance().registerEventListener(this);
		
		this.setVisible(false);
	}

	public void initFrame() {
		this.setVisible(true);
	}
	
	@EventMethod
	public void onProjectOpened(ProjectOpenedEvent event) {
		this.dispose();
	}
	
}
