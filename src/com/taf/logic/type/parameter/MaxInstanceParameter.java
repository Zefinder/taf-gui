package com.taf.logic.type.parameter;

public class MaxInstanceParameter extends TypeParameter {

	private static final String PARAMETER_NAME = "max";

	private Number value;

	public MaxInstanceParameter(Number value) {
		super(PARAMETER_NAME);
		this.value = value;
	}

	public void setValue(Number value) {
		this.value = value;
	}

	@Override
	public String valueToString() {
		return String.valueOf(value.longValue());
	}

}
