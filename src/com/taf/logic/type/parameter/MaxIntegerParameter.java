package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class MaxIntegerParameter extends MaxParameter {

	private static final String ERROR_MESSAGE = "Max integer number must be an integer!";
	
	public static final String PARAMETER_NAME = "max";
	
	MaxIntegerParameter() {
		super(PARAMETER_NAME);
	}
	
	public MaxIntegerParameter(int value) {
		super(PARAMETER_NAME, value, false);
	}
	
	@Override
	void stringToValue(String stringValue) throws ParseException {
		try {			
			this.value = Integer.valueOf(stringValue);
		} catch (NumberFormatException e) {
			throw new ParseException(this.getClass(), ERROR_MESSAGE);
		}
	}
}
