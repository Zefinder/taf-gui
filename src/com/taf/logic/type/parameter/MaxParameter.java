package com.taf.logic.type.parameter;

import java.text.DecimalFormat;

import com.taf.manager.ConstantManager;

public abstract class MaxParameter extends TypeParameter {

	private static final String PARAMETER_NAME = "max";

	private final DecimalFormat realFormatter = ConstantManager.REAL_FORMATTER;

	protected Number value;
	private boolean isReal;

	public MaxParameter() {
		super(PARAMETER_NAME);
	}
	
	public MaxParameter(Number value, boolean isReal) {
		this();
		this.value = value;
		this.isReal = isReal;
	}

	public void setValue(Number value) {
		this.value = value;
	}

	public Number getValue() {
		return value;
	}
	
	@Override
	public String valueToString() {
		return isReal ? realFormatter.format(value.doubleValue()) : String.valueOf(value.longValue());
	}

}