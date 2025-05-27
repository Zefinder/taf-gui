package com.taf.logic.type.parameter;

public class TypeNameParameter extends TypeParameter {

	private static final String PARAMETER_NAME = "type";
	
	private String typeName;
	
	public TypeNameParameter(String typeName) {
		super(PARAMETER_NAME);
		this.typeName = typeName;
	}
	
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String valueToString() {
		return typeName;
	}

}
