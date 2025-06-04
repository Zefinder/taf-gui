package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class MaxRealParameter extends MaxParameter {

	public MaxRealParameter() {
		super();
	}
	
	public MaxRealParameter(Number value) {
		super(value, true);
	}
	
	@Override
	protected void valuefromString(String stringValue) throws ParseException {
		try {			
			this.value = Double.valueOf(stringValue);
		} catch (NumberFormatException e) {
			throw new ParseException("Max real number must be an integer or a real!");
		}
	}
}
