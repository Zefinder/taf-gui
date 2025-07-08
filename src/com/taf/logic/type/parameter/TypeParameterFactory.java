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

	public static TypeParameter createTypeParameter(String typeName, String stringValue,
			MinMaxTypeParameterType minMaxTypeParameter) throws ParseException {
		TypeParameter type = switch (typeName) {

		case InstanceNumberParameter.PARAMETER_NAME: {
			yield new InstanceNumberParameter();
		}

		// Must be before MaxParameter since not the same name
		case MaxDepthParameter.PARAMETER_NAME: {
			yield new MaxDepthParameter();
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
				throw new ParseException(TypeParameterFactory.class, MAX_PARAMETER_ERROR_MESSAGE);
			}
		}

		// Must be before MinParameter since not the same name
		case MinDepthParameter.PARAMETER_NAME: {
			yield new MinDepthParameter();
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
				throw new ParseException(TypeParameterFactory.class, MIN_PARAMETER_ERROR_MESSAGE);
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

		case DistributionParameter.PARAMETER_NAME: {
			yield new DistributionParameter();
		}

		case MeanParameter.PARAMETER_NAME: {
			yield new MeanParameter();
		}

		case VarianceParameter.PARAMETER_NAME: {
			yield new VarianceParameter();
		}

		case RangesParameter.PARAMETER_NAME: {
			yield new RangesParameter();
		}
		
		case ReferenceParameter.PARAMETER_NAME: {
			yield new ReferenceParameter();
		}
		
		case DepthNumberParameter.PARAMETER_NAME: {
			yield new DepthNumberParameter();
		}

		default:
			throw new ParseException(TypeParameterFactory.class, UNEXPECTED_VALUE_ERROR_MESSAGE + typeName);
		};

		type.stringToValue(stringValue);

		return type;
	}

}
