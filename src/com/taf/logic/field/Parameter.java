package com.taf.logic.field;

import com.taf.logic.type.FieldType;

public class Parameter extends Field {

	private static final String PARAMETER_STRING_FORMAT = "<parameter %s/>";

	public Parameter(String name, FieldType type) {
		super(name, type);
	}
	
	@Override
	public String getEntityTypeName() {
		return getType().getName();
	}

	@Override
	public String toString() {
		return PARAMETER_STRING_FORMAT.formatted(super.toString());
	}

}
