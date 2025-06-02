package com.taf.logic.type.parameter;

public class MaxInstanceParameter extends TypeParameter {

	private static final String PARAMETER_NAME = "max";

	private Integer value;

	public MaxInstanceParameter(Integer value) {
		super(PARAMETER_NAME);
		this.value = value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}

	@Override
	public String valueToString() {
		return String.valueOf(value.longValue());
	}

}
