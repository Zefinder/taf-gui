package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class MinIntegerParameter extends MinParameter {
	
	public MinIntegerParameter() {
		super();
	}
	
	public MinIntegerParameter(Number value) {
		super(value, false);
	}
	
	@Override
	protected void valuefromString(String stringValue) throws ParseException {
		try {			
			this.value = Integer.valueOf(stringValue);
		} catch (NumberFormatException e) {
			throw new ParseException("Min integer number must be an integer!");
		}
	}
}
