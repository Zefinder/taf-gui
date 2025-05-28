package com.taf.logic.type;

import java.util.ArrayList;
import java.util.List;

import com.taf.logic.type.parameter.TypeParameter;
import com.taf.manager.ConstantManager;

public abstract class Type {

	// List for custom types!
	private List<TypeParameter> parameterList;

	public Type() {
		this.parameterList = new ArrayList<TypeParameter>();
	}

	protected void addTypeParameter(TypeParameter typeParameter) {
		if (isAllowedTypeParameter(typeParameter)) {
			parameterList.add(typeParameter);
		}
	}

	public void removeTypeParameter(int index) {
		parameterList.remove(index);
	}

	public abstract boolean isAllowedTypeParameter(TypeParameter typeParameter);

	public abstract String typeToString();
	
	public String getName() {
		return "";
	}

	@Override
	public String toString() {
		final String separator = ConstantManager.PARAMETER_SEPARATOR;
		String typeStr = typeToString();

		for (TypeParameter typeParameter : parameterList) {
			typeStr += separator + typeParameter.toString();
		}

		return typeStr;
	}

}
