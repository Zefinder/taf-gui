package com.taf.logic.type;

import com.taf.logic.type.parameter.TypeNameParameter;
import com.taf.logic.type.parameter.TypeParameter;

public class BooleanType extends Type {
	
	private static final String TYPE_NAME = "boolean";

	private TypeNameParameter typeName;
	
	public BooleanType() {
		typeName = new TypeNameParameter(TYPE_NAME);
	}

	@Override
	public boolean isAllowedTypeParameter(TypeParameter typeParameter) {
		return false;
	}

	@Override
	public String typeToString() {
		return typeName.toString();
	}

}
