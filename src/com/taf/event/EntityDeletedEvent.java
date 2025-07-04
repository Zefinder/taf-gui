package com.taf.event;

import com.taf.logic.Entity;
import com.taf.logic.field.Type;

public class EntityDeletedEvent implements Event {

	private Entity entity;
	private Type parent;
	
	public EntityDeletedEvent(Entity entity, Type parent) {
		this.entity = entity;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public Type getParent() {
		return parent;
	}

}
