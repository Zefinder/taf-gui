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

import java.text.DecimalFormat;

import com.taf.annotation.NotNull;
import com.taf.logic.type.NumericalType;
import com.taf.util.Consts;

/**
 * The MaxParameter parameter represents an lower bound for a numerical type. It
 * can be either an unsigned number, a signed number or a real number (depending
 * on the class extending it).
 * 
 * @see TypeParameter
 * @see NumericalType
 *
 * @author Adrien Jakubiak
 */
public abstract class MinParameter extends TypeParameter {

	/** The parameter name. */
	public static final String PARAMETER_NAME = "min";

	/** The real formatter. */
	private final DecimalFormat realFormatter = Consts.REAL_FORMATTER;

	/** The minimum value. */
	protected Number value;

	/** Represents a real minimum value (not an integer). */
	private boolean isReal;

	/**
	 * Instantiates a new minimum parameter with a value and if the value is a real
	 * number.
	 *
	 * @param name   the name
	 * @param value  the value
	 * @param isReal true if the value is a real number
	 */
	public MinParameter(String name, Number value, boolean isReal) {
		this(name);
		this.value = value;
		this.isReal = isReal;
	}

	/**
	 * Instantiates a new minimum parameter.
	 *
	 * @param name the name
	 */
	MinParameter(String name) {
		super(name);
	}

	/**
	 * Returns the value.
	 *
	 * @return the value
	 */
	public Number getValue() {
		return isReal ? value.doubleValue() : value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(Number value) {
		this.value = value;
	}

	@Override
	@NotNull
	public String valueToString() {
		return isReal ? realFormatter.format(value.doubleValue()) : String.valueOf(value.longValue());
	}

}
