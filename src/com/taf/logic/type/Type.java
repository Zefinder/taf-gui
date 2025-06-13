package com.taf.logic.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.taf.logic.type.parameter.TypeParameter;
import com.taf.manager.ConstantManager;

public abstract class Type {

	// TODO Remove
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

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Type)) {
			return false;
		}

		Type other = (Type) obj;
		return this.getName().equals(other.getName()) && this.toString().equals(other.toString());
	}

	@Override
	public int hashCode() {
		return (this.getClass().toString() + ConstantManager.HASH_SEPARATOR + getName() + toString()).hashCode();
	}

}
