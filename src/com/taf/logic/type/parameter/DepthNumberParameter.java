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
		setDepthNumber(depthNumber);
	}
	
	public void setDepthNumber(int depthNumber) {
		this.depthNumber = depthNumber < 0 ? 0 : depthNumber;
	}
	
	public int getDepthNumber() {
		return depthNumber;
	}
	
	@Override
	public void stringToValue(String stringValue) throws ParseException {
		try {			
			setDepthNumber(Integer.valueOf(stringValue));
		} catch (NumberFormatException e) {
			throw new ParseException(this.getClass(), ERROR_MESSAGE);
		}
	}

	@Override
	public String valueToString() {
		return String.valueOf(depthNumber);
	}
	

}
