package com.taf.logic.constraint.parameter;

import com.taf.exception.ParseException;

public class ConstraintParameterFactory {

	private static final String UNEXPECTED_VALUE_ERROR_MESSAGE = "Unexpected constraint parameter name: ";
	
	private ConstraintParameterFactory() {
	}

	public static ConstraintParameter createConstraintParameter(String name, String stringValue) throws ParseException {
		ConstraintParameter parameter = switch (name) {
		case ExpressionConstraintParameter.CONSTRAINT_PARAMETER_NAME:
			yield new ExpressionConstraintParameter();

		case QuantifiersConstraintParameter.CONSTRAINT_PARAMETER_NAME:
			yield new QuantifiersConstraintParameter();

		case RangesConstraintParameter.CONSTRAINT_PARAMETER_NAME:
			yield new RangesConstraintParameter();

		case TypesConstraintParameter.CONSTRAINT_PARAMETER_NAME:
			yield new TypesConstraintParameter();

		default:
			throw new ParseException(ConstraintParameterFactory.class, UNEXPECTED_VALUE_ERROR_MESSAGE + name);
		};

		parameter.stringToValue(stringValue);
		return parameter;
	}

}
