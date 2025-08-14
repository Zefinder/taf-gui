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
 * The DistributionType enumeration gives all distribution possibilities for the
 * {@link DistributionParameter}.
 * 
 * @see DistributionParameter
 * 
 * @author Adrien Jakubiak
 */
public enum DistributionType {

	/** The uniform distribution. */
	UNIFORM("u"),
	/** The normal distribution. */
	NORMAL("n"),
	/** The interval distribution. */
	INTERVAL("i");

	private static final String NULL_ERROR_MESSAGE = "Distribution type must not be null!";

	/**
	 * Returns the DistributionType associated to the given string representation,
	 * with {@link #UNIFORM} as default value.
	 *
	 * @param distributionString the distribution string
	 * @return the distribution type associated to the distribution string
	 * @throws ParseException if the distribution string is null
	 */
	public static DistributionType fromDistributionString(String distributionString) throws ParseException {
		if (distributionString == null) {
			throw new ParseException(DistributionType.class, NULL_ERROR_MESSAGE);
		}

		return switch (distributionString) {
		case "u":
			yield UNIFORM;

		case "n":
			yield NORMAL;

		case "i":
			yield INTERVAL;

		default:
			yield UNIFORM;
		};
	}

	/** The distribution string representation. */
	private String distributionString;

	/**
	 * Instantiates a new distribution type.
	 *
	 * @param distributionString the distribution string
	 */
	private DistributionType(String distributionString) {
		this.distributionString = distributionString;
	}

	/**
	 * Returns the distribution string.
	 *
	 * @return the distribution string
	 */
	public String getDistributionString() {
		return distributionString;
	}

}
