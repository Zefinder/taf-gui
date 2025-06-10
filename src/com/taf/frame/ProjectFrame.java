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

	public ProjectFrame(Root root) {
		this.setTitle("TAF GUI");
		this.setSize(650, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		this.setJMenuBar(new TafProjectMenuBar());
		
		this.setLayout(new BorderLayout());
		this.add(new TafPanel(root));
		
		EventManager.getInstance().registerEventListener(this);
		
		this.setVisible(false);
	}
	
	public void initFrame() {
		this.setVisible(true);
	}
	
	@EventMethod
	public void onProjectClosed(ProjectClosedEvent event) {
		this.dispose();
	}

}
