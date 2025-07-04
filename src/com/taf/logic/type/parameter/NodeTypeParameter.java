package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class NodeTypeParameter extends TypeParameter {

	public static final String PARAMETER_NAME = "type";
	
	private String typeName; 
	
	public NodeTypeParameter() {
		super(PARAMETER_NAME);
		typeName = "";
	}
	
	public NodeTypeParameter(String typeName) {
		this();
		this.typeName = typeName;
	}

	@Override
	public String valueToString() {
		return typeName;
	}

	@Override
	void stringToValue(String stringValue) throws ParseException {
		
	}

}
