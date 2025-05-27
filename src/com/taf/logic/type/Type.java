package com.taf.logic.type;

import java.util.ArrayList;
import java.util.List;

import com.taf.logic.type.parameter.InstanceNumberParameter;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.manager.ConstantManager;

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
		final String separator = ConstantManager.ELEMENT_SEPARATOR;
		String typeStr = instanceNumber.toString();
		typeStr += separator + typeToString();

		for (TypeParameter typeParameter : parameterList) {
			typeStr += separator + typeParameter.toString();
		}

		return typeStr;
	}

}
