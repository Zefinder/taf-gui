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

import java.util.HashSet;

import com.taf.logic.Parsable;
import com.taf.logic.constraint.Constraint;
import com.taf.util.Consts;
import com.taf.util.HashSetBuilder;

/**
 * <p>
 * The ConstraintParameter class represents a parameter that can be added to a
 * {@link Constraint}. It implements {@link Parsable}, meaning that a string can
 * be parsed into a constraint parameter (and a constraint parameter into a
 * string)
 * </p>
 * 
 * <p>
 * Type parameters are created using the {@link ConstraintParameterFactory}
 * class, which requires at least one constructor with no argument.
 * </p>
 *
 * @see Parsable
 * @see ConstraintParameterFactory
 * @see Constraint
 *
 * @author Adrien Jakubiak
 */
public abstract class ConstraintParameter implements Parsable {

	/** The set of constraint parameters' name. */
	private static final HashSet<String> CONSTRAINT_PARAMETER_NAMES = new HashSetBuilder<String>()
			.add(ExpressionsConstraintParameter.CONSTRAINT_PARAMETER_NAME)
			.add(QuantifiersConstraintParameter.CONSTRAINT_PARAMETER_NAME)
			.add(RangesConstraintParameter.CONSTRAINT_PARAMETER_NAME)
			.add(TypesConstraintParameter.CONSTRAINT_PARAMETER_NAME)
			.build();

	/**
	 * Returns the set of constraint parameter names.
	 *
	 * @return the set constraint parameter names
	 */
	public static HashSet<String> getConstraintParameterNames() {
		return CONSTRAINT_PARAMETER_NAMES;
	}

	/** The parameter name. */
	protected final String name;

	/**
	 * Instantiates a new constraint parameter.
	 *
	 * @param name the name
	 */
	public ConstraintParameter(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ConstraintParameter)) {
			return false;
		}

		ConstraintParameter otherParameter = (ConstraintParameter) obj;
		return this.name.equals(otherParameter.name) && this.valueToString().equals(otherParameter.valueToString());
	}

	@Override
	public int hashCode() {
		return (this.getClass().toString() + Consts.HASH_SEPARATOR + name + valueToString()).hashCode();
	}

	@Override
	public String toString() {
		return Consts.formatParameter(name, valueToString());
	}

}
