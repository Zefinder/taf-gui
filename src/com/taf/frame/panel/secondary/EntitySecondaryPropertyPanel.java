package com.taf.frame.panel.secondary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

public abstract class EntitySecondaryPropertyPanel extends JPanel {

	private static final long serialVersionUID = 3797269772034697720L;

	public EntitySecondaryPropertyPanel() {
		this.setLayout(new GridBagLayout());
	}
	
	protected void addComponent(JComponent component, GridBagConstraints c) {
		this.add(component, c);
 	}
	
}
