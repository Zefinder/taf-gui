package com.taf.logic.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.taf.logic.type.parameter.TypeParameter;
import com.taf.manager.ConstantManager;

public abstract class Type {

	// List for custom types!
	private List<TypeParameter> parameterList;

	public Type() {
		this.parameterList = new ArrayList<TypeParameter>();
	}
	
	public abstract void addTypeParameter(TypeParameter typeParameter); 

	public void removeTypeParameter(int index) {
		parameterList.remove(index);
	}

	public abstract Set<String> getMandatoryParametersName();
	
	public abstract Set<String> getOptionalParametersName();
	
	// TODO Remove this method
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
