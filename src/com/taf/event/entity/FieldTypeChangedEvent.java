package com.taf.event.entity;

import com.taf.event.Event;
import com.taf.logic.field.Field;
import com.taf.logic.type.FieldType;

public class FieldTypeChangedEvent implements Event {

	private Field field;
	private FieldType type;
	
	public FieldTypeChangedEvent(Field field, FieldType type) {
		this.field = field;
		this.type = type;
	}

	public Field getField() {
		return field;
	}
	
	public FieldType getType() {
		return type;
	}
	
}
