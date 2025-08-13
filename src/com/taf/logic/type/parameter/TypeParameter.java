package com.taf.logic.type.parameter;

import com.taf.logic.Parsable;
import com.taf.util.Consts;

/**
 * Class that represents a type parameter. Every class that extends it must
 * contain an empty constructor.
 */
public abstract class TypeParameter implements Parsable {

	protected final String name;

	public TypeParameter(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return Consts.formatParameter(name, valueToString());
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
		return (this.getClass().toString() + Consts.HASH_SEPARATOR + name + valueToString()).hashCode();
	}

}
