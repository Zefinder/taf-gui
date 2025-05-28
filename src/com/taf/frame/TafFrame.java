package com.taf.frame;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.taf.frame.panel.TafPanel;

public class TafFrame extends JFrame {

	private static final long serialVersionUID = 1724446330461662942L;

	public TafFrame() {
		this.setTitle("TAF GUI");
		this.setSize(650, 550);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		
		this.setLayout(new BorderLayout());
		this.add(new TafPanel());
		
		this.setVisible(false);
	}
	
	public void initFrame() {
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new TafFrame().initFrame();
	}

}
