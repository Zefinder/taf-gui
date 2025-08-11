package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class TypeNameParameter extends TypeParameter {

	public static final String PARAMETER_NAME = "type";
	
	private static final String NULL_ERROR_MESSAGE = "The name cannot be null";
	private static final String ERROR_MESSAGE = "The name cannot be blank nor empty";

	private String typeName;

	TypeNameParameter() {
		super(PARAMETER_NAME);
	}

	public TypeNameParameter(String typeName) {
		this();
		this.typeName = typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public void stringToValue(String stringValue) throws ParseException {
		if (stringValue == null) {
			throw new ParseException(this.getClass(), NULL_ERROR_MESSAGE);
		}
		
		if (!stringValue.isBlank()) {
			this.typeName = stringValue;
		} else {
			throw new ParseException(this.getClass(), ERROR_MESSAGE);
		}
	}

	@Override
	public String valueToString() {
		return typeName;
	}

}
