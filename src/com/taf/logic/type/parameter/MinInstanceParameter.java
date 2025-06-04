package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class MinInstanceParameter extends MinParameter {

	public MinInstanceParameter() {
		super();
	}

	public MinInstanceParameter(Integer value) {
		super(value, false);
	}

	@Override
	protected void valuefromString(String stringValue) throws ParseException {
		try {
			this.value = Integer.valueOf(stringValue);
		} catch (NumberFormatException e) {
			throw new ParseException("Min instance number must be an integer!");
		}
	}
}
