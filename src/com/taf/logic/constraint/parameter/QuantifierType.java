package com.taf.logic.constraint.parameter;

public enum QuantifierType {

	FORALL("forall"), EXISTS("exists");

	private final String value;

	private QuantifierType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	/**
	 * Default value is FORALL
	 * 
	 * @param value
	 * @return
	 */
	public static QuantifierType fromString(String value) {
		if (value.equals(EXISTS.getValue())) {
			return EXISTS;
		}
		
		return FORALL;
	}

}
