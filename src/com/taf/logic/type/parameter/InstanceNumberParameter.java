package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class InstanceNumberParameter extends TypeParameter {

	private static final String ERROR_MESSAGE = "Instance number must be an integer!";
	
	public static final String PARAMETER_NAME = "nb_instances";
	
	private int instanceNumber;
	
	InstanceNumberParameter() {
		super(PARAMETER_NAME);
	}
	
	public InstanceNumberParameter(int instanceNumber) {
		this();
		this.instanceNumber = instanceNumber;
	}
	
	public void setInstanceNumber(int instanceNumber) {
		this.instanceNumber = instanceNumber;
	}
	
	public int getInstanceNumber() {
		return instanceNumber;
	}
	
	@Override
	public void valuefromString(String stringValue) throws ParseException {
		try {			
			this.instanceNumber = Integer.valueOf(stringValue);
		} catch (NumberFormatException e) {
			throw new ParseException(ERROR_MESSAGE);
		}
	}

	@Override
	public String valueToString() {
		return String.valueOf(instanceNumber);
	}
	
}
