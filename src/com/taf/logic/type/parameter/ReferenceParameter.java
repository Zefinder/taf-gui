package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class ReferenceParameter extends TypeParameter {

	public static final String PARAMETER_NAME = "ref";

	private static final String NULL_ERROR_MESSAGE = "The name cannot be null";
	private static final String ERROR_MESSAGE = "The name cannot be blank nor empty";

	private String refName;

	ReferenceParameter() {
		super(PARAMETER_NAME);
	}

	public ReferenceParameter(String refName) {
		this();
		this.refName = refName;
	}

	public void setReferenceName(String refName) {
		this.refName = refName;
	}

	@Override
	void stringToValue(String stringValue) throws ParseException {
		if (stringValue == null) {
			throw new ParseException(this.getClass(), NULL_ERROR_MESSAGE);
		}

		if (!stringValue.isBlank()) {
			this.refName = stringValue;
		} else {
			throw new ParseException(this.getClass(), ERROR_MESSAGE);
		}
	}

	@Override
	public String valueToString() {
		return refName;
	}

}
