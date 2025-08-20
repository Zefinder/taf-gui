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

/**
 * The QuantifierType enumeration gives information on the type of a quantifier.
 * A quantifier can have two types: <code>for all</code> or
 * <code>it exists</code>.
 *
 * @see QuantifiersConstraintParameter
 *
 * @author Adrien Jakubiak
 */
public enum QuantifierType {

	/** The <code>for all</code> type. */
	FORALL("forall"),
	/** The <code>it exists</code> type. */
	EXISTS("exists");

	/**
	 * Returns the {@link QuantifierType} associated to the string representation
	 * given. The default value is {@link QuantifierType#FORALL}
	 *
	 * @param value the value
	 * @return the quantifier type
	 */
	public static QuantifierType fromString(String value) {
		if (value.equals(EXISTS.getValue())) {
			return EXISTS;
		}

		return FORALL;
	}

	/** The string representation of the type. */
	private final String value;

	/**
	 * Instantiates a new quantifier type.
	 *
	 * @param value the value
	 */
	private QuantifierType(String value) {
		this.value = value;
	}

	/**
	 * Returns the type value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}

}
