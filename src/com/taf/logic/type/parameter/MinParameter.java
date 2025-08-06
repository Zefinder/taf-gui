package com.taf.logic.type.parameter;

import java.text.DecimalFormat;

import com.taf.util.Consts;

public abstract class MinParameter extends TypeParameter {

	public static final String PARAMETER_NAME = "min";

	private final DecimalFormat realFormatter = Consts.REAL_FORMATTER;

	protected Number value;
	private boolean isReal;

	MinParameter(String name) {
		super(name);
	}
	
	public MinParameter(String name, Number value, boolean isReal) {
		this(name);
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
