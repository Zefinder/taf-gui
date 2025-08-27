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
package com.taf.logic.type;

import com.taf.exception.EntityCreationException;
import com.taf.exception.ParseException;
import com.taf.logic.field.Parameter;
import com.taf.logic.type.parameter.TypeParameter;

/**
 * The ParameterTypeFactory class is a factory for creating {@link FieldType}
 * objects for {@link Parameter}s. It requires that the {@link FieldType} has at
 * least one constructor with no argument.
 *
 * @author Adrien Jakubiak
 */
public class ParameterTypeFactory {

	private static final String UNEXPECTED_VALUE_ERROR_MESSAGE = "Unexpected type name: ";

	/**
	 * Creates a new {@link TypeParameter} object for parameters.
	 *
	 * @param typeName the type name
	 * @return the field type
	 * @throws ParseException the parse exception
	 */
	public static FieldType createFieldType(String typeName) throws EntityCreationException {
		FieldType type = switch (typeName) {
		case IntegerType.TYPE_NAME:
			yield new IntegerType();

		case BooleanType.TYPE_NAME:
			yield new BooleanType();

		case RealType.TYPE_NAME:
			yield new RealType();

		case StringType.TYPE_NAME:
			yield new StringType();

		default:
			throw new EntityCreationException(ParameterTypeFactory.class, UNEXPECTED_VALUE_ERROR_MESSAGE);
		};

		return type;
	}

	private ParameterTypeFactory() {
	}

}
