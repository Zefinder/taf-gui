package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class MaxIntegerParameter extends MaxParameter {

	public static final String PARAMETER_NAME = "max";
	
	MaxIntegerParameter() {
		super(PARAMETER_NAME);
	}
	
	public MaxIntegerParameter(Number value) {
		super(PARAMETER_NAME, value, false);
	}
	
	@Override
	public void valuefromString(String stringValue) throws ParseException {
		try {			
			this.value = Integer.valueOf(stringValue);
		} catch (NumberFormatException e) {
			throw new ParseException("Max integer number must be an integer!");
		}
	}
}
