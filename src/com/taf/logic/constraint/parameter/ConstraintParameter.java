package com.taf.logic.constraint.parameter;

import com.taf.manager.ConstantManager;

public abstract class ConstraintParameter {

	protected final String name;

	public ConstraintParameter(String name) {
		this.name = name;
	}

	public abstract String valueToString();

	@Override
	public String toString() {
		return ConstantManager.PARAMETER_STRING_FORMAT.formatted(name, valueToString());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ConstraintParameter)) {
			return false;
		}

		ConstraintParameter otherParameter = (ConstraintParameter) obj;
		return this.name.equals(otherParameter.name) && this.valueToString().equals(otherParameter.valueToString());
	}

	@Override
	public int hashCode() {
		return (this.getClass().toString() + ConstantManager.HASH_SEPARATOR + name + valueToString()).hashCode();
	}

}
