package com.taf.logic.constraint.parameter;

public enum TypeConstraintParameterEnum {

	FORALL("forall"), EXISTS("exists");

	private final String value;

	private TypeConstraintParameterEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
