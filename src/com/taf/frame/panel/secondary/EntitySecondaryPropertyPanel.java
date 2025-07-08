package com.taf.frame.panel.secondary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.taf.event.EventListener;
import com.taf.manager.EventManager;

public abstract class EntitySecondaryPropertyPanel extends JPanel implements EventListener {

	private static final long serialVersionUID = 3797269772034697720L;

	public EntitySecondaryPropertyPanel() {
		this.setLayout(new GridBagLayout());
		EventManager.getInstance().registerEventListener(this);
	}
	
	protected void addComponent(JComponent component, GridBagConstraints c) {
		this.add(component, c);
 	}
	
	@Override
	public void unregisterComponents() {
		// Nothing to unregister
	}
	
}
