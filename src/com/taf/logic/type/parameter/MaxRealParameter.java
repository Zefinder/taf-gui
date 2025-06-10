package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class MaxRealParameter extends MaxParameter {

	public static final String PARAMETER_NAME = "max";
	
	MaxRealParameter() {
		super(PARAMETER_NAME);
	}
	
	public MaxRealParameter(Number value) {
		super(PARAMETER_NAME, value, true);
	}
	
	@Override
	public void valuefromString(String stringValue) throws ParseException {
		try {			
			this.value = Double.valueOf(stringValue);
		} catch (NumberFormatException e) {
			throw new ParseException("Max real number must be an integer or a real!");
		}
	}
}
