package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;
import com.taf.manager.ConstantManager;

/**
 * Class that represents a type parameter. Every class that extends it must
 * contain an empty constructor.
 */
public abstract class TypeParameter {

	protected final String name;

	public TypeParameter(String name) {
		this.name = name;
	}

	public abstract String valueToString();

	abstract void stringToValue(String stringValue) throws ParseException;

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
		return (this.getClass().toString() + ConstantManager.HASH_SEPARATOR + name + valueToString()).hashCode();
	}

}
