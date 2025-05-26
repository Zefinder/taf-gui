package com.taf.logic.type;

import java.util.ArrayList;
import java.util.List;

import com.taf.logic.type.parameter.InstanceNumberParameter;
import com.taf.logic.type.parameter.TypeParameter;

public abstract class Type {

	private InstanceNumberParameter instanceNumber;

	private List<TypeParameter> parameterList;

	public Type() {
		this.instanceNumber = new InstanceNumberParameter(1);
		this.parameterList = new ArrayList<TypeParameter>();
	}

	public void editInstanceNumber(int number) {
		instanceNumber.setInstanceNumber(number);
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

	@Override
	public String toString() {
		String typeStr = instanceNumber.toString();
		typeStr += " " + typeToString();

		for (TypeParameter typeParameter : parameterList) {
			typeStr += " " + typeParameter.toString();
		}

		return typeStr;
	}

}
