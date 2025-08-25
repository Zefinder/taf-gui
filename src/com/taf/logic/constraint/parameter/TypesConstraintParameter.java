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

import java.util.ArrayList;
import java.util.List;

import com.taf.annotation.FactoryObject;
import com.taf.annotation.NotNull;
import com.taf.exception.ParseException;
import com.taf.util.Consts;

/**
 * The TypesConstraintParameter parameter represents a list of
 * {@link QuantifierType} that describe the type of a quantifier
 * 
 * @see ConstraintParameter
 * @see QuantifierType
 *
 * @author Adrien Jakubiak
 */
@FactoryObject(types = {}, generate = true)
public class TypesConstraintParameter extends ConstraintParameter {

	/** The parameter name. */
	static final String CONSTRAINT_PARAMETER_NAME = "types";

	private static final String NULL_ERROR_MESSAGE = "Quantifier types must not be null!";

	/** The list of types. */
	private List<QuantifierType> types;

	/**
	 * Instantiates a new types constraint parameter.
	 */
	public TypesConstraintParameter() {
		super(CONSTRAINT_PARAMETER_NAME);
		types = new ArrayList<QuantifierType>();
	}

	/**
	 * Adds a type.
	 *
	 * @param type the type
	 */
	public void addType(QuantifierType type) {
		types.add(type);
	}

	/**
	 * Edits the type at the specified index.
	 *
	 * @param index the index
	 * @param type  the type
	 */
	public void editType(int index, QuantifierType type) {
		types.set(index, type);
	}

	/**
	 * Returns the list of types.
	 *
	 * @return the types
	 */
	public List<QuantifierType> getTypes() {
		return types;
	}

	/**
	 * Removes the type at the specified index.
	 *
	 * @param index the index
	 */
	public void removeType(int index) {
		types.remove(index);
	}

	@Override
	public void stringToValue(String stringValue) throws ParseException {
		if (stringValue == null) {
			throw new ParseException(this.getClass(), NULL_ERROR_MESSAGE);
		}
		if (stringValue.isBlank()) {
			return;
		}

		final String separator = Consts.ELEMENT_SEPARATOR;
		String[] values = stringValue.split(separator);
		types.clear();

		for (String value : values) {
			if (!value.isBlank()) {
				QuantifierType type = QuantifierType.fromString(value);
				addType(type);
			}
		}
	}

	@Override
	@NotNull
	public String valueToString() {
		if (types.isEmpty()) {
			return "";
		}

		final String separator = Consts.ELEMENT_SEPARATOR;
		String res = types.get(0).getValue();
		for (int i = 1; i < types.size(); i++) {
			res += separator + types.get(i).getValue();
		}

		return res;
	}

}
