package com.taf.frame;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.taf.event.EventListener;
import com.taf.event.EventMethod;
import com.taf.event.ProjectClosedEvent;
import com.taf.frame.menubar.TafProjectMenuBar;
import com.taf.frame.panel.TafPanel;
import com.taf.logic.field.Root;
import com.taf.manager.EventManager;

public class ProjectFrame extends JFrame implements EventListener {

	private static final long serialVersionUID = 1724446330461662942L;

	private static final String FRAME_NAME = "TAF GUI";
	
	private final TafPanel tafPanel;
	// TODO Add verification to check if saved before quit !
	
	public ProjectFrame(Root root) {
		this.setTitle(FRAME_NAME);
		this.setSize(650, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		this.setJMenuBar(new TafProjectMenuBar());
		
		tafPanel = new TafPanel(root);
		this.setLayout(new BorderLayout());
		this.add(tafPanel);
		
		EventManager.getInstance().registerEventListener(this);
		
		this.setVisible(false);
	}
	
	public void initFrame() {
		this.setVisible(true);
	}
	
	@Override
	public void unregisterComponents() {
		EventManager.getInstance().unregisterEventListener(tafPanel);
	}
	
	@EventMethod
	public void onProjectClosed(ProjectClosedEvent event) {
		EventManager.getInstance().unregisterEventListener(this);		
		this.dispose();
	}

}
