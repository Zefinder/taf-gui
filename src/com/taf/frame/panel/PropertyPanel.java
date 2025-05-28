package com.taf.frame.panel;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.taf.event.EventListener;
import com.taf.event.EventMethod;
import com.taf.event.FieldSelectedEvent;
import com.taf.frame.panel.field.FieldPanelFactory;
import com.taf.frame.panel.field.FieldPropertyPanel;
import com.taf.frame.panel.type.TypePanelFactory;
import com.taf.frame.panel.type.TypePropertyPanel;
import com.taf.logic.field.Field;
import com.taf.manager.EventManager;

public class PropertyPanel extends JPanel implements EventListener {

	private static final long serialVersionUID = -1873086205248435494L;

	private Field field;
	private FieldPropertyPanel fieldPropertyPanel;
	private TypePropertyPanel typePropertyPanel;

	public PropertyPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
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
			JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
			fieldPropertyPanel = FieldPanelFactory.createFieldPropertyPanel(field);
			typePropertyPanel = TypePanelFactory.createTypePropertyPanel(field.getType());

			this.add(fieldPropertyPanel);
			this.add(separator);
			if (typePropertyPanel != null)
				this.add(typePropertyPanel);
		}

		// removeAll method invalidates
		this.validate();
	}

	@EventMethod
	public void onFieldSelected(FieldSelectedEvent event) {
		this.field = event.getField();
		updatePanel();
	}

}
