package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class MaxIntegerParameter extends MaxParameter {

	public MaxIntegerParameter() {
		super();
	}
	
	public MaxIntegerParameter(Number value) {
		super(value, false);
	}
	
	@Override
	protected void valuefromString(String stringValue) throws ParseException {
		try {			
			this.value = Integer.valueOf(stringValue);
		} catch (NumberFormatException e) {
			throw new ParseException("Max integer number must be an integer!");
		}
	}
}
