package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class MinRealParameter extends MinParameter {
	
	public MinRealParameter() {
		super();
	}
	
	public MinRealParameter(Number value) {
		super(value, true);
	}
	
	@Override
	protected void valuefromString(String stringValue) throws ParseException {
		try {			
			this.value = Double.valueOf(stringValue);
		} catch (NumberFormatException e) {
			throw new ParseException("Min real number must be an integer or a real!");
		}
	}
}
