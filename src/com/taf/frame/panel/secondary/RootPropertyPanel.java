package com.taf.frame.panel.secondary;

import java.awt.GridBagConstraints;

import com.taf.frame.panel.secondary.type.RootAddEntityPanel;
import com.taf.manager.ConstantManager;

public class RootPropertyPanel extends EntitySecondaryPropertyPanel {

	private static final long serialVersionUID = 3296878396678633310L;

	public RootPropertyPanel() {
		GridBagConstraints c = ConstantManager.getDefaultConstraint();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;
		this.add(new RootAddEntityPanel(), c);
	}

}
