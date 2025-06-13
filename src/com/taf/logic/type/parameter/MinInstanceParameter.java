package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class MinInstanceParameter extends MinParameter {

	private static final String ERROR_MESSAGE = "Min instance number must be an integer!";
	
	public static final String PARAMETER_NAME = "min";
	
	MinInstanceParameter() {
		super(PARAMETER_NAME);
	}

	public MinInstanceParameter(Integer value) {
		super(PARAMETER_NAME, value, false);
	}

	@Override
	void stringToValue(String stringValue) throws ParseException {
		try {
			this.value = Integer.valueOf(stringValue);
		} catch (NumberFormatException e) {
			throw new ParseException(ERROR_MESSAGE);
		}
	}
}
