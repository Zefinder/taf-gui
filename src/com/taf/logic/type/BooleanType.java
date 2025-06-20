package com.taf.logic.type;

import java.util.HashSet;
import java.util.Set;

import com.taf.logic.type.parameter.TypeNameParameter;
import com.taf.logic.type.parameter.TypeParameter;

public class BooleanType extends Type {
	
	public static final String TYPE_NAME = "boolean";

	private TypeNameParameter typeName;
	
	public BooleanType() {
		typeName = new TypeNameParameter(TYPE_NAME);
	}
	
	@Override
	public void addTypeParameter(TypeParameter typeParameter) {
	}
	
	@Override
	public Set<String> getMandatoryParametersName() {
		return new HashSet<String>();
	}
	
	@Override
	public Set<String> getOptionalParametersName() {
		return new HashSet<String>();
	}

	@Override
	public boolean isAllowedTypeParameter(TypeParameter typeParameter) {
		return false;
	}
	
	@Override
	public String getName() {
		return TYPE_NAME;
	}

	@Override
	public String typeToString() {
		return typeName.toString();
	}

}
