package com.taf.frame.panel.secondary;

import java.awt.GridBagConstraints;

import com.taf.frame.panel.secondary.type.TypeAddEntityPanel;
import com.taf.util.Consts;

public class TypePropertyPanel extends EntitySecondaryPropertyPanel {

	private static final long serialVersionUID = 3296878396678633310L;

	public TypePropertyPanel() {
		GridBagConstraints c = Consts.getDefaultConstraint();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;
		this.add(new TypeAddEntityPanel(), c);
	}

}
