package com.taf.frame.panel.secondary.type;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JButton;

import com.taf.event.Event;
import com.taf.event.entity.creation.TypeCreatedEvent;
import com.taf.frame.dialog.TypeCreationDialog;
import com.taf.logic.field.Type;
import com.taf.manager.ConstantManager;
import com.taf.manager.EventManager;

public class RootAddEntityPanel extends TypeAddEntityPanel {

	private static final long serialVersionUID = -9178603688544702107L;

	private static final String ADD_TYPE_BUTTON_TEXT = "+ Add type";

	public RootAddEntityPanel() {
		super();

		GridBagConstraints c = ConstantManager.getDefaultConstraint();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(ConstantManager.MEDIUM_INSET_GAP, 0, 0, 0);
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridy = 3;
		JButton addTypeButton = new JButton(ADD_TYPE_BUTTON_TEXT);
		addTypeButton.addActionListener(e -> {
			TypeCreationDialog dialog = new TypeCreationDialog();
			dialog.initDialog();
			Type type = dialog.getField();
			if (type != null) {
				Event event = new TypeCreatedEvent(type);
				EventManager.getInstance().fireEvent(event);
			}
		});
		this.add(addTypeButton, c);
	}

}
