package com.taf.logic.constraint.parameter;

import java.util.HashSet;

import com.taf.manager.ConstantManager;
import com.taf.util.HashSetBuilder;

public abstract class ConstraintParameter {

	private static final HashSet<String> CONSTRAINT_PARAMETER_NAMES = new HashSetBuilder<String>()
			.add(ExpressionConstraintParameter.CONSTRAINT_PARAMETER_NAME)
			.add(QuantifiersConstraintParameter.CONSTRAINT_PARAMETER_NAME)
			.add(RangesConstraintParameter.CONSTRAINT_PARAMETER_NAME)
			.add(TypesConstraintParameter.CONSTRAINT_PARAMETER_NAME)
			.build();

	protected final String name;

	public ConstraintParameter(String name) {
		this.name = name;
	}

	public abstract String valueToString();
	
	abstract void stringToValue(String stringValue);
	
	public static HashSet<String> getConstraintParameterNames() {
		return CONSTRAINT_PARAMETER_NAMES;
	}

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
