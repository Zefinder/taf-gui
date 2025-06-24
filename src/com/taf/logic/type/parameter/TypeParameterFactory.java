package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class TypeParameterFactory {
	
	private static final String MAX_PARAMETER_ERROR_MESSAGE = "The parameter type for max must be different from NONE";
	private static final String MIN_PARAMETER_ERROR_MESSAGE = "The parameter type for min must be different from NONE";
	private static final String UNEXPECTED_VALUE_ERROR_MESSAGE = "Unexpected type name: ";

	public enum MinMaxTypeParameterType {
		INTEGER, REAL, INSTANCE, NONE;
	}

	private TypeParameterFactory() {
	}

	public static TypeParameter createTypeParameter(String typeName, String stringValue) throws ParseException {
		return createTypeParameter(typeName, stringValue, MinMaxTypeParameterType.NONE);
	}

	public static TypeParameter createTypeParameter(String typeName, String stringValue, MinMaxTypeParameterType minMaxTypeParameter)
			throws ParseException {
		TypeParameter type = switch (typeName) {

		case InstanceNumberParameter.PARAMETER_NAME: {
			yield new InstanceNumberParameter();
		}

		case MaxParameter.PARAMETER_NAME: {
			switch (minMaxTypeParameter) {
			case INTEGER:
				yield new MaxIntegerParameter();

			case REAL:
				yield new MaxRealParameter();

			case INSTANCE:
				yield new MaxInstanceParameter();

			default:
				throw new ParseException(MAX_PARAMETER_ERROR_MESSAGE);
			}
		}

		case MinParameter.PARAMETER_NAME: {
			switch (minMaxTypeParameter) {
			case INTEGER:
				yield new MinIntegerParameter();

			case REAL:
				yield new MinRealParameter();

			case INSTANCE:
				yield new MinInstanceParameter();

			default:
				throw new ParseException(MIN_PARAMETER_ERROR_MESSAGE);
			}
		}

		case TypeNameParameter.PARAMETER_NAME: {
			yield new TypeNameParameter();
		}

		case ValuesParameter.PARAMETER_NAME: {
			yield new ValuesParameter();
		}

		case WeightsParameter.PARAMETER_NAME: {
			yield new WeightsParameter();
		}

		default:
			throw new ParseException(UNEXPECTED_VALUE_ERROR_MESSAGE + typeName);
		};
		
		type.stringToValue(stringValue);

		return type;
	}

}
