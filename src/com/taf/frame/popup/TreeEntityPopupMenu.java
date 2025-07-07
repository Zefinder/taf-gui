package com.taf.frame.popup;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.taf.event.Event;
import com.taf.event.entity.EntityDeletedEvent;
import com.taf.logic.Entity;
import com.taf.logic.field.Type;
import com.taf.manager.EventManager;

public class TreeEntityPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = -1647472349297224736L;

	private static final String DELETE_ITEM_TEXT = "Delete";
	
	public TreeEntityPopupMenu(Entity entity) {
		JMenuItem deleteItem = new JMenuItem(DELETE_ITEM_TEXT);
		deleteItem.addActionListener(e -> {
			Type parent = entity.getParent();
			parent.removeEntity(entity);
			Event event = new EntityDeletedEvent(entity);
			EventManager.getInstance().fireEvent(event);
		});
		
		this.add(deleteItem);
	}

}
