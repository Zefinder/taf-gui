package com.taf.event;

import com.taf.logic.type.Type;

public class FieldTypeChangedEvent implements Event {

	private Type type;
	
	public FieldTypeChangedEvent(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}
	
}
