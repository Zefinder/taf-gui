package com.taf.event.entity.creation;

import com.taf.event.Event;
import com.taf.logic.field.Type;

public class TypeCreatedEvent implements Event {

	private Type type;
	
	public TypeCreatedEvent(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}

}
