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

import com.taf.annotation.NotNull;
import com.taf.exception.ParseException;
import com.taf.logic.type.NumericalType;
import com.taf.util.Consts;

/**
 * The VarianceParameter parameter represents the variance in a normal
 * distribution. This is used in the {@link DistributionParameter}, and is a
 * real value.
 * 
 * @see TypeParameter
 * @see DistributionParameter
 * @see NumericalType
 *
 * @author Adrien Jakubiak
 */
public class VarianceParameter extends TypeParameter {

	private static final String ERROR_MESSAGE = "Variance value must be an integer or a real!";

	/** The parameter. */
	public static final String PARAMETER_NAME = "variance";

	/** The variance. */
	private double variance;

	/**
	 * Instantiates a new variance parameter with a value.
	 *
	 * @param variance the variance
	 */
	public VarianceParameter(double variance) {
		this();
		this.variance = variance;
	}

	/**
	 * Instantiates a new variance parameter.
	 */
	VarianceParameter() {
		super(PARAMETER_NAME);
	}

	/**
	 * Returns the variance.
	 *
	 * @return the variance
	 */
	public double getVariance() {
		return variance;
	}

	/**
	 * Sets the variance.
	 *
	 * @param variance the new variance
	 */
	public void setVariance(double variance) {
		this.variance = variance;
	}

	@Override
	public void stringToValue(String stringValue) throws ParseException {
		try {
			setVariance(Double.valueOf(stringValue));
		} catch (NumberFormatException | NullPointerException e) {
			throw new ParseException(this.getClass(), ERROR_MESSAGE);
		}
	}

	@Override
	@NotNull
	public String valueToString() {
		return Consts.REAL_FORMATTER.format(variance);
	}

}
