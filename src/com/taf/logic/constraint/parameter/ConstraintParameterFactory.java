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
package com.taf.logic.constraint.parameter;

import com.taf.exception.ParseException;

/**
 * The ConstraintParameterFactory class is a factory for creating
 * {@link ConstraintParameter} objects. It requires that the
 * {@link ConstraintParameter} declares at least one constructor with no
 * argument.
 *
 * @author Adrien Jakubiak
 */
public class ConstraintParameterFactory {

	private static final String UNEXPECTED_VALUE_ERROR_MESSAGE = "Unexpected constraint parameter name: ";

	/**
	 * Creates a new {@link ConstraintParameter} object.
	 *
	 * @param name        the name
	 * @param stringValue the string value
	 * @return the constraint parameter
	 * @throws ParseException the parse exception
	 */
	public static ConstraintParameter createConstraintParameter(String name, String stringValue) throws ParseException {
		ConstraintParameter parameter = switch (name) {
		case ExpressionsConstraintParameter.CONSTRAINT_PARAMETER_NAME:
			yield new ExpressionsConstraintParameter();

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

	private ConstraintParameterFactory() {
	}

}
