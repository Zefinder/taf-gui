package com.taf.event.entity;

import com.taf.event.Event;
import com.taf.logic.Entity;

public class EntitySelectedEvent implements Event {

	private final Entity entity;
	
	public EntitySelectedEvent(Entity entity) {
		this.entity = entity;
	}
	
	public Entity getEntity() {
		return entity;
	}

}
