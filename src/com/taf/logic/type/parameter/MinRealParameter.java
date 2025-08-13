package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class MinRealParameter extends MinParameter {
	
	private static final String ERROR_MESSAGE = "Min real number must be an integer or a real!";
	
	public static final String PARAMETER_NAME = "min";
	
	MinRealParameter() {
		super(PARAMETER_NAME);
	}
	
	public MinRealParameter(double value) {
		super(PARAMETER_NAME, value, true);
	}
	
	@Override
	public void stringToValue(String stringValue) throws ParseException {
		try {
			setValue(Double.valueOf(stringValue));
		} catch (NumberFormatException | NullPointerException e) {
			throw new ParseException(this.getClass(), ERROR_MESSAGE);
		}
	}
}
