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
package com.taf.logic.field;

import com.taf.logic.type.BooleanType;
import com.taf.logic.type.FieldType;
import com.taf.logic.type.IntegerType;
import com.taf.logic.type.RealType;
import com.taf.logic.type.StringType;
import com.taf.logic.type.parameter.TypeParameter;

/**
 * <p>
 * A Parameter is a TAF entity which represents a value. A value has a type, and
 * different parameters depending on its type (see {@link TypeParameter}).
 * </p>
 * 
 * <p>
 * The different types a parameter can take are the following:
 * <ul>
 * <li>{@link IntegerType} to represent an integer parameter
 * <li>{@link RealType} to represent a float parameter
 * <li>{@link StringType} to represent a string parameter
 * <li>{@link BooleanType} to represent a boolean parameter
 * </ul>
 * </p>
 * 
 * @see Field
 * @see TypeParameter
 * 
 * @author Adrien Jakubiak
 */
public class Parameter extends Field {

	/** Used to format a parameter to a XML string representation. */
	private static final String PARAMETER_STRING_FORMAT = "<parameter %s/>";

	/**
	 * Instantiates a new parameter with a specified field type.
	 *
	 * @param name the parameter name
	 * @param type the parameter type
	 */
	public Parameter(String name, FieldType type) {
		super(name, type);
	}

	@Override
	public String getEntityTypeName() {
		return getType().getName();
	}

	@Override
	public String toString() {
		return PARAMETER_STRING_FORMAT.formatted(super.toString());
	}

}
