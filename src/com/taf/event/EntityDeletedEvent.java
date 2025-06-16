package com.taf.event;

import com.taf.logic.Entity;
import com.taf.logic.field.Node;

public class EntityDeletedEvent implements Event {

	private Entity entity;
	private Node parent;
	
	public EntityDeletedEvent(Entity entity, Node parent) {
		this.entity = entity;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public Node getParent() {
		return parent;
	}

}
