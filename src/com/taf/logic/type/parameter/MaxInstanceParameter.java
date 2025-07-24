package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class MaxInstanceParameter extends MaxParameter {

	private static final String ERROR_MESSAGE = "Max instance number must be an integer!";
	
	public static final String PARAMETER_NAME = "max";
	
	MaxInstanceParameter() {
		super(PARAMETER_NAME);
	}
	
	public MaxInstanceParameter(Integer value) {
		super(PARAMETER_NAME, value, false);
	}
	
	@Override
	public void setValue(Number value) {
		super.setValue(value.intValue() < 0 ? 0 : value);
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
