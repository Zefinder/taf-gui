package com.taf.frame.panel.field;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;

import com.taf.logic.field.Node;
import com.taf.manager.ConstantManager;

public class NodePropertyPanel extends FieldPropertyPanel {

	private static final long serialVersionUID = 8423915116760040223L;

	public NodePropertyPanel(Node node) {
		super(node);
		
		GridBagConstraints c = ConstantManager.getDefaultConstraint();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 5, 5);
		c.weightx = 0;
		c.weighty = 0;
		JLabel fieldLabel = new JLabel(ConstantManager.NODE_NAME_LABEL_TEXT);
		this.add(fieldLabel, c);

		c.insets = new Insets(0, 5, 5, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		this.add(fieldName, c);
	}

}
