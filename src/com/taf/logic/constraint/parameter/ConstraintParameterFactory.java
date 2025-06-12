package com.taf.logic.constraint.parameter;

public class ConstraintParameterFactory {

	private ConstraintParameterFactory() {
	}

	public ConstraintParameter createConstraintParameter(String name) {
		switch (name) {
		case ExpressionConstraintParameter.CONSTRAINT_PARAMETER_NAME:
			return new ExpressionConstraintParameter();

		case QuantifiersConstraintParameter.CONSTRAINT_PARAMETER_NAME:
			return new QuantifiersConstraintParameter();

		case RangesConstraintParameter.CONSTRAINT_PARAMETER_NAME:
			return new RangesConstraintParameter();

		case TypesConstraintParameter.CONSTRAINT_PARAMETER_NAME:
			return new TypesConstraintParameter();

		default:
			return null;
		}
	}

}
