package com.taf.frame.popup;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.taf.event.PopupProjectDeletedEvent;
import com.taf.event.PopupProjectOpenedEvent;
import com.taf.manager.EventManager;

public class ProjectPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 3680615409452198572L;
	
	private static final String OPEN_ITEM_TEXT = "Open project";
	private static final String DELETE_ITEM_TEXT = "Delete project";

	public ProjectPopupMenu() {
		JMenuItem openItem = new JMenuItem(OPEN_ITEM_TEXT);
		JMenuItem deleteItem = new JMenuItem(DELETE_ITEM_TEXT);
		
		openItem.addActionListener(e -> open());
		deleteItem.addActionListener(e -> delete());
		
		this.add(openItem);
		this.add(deleteItem);
	}
	
	private void open() {
		EventManager.getInstance().fireEvent(new PopupProjectOpenedEvent());
	}
	
	private void delete() {
		EventManager.getInstance().fireEvent(new PopupProjectDeletedEvent());
	}
	
}
