package com.taf.logic.type;

import com.taf.logic.type.parameter.TypeParameter;

public class AnonymousType  extends Type{

	public AnonymousType() {
	}

	@Override
	public boolean isAllowedTypeParameter(TypeParameter typeParameter) {
		return false;
	}

	@Override
	public String typeToString() {
		return "";
	}

}
