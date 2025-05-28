package com.taf.frame.panel.field;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.taf.event.Event;
import com.taf.event.FieldNameChangedEvent;
import com.taf.logic.field.Node;
import com.taf.manager.EventManager;

public class NodePropertyPanel extends FieldPropertyPanel {

	private static final long serialVersionUID = 8423915116760040223L;

	private final JTextField fieldName;

	private Node node;
	private String name;

	public NodePropertyPanel(Node node) {
		this.setLayout(new GridBagLayout());
		
		this.node = node;
		this.name = node.getName();

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 0, 5, 5);
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		JLabel fieldLabel = new JLabel("Parameter name");
		this.add(fieldLabel, c);

		c.insets = new Insets(0, 5, 5, 0);
		c.gridx = 1;
		fieldName = new JTextField(20);
		fieldName.setText(name);
		fieldName.addActionListener(e -> updateFieldName());
		this.add(fieldName, c);
	}

	private void updateFieldName() {
		String text = fieldName.getText();
		if (!text.isBlank()) {
			node.setName(text);
			name = text;
			
			Event event = new FieldNameChangedEvent(name, text);
			EventManager.getInstance().fireEvent(event);
		}
	}

}
