package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class MinIntegerParameter extends MinParameter {
	
	private static final String ERROR_MESSAGE = "Min integer number must be an integer!";
	
	public static final String PARAMETER_NAME = "min";
	
	MinIntegerParameter() {
		super(PARAMETER_NAME);
	}
	
	public MinIntegerParameter(int value) {
		super(PARAMETER_NAME, value, false);
	}
	
	@Override
	public void stringToValue(String stringValue) throws ParseException {
		try {			
			this.value = Integer.valueOf(stringValue);
		} catch (NumberFormatException e) {
			throw new ParseException(this.getClass(), ERROR_MESSAGE);
		}
	}
}
