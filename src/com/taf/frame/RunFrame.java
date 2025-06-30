package com.taf.frame;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.taf.event.Event;
import com.taf.event.ProjectStopRunEvent;
import com.taf.frame.panel.RunPanel;
import com.taf.manager.EventManager;

public class RunFrame extends JFrame {

	private static final long serialVersionUID = -2445180809943024092L;

	private static final String FRAME_NAME = "Run TAF";
	
	private RunPanel runPanel;
	
	public RunFrame() {
		this.setTitle(FRAME_NAME);
		this.setSize(1000, 600);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// Send the event to notify that the frame is closed
				Event event = new ProjectStopRunEvent();
				EventManager.getInstance().fireEvent(event);
				runPanel.unregisterConsolePanel();
			}
		});
		
		this.setLayout(new BorderLayout());
		runPanel = new RunPanel();
		this.add(runPanel);
		
		this.setVisible(false);
	}
	
	public void initFrame() {
		this.setVisible(true);
	}

}
