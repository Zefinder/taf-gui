package com.taf.event;

import com.taf.logic.field.Field;

public class FieldSelectedEvent implements Event {

	private final Field field;
	
	public FieldSelectedEvent(Field field) {
		this.field = field;
	}
	
	public Field getField() {
		return field;
	}

}
