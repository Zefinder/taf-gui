package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class TypeNameParameter extends TypeParameter {

	private static final String PARAMETER_NAME = "type";
	
	private String typeName;
	
	public TypeNameParameter() {
		super(PARAMETER_NAME);
	}
	
	public TypeNameParameter(String typeName) {
		this();
		this.typeName = typeName;
	}
	
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	@Override
	protected void valuefromString(String stringValue) throws ParseException {
		this.typeName = stringValue;
	}

	@Override
	public String valueToString() {
		return typeName;
	}

}
