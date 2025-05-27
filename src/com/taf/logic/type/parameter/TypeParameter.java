package com.taf.logic.type.parameter;

import com.taf.manager.ConstantManager;

public abstract class TypeParameter {

	private static final String HASH_SEPARATOR = ":";
	
	protected final String name;

	public TypeParameter(String name) {
		this.name = name;
	}

	public abstract String valueToString();

	@Override
	public String toString() {
		return ConstantManager.PARAMETER_STRING_FORMAT.formatted(name, valueToString());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TypeParameter)) {
			return false;
		}

		TypeParameter otherParameter = (TypeParameter) obj;
		return this.name.equals(otherParameter.name) && this.valueToString().equals(otherParameter.valueToString());
	}
	
	@Override
	public int hashCode() {
		return (this.getClass().toString() + HASH_SEPARATOR + name + valueToString()).hashCode();
	}

}
