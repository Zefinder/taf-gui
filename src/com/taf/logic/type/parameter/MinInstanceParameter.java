package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class MinInstanceParameter extends MinParameter {

	public static final String PARAMETER_NAME = "min";
	
	MinInstanceParameter() {
		super(PARAMETER_NAME);
	}

	public MinInstanceParameter(Integer value) {
		super(PARAMETER_NAME, value, false);
	}

	@Override
	public void valuefromString(String stringValue) throws ParseException {
		try {
			this.value = Integer.valueOf(stringValue);
		} catch (NumberFormatException e) {
			throw new ParseException("Min instance number must be an integer!");
		}
	}
}
