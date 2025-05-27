package com.taf.logic.type.parameter;

public class MinInstanceParameter extends TypeParameter {

	private static final String PARAMETER_NAME = "min";
	
	private Number value;

	public MinInstanceParameter(Number value) {
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
