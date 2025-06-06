package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class MaxInstanceParameter extends MaxParameter {

	public static final String PARAMETER_NAME = "max";
	
	MaxInstanceParameter() {
		super(PARAMETER_NAME);
	}
	
	public MaxInstanceParameter(Integer value) {
		super(PARAMETER_NAME, value, false);
	}

	@Override
	public void valuefromString(String stringValue) throws ParseException {
		try {			
			this.value = Integer.valueOf(stringValue);
		} catch (NumberFormatException e) {
			throw new ParseException("Max instance number must be an integer!");
		}
	}

}
