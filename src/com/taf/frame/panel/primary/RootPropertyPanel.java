package com.taf.frame.panel.primary;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;

import com.taf.logic.field.Root;
import com.taf.util.Consts;

public class RootPropertyPanel extends EntityPrimaryPropertyPanel {

	private static final long serialVersionUID = 8423915116760040223L;

	public RootPropertyPanel(Root node) {
		super(node);

		GridBagConstraints c = Consts.getDefaultConstraint();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP);
		c.weightx = 0;
		c.weighty = 0;
		JLabel nodeLabel = new JLabel(Consts.ROOT_NAME_LABEL_TEXT);
		this.add(nodeLabel, c);

		c.insets = new Insets(0, Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		this.add(entityName, c);
	}

}
