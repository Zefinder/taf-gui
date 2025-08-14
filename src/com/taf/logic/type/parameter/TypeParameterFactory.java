/*
 * Copyright or © or Copr.
 * 
 * This software is a computer program whose purpose is to generate random test
 * case from a template file describing the data model.
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software. You can use, modify
 * and/or redistribute the software under the terms of the CeCILL-B license as
 * circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * 
 * As a counterpart to the access to the source code and rights to copy, modify
 * and redistribute granted by the license, users are provided only with a
 * limited warranty and the software's author, the holder of the economic
 * rights, and the successive licensors have only limited liability.
 * 
 * In this respect, the user's attention is drawn to the risks associated with
 * loading, using, modifying and/or developing or reproducing the software by
 * the user in light of its specific status of free software, that may mean that
 * it is complicated to manipulate, and that also therefore means that it is
 * reserved for developers and experienced professionals having in-depth
 * computer knowledge. Users are therefore encouraged to load and test the
 * software's suitability as regards their requirements in conditions enabling
 * the security of their systems and/or data to be ensured and, more generally,
 * to use and operate it in the same conditions as regards security.
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

/**
 * The TypeParameterFactory class is a factory for creating
 * {@link TypeParameter} objects. It requires that the {@link TypeParameter} has
 * at least one constructor with no argument.
 *
 * @see TypeParameter
 *
 * @author Adrien Jakubiak
 */
public class TypeParameterFactory {

	private static final String MAX_PARAMETER_ERROR_MESSAGE = "The parameter type for max must be different from NONE";

	private static final String MIN_PARAMETER_ERROR_MESSAGE = "The parameter type for min must be different from NONE";

	private static final String UNEXPECTED_VALUE_ERROR_MESSAGE = "Unexpected type name: ";

	/**
	 * Creates a new TypeParameter object with {@link MinMaxTypeParameterType#NONE}.
	 *
	 * @param typeName    the type name
	 * @param stringValue the string value
	 * @return the type parameter
	 * @throws ParseException the parse exception
	 */
	public static TypeParameter createTypeParameter(String typeName, String stringValue) throws ParseException {
		return createTypeParameter(typeName, stringValue, MinMaxTypeParameterType.NONE);
	}

	/**
	 * Creates a new TypeParameter object.
	 *
	 * @param typeName            the type name
	 * @param stringValue         the string value
	 * @param minMaxTypeParameter the min max type parameter
	 * @return the type parameter
	 * @throws ParseException the parse exception
	 */
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

	/**
	 * Instantiates a new type parameter factory.
	 */
	private TypeParameterFactory() {
	}

	/**
	 * The MinMaxTypeParameterType enumeration indicated which parameter to use when
	 * a {@link MinParameter} or a {@link MaxParameter} needs to be created.
	 *
	 * @author Adrien Jakubiak
	 */
	public enum MinMaxTypeParameterType {

		/** Integer type parameter. */
		INTEGER,
		/** Real type parameter. */
		REAL,
		/** Instance type parameter. */
		INSTANCE,
		/** None. */
		NONE;
	}

}
