package com.taf.event.entity;

import com.taf.event.Event;

public class EntityNameChangedEvent implements Event {

	private String oldName;
	private String newName;
	
	public EntityNameChangedEvent(String oldName, String newName) {
		this.oldName = oldName;
		this.newName = newName;
	}
	
	public String getOldName() {
		return oldName;
	}
	
	public String getNewName() {
		return newName;
	}

}
