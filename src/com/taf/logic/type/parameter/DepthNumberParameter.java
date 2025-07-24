package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class DepthNumberParameter extends TypeParameter {

	private static final String ERROR_MESSAGE = "Depth number must be an integer!";
	
	public static final String PARAMETER_NAME = "depth";
	
	private int depthNumber;
	
	DepthNumberParameter() {
		super(PARAMETER_NAME);
	}
	
	public DepthNumberParameter(int depthNumber) {
		this();
		this.depthNumber = depthNumber;
	}
	
	public void setDepthNumber(int depthNumber) {
		this.depthNumber = depthNumber;
	}
	
	public int getDepthNumber() {
		return depthNumber;
	}
	
	@Override
	void stringToValue(String stringValue) throws ParseException {
		try {			
			this.depthNumber = Integer.valueOf(stringValue);
		} catch (NumberFormatException e) {
			throw new ParseException(this.getClass(), ERROR_MESSAGE);
		}
	}

	@Override
	public String valueToString() {
		return String.valueOf(depthNumber);
	}
	

}
