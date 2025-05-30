package com.taf.frame.panel.type;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

public abstract class TypePropertyPanel extends JPanel {

	private static final long serialVersionUID = 3797269772034697720L;

	public TypePropertyPanel() {
		this.setLayout(new GridBagLayout());
	}
	
	protected void addComponent(JComponent component, GridBagConstraints c) {
		this.add(component, c);
 	}
	
}
