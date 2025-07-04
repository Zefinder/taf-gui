package com.taf.event;

import com.taf.logic.type.FieldType;

public class FieldTypeChangedEvent implements Event {

	private FieldType type;
	
	public FieldTypeChangedEvent(FieldType type) {
		this.type = type;
	}

	public FieldType getType() {
		return type;
	}
	
}
