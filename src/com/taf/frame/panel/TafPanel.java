package com.taf.frame.panel;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.taf.event.EventListener;
import com.taf.logic.field.Root;
import com.taf.manager.EventManager;

public class TafPanel extends JPanel implements EventListener {

	private static final long serialVersionUID = -7098545217788512796L;

	private FieldTreePanel fieldsPanel;
	private PropertyPanel propertyPanel;

	public TafPanel(Root root) {
		this.setLayout(new BorderLayout());
		fieldsPanel = new FieldTreePanel(root);
		propertyPanel = new PropertyPanel();
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, fieldsPanel, propertyPanel);
		splitPane.setResizeWeight(.15);
		
		this.add(splitPane);
	}
	
	@Override
	public void unregisterComponents() {
		EventManager.getInstance().unregisterEventListener(fieldsPanel);
		EventManager.getInstance().unregisterEventListener(propertyPanel);
	}
}
