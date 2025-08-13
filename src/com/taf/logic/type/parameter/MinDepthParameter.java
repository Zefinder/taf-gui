package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class MinDepthParameter extends MinParameter {

	private static final String ERROR_MESSAGE = "Min depth number must be an integer!";

	public static final String PARAMETER_NAME = "min_depth";

	MinDepthParameter() {
		super(PARAMETER_NAME);
	}

	public MinDepthParameter(int value) {
		super(PARAMETER_NAME, value < 0 ? 0 : value, false);
	}

	@Override
	public void setValue(Number value) {
		super.setValue(value.intValue() < 0 ? 0 : value);
	}

	@Override
	public void stringToValue(String stringValue) throws ParseException {
		try {
			setValue(Integer.valueOf(stringValue));
		} catch (NumberFormatException e) {
			throw new ParseException(this.getClass(), ERROR_MESSAGE);
		}
	}

}
