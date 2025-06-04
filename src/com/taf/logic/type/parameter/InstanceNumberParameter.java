package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class InstanceNumberParameter extends TypeParameter {

	private static final String PARAMETER_NAME = "nb_instances";
	
	private int instanceNumber;
	
	public InstanceNumberParameter() {
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
	protected void valuefromString(String stringValue) throws ParseException {
		try {			
			this.instanceNumber = Integer.valueOf(stringValue);
		} catch (NumberFormatException e) {
			throw new ParseException("Instance number must be an integer!");
		}
	}

	@Override
	public String valueToString() {
		return String.valueOf(instanceNumber);
	}
	
}
