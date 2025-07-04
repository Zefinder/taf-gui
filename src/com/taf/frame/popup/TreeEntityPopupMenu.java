package com.taf.frame.popup;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.taf.event.EntityDeletedEvent;
import com.taf.event.Event;
import com.taf.logic.Entity;
import com.taf.logic.field.Node;
import com.taf.manager.EventManager;

public class TreeEntityPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = -1647472349297224736L;

	private static final String DELETE_ITEM_TEXT = "Delete";
	
	public TreeEntityPopupMenu(Entity entity) {
		JMenuItem deleteItem = new JMenuItem(DELETE_ITEM_TEXT);
		deleteItem.addActionListener(e -> {
			Node parent = entity.getParent();
			parent.removeEntity(entity);
			Event event = new EntityDeletedEvent(entity, parent);
			EventManager.getInstance().fireEvent(event);
		});
		
		this.add(deleteItem);
	}

}
