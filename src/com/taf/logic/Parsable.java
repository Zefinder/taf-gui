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
package com.taf.logic;

import com.taf.annotation.NotNull;
import com.taf.exception.ParseException;
import com.taf.logic.type.parameter.TypeParameter;

/**
 * Interface that represent any parsable object. It contains two methods:
 * <ul>
 * <li>{@link #stringToValue(String)} which parses a string to a value. It has
 * the same meaning as the <code>valueOf</code> method of Integer or Double for
 * example.
 * <li>{@link #valueToString()} which parses the object value to its string
 * representation. This method differs from the {@link #toString()} method since
 * it parses a value and does not turn an object into a String form. see
 * {@link TypeParameter} for a concrete example.
 * </ul>
 *
 * @author Adrien Jakubiak
 */
public interface Parsable {

	/**
	 * <p>
	 * Parses a string into a meaningful value for the parsable object.
	 * </p>
	 * 
	 * <p>
	 * For example, take a parsable object with value of type <code>int[]</code>.
	 * This method would take a string representation of an integer array and parse
	 * it into an actual integer array.
	 * </p>
	 * 
	 * @param stringValue the string representation of a value
	 * @throws ParseException if the string representation does not correspond to
	 *                        the value type or style
	 */
	void stringToValue(String stringValue) throws ParseException;

	/**
	 * <p>
	 * Parses the value of the Parsable into a string representation. This is NOT a
	 * {@link #toString()} replacement, it has a different meaning: it does not
	 * describe the object but only its value representation.
	 * </p>
	 * 
	 * <p>
	 * For example a parsable {@link Entity} has a name, a parent and could have
	 * another meaningful value of type <code>int[]</code>. This method would give
	 * the string representation of the integer array and not describe the whole
	 * entity with the name and the parent.
	 * </p>
	 * 
	 * @return the string representation of the value
	 */
	@NotNull
	String valueToString();

}
