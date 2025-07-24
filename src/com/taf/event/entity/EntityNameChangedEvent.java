package com.taf.event.entity;

import com.taf.event.Event;
import com.taf.logic.Entity;

public class EntityNameChangedEvent implements Event {

	private Entity entity;
	private String oldName;
	private String newName;
	
	public EntityNameChangedEvent(Entity entity, String oldName, String newName) {
		this.entity = entity;
		this.oldName = oldName;
		this.newName = newName;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public String getOldName() {
		return oldName;
	}
	
	public String getNewName() {
		return newName;
	}

}
