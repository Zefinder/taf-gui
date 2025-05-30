package com.taf.frame.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.taf.event.EventListener;
import com.taf.event.EventMethod;
import com.taf.event.FieldSelectedEvent;
import com.taf.event.FieldTypeChangedEvent;
import com.taf.frame.panel.field.FieldPanelFactory;
import com.taf.frame.panel.field.FieldPropertyPanel;
import com.taf.frame.panel.type.TypePanelFactory;
import com.taf.frame.panel.type.TypePropertyPanel;
import com.taf.logic.field.Field;
import com.taf.manager.ConstantManager;
import com.taf.manager.EventManager;

public class PropertyPanel extends JPanel implements EventListener {

	private static final long serialVersionUID = -1873086205248435494L;

	private Field field;
	private FieldPropertyPanel fieldPropertyPanel;
	private TypePropertyPanel typePropertyPanel;

	public PropertyPanel() {
		this.setLayout(new GridBagLayout());
		EventManager.getInstance().registerEventListener(this);

		field = null;
		fieldPropertyPanel = null;
		typePropertyPanel = null;

		updatePanel();
	}

	private void updatePanel() {
		// Remove everything from panel
		this.removeAll();

		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));		

		if (field != null) {			
			GridBagConstraints c = ConstantManager.getDefaultConstraint();
			c.anchor = GridBagConstraints.NORTH;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.insets = new Insets(10, 0, 10, 0);
			c.weighty = 0;
			fieldPropertyPanel = FieldPanelFactory.createFieldPropertyPanel(field);
			this.add(fieldPropertyPanel, c);
			
			typePropertyPanel = TypePanelFactory.createTypePropertyPanel(field.getType());
			boolean noTypeProperty = typePropertyPanel == null;
			c.insets = new Insets(5, 0, 5, 0);
			c.gridy = 1;
			c.weighty = noTypeProperty ? 1 : 0;
			JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
			this.add(separator, c);
			
			if (!noTypeProperty) {
				c.insets = new Insets(10, 0, 10, 0);
				c.weighty = 1;
				c.gridy = 2;
				this.add(typePropertyPanel, c);				
			}
		}

		// removeAll method invalidates
		this.validate();
	}

	@EventMethod
	public void onFieldSelected(FieldSelectedEvent event) {
		this.field = event.getField();
		updatePanel();
	}
	
	@EventMethod
	public void onFieldTypeChanged(FieldTypeChangedEvent event) {
		updatePanel();
	}

}
