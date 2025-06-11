package com.taf.frame.panel.entity;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;

import com.taf.logic.field.Node;
import com.taf.manager.ConstantManager;

public class NodePropertyPanel extends EntityPropertyPanel {

	private static final long serialVersionUID = 8423915116760040223L;

	public NodePropertyPanel(Node node) {
		super(node);

		GridBagConstraints c = ConstantManager.getDefaultConstraint();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, ConstantManager.SMALL_INSET_GAP, ConstantManager.SMALL_INSET_GAP);
		c.weightx = 0;
		c.weighty = 0;
		JLabel nodeLabel = new JLabel(ConstantManager.NODE_NAME_LABEL_TEXT);
		this.add(nodeLabel, c);

		c.insets = new Insets(0, ConstantManager.SMALL_INSET_GAP, ConstantManager.SMALL_INSET_GAP, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		this.add(entityName, c);
	}

}
