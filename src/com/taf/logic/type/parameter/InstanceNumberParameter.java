package com.taf.logic.type.parameter;

public class InstanceNumberParameter extends TypeParameter {

	private static final String PARAMETER_NAME = "nb_instances";
	
	private int instanceNumber;
	
	public InstanceNumberParameter(int instanceNumber) {
		super(PARAMETER_NAME);
		this.instanceNumber = instanceNumber;
	}
	
	public void setInstanceNumber(int instanceNumber) {
		this.instanceNumber = instanceNumber;
	}

	@Override
	public String valueToString() {
		return String.valueOf(instanceNumber);
	}
	
}
