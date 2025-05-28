package com.taf.frame;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.taf.frame.panel.FieldsPanel;

public class TafFrame extends JFrame {

	private static final long serialVersionUID = 1724446330461662942L;

	public TafFrame() {
		this.setTitle("TAF GUI");
		this.setSize(600, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		
		this.add(createJTreePanel());
		
		this.setVisible(false);
	}
	
	private JPanel createJTreePanel() {
		return new FieldsPanel();
	}
	
	public void initFrame() {
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new TafFrame().initFrame();
	}

}
