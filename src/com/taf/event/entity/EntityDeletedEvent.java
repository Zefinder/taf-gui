package com.taf.event.entity;

import com.taf.event.Event;
import com.taf.logic.Entity;

public class EntityDeletedEvent implements Event {

	private Entity entity;
	
	public EntityDeletedEvent(Entity entity) {
		this.entity = entity;
	}
	
	public Entity getEntity() {
		return entity;
	}

}
