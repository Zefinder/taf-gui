package com.taf.logic.constraint.parameter;

public class TypeConstraintParameter extends ConstraintParameter {

	private static final String CONSTRAINT_PARAMETER_NAME = "type";
	
	private TypeConstraintParameterEnum type;
	
	public TypeConstraintParameter(TypeConstraintParameterEnum type) {
		super(CONSTRAINT_PARAMETER_NAME);
		this.type = type;
	}

	public void setType(TypeConstraintParameterEnum type) {
		this.type = type;
	}
	
	@Override
	public String valueToString() {
		return type.getValue();
	}

}
