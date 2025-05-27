package com.taf.logic.constraint.parameter;

public class QuantifiersConstraintParameter extends ConstraintParameter {

	// We assume that there can only be one quantifier... 
	
	private static final String CONSTRAINT_PARAMETER_NAME = "quantifiers";
	
	private char quantifier; 
	
	public QuantifiersConstraintParameter(char quantifier) {
		super(CONSTRAINT_PARAMETER_NAME);
		this.quantifier = quantifier;
	}

	@Override
	public String valueToString() {
		return String.valueOf(quantifier);
	}

}
