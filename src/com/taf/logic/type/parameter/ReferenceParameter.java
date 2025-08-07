package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class ReferenceParameter extends TypeParameter {

	public static final String PARAMETER_NAME = "ref";

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
		if (!stringValue.isBlank()) {
			this.refName = stringValue;
		} else {
			throw new ParseException(this.getClass(), "The name cannot be blank nor empty");
		}
	}

	@Override
	public String valueToString() {
		return refName;
	}

}
