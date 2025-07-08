package com.taf.logic.type;

import java.util.HashSet;
import java.util.Set;

import com.taf.logic.type.parameter.TypeParameter;

public class DefaultFieldType extends FieldType {

	public DefaultFieldType() {
	}

	@Override
	public void addTypeParameter(TypeParameter typeParameter) {
		// No type parameters
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
	public String typeToString() {
		return "";
	}

}
