package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class MinRealParameter extends MinParameter {
	
	private static final String ERROR_MESSAGE = "Min real number must be an integer or a real!";
	
	public static final String PARAMETER_NAME = "min";
	
	MinRealParameter() {
		super(PARAMETER_NAME);
	}
	
	public MinRealParameter(Number value) {
		super(PARAMETER_NAME, value, true);
	}
	
	@Override
	void stringToValue(String stringValue) throws ParseException {
		try {			
			this.value = Double.valueOf(stringValue);
		} catch (NumberFormatException e) {
			throw new ParseException(ERROR_MESSAGE);
		}
	}
}
