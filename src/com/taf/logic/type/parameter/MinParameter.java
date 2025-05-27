package com.taf.logic.type.parameter;

import java.text.DecimalFormat;

import com.taf.manager.ConstantManager;

public class MinParameter extends TypeParameter {

	private static final String PARAMETER_NAME = "min";
	
	private final DecimalFormat realFormatter = ConstantManager.REAL_FORMATTER;

	private Number value;
	private boolean isReal;

	public MinParameter(Number value, boolean isReal) {
		super(PARAMETER_NAME);
		this.value = value;
		this.isReal = isReal;
	}

	public void setValue(Number value) {
		this.value = value;
	}

	@Override
	public String valueToString() {
		return isReal ? realFormatter.format(value.doubleValue()) : String.valueOf(value.longValue());
	}

}
