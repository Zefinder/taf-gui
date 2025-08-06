package com.taf.logic.type;

import java.util.Set;

import com.taf.logic.type.parameter.TypeParameter;
import com.taf.util.Consts;

public abstract class FieldType {

	public FieldType() {
	}

	public abstract void addTypeParameter(TypeParameter typeParameter);

	public abstract Set<String> getMandatoryParametersName();

	public abstract Set<String> getOptionalParametersName();

	public abstract String typeToString();

	public String getName() {
		return "";
	}

	@Override
	public String toString() {
		return typeToString();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FieldType)) {
			return false;
		}

		FieldType other = (FieldType) obj;
		return this.getName().equals(other.getName()) && this.toString().equals(other.toString());
	}

	@Override
	public int hashCode() {
		return (this.getClass().toString() + Consts.HASH_SEPARATOR + getName() + toString()).hashCode();
	}

}
