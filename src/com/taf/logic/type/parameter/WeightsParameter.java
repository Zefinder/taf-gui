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

import java.util.ArrayList;
import java.util.List;

import com.taf.annotation.FactoryObject;
import com.taf.annotation.NotNull;
import com.taf.exception.ParseException;
import com.taf.logic.type.NumericalType;
import com.taf.logic.type.StringType;
import com.taf.util.Consts;

/**
 * The WeightsParameter parameter represents a list of weights for a random
 * choice. This can be used in {@link StringType} to set a selection weight in a
 * list of string values, or in a {@link NumericalType} with an interval
 * distribution to set a selection weight in a list of ranges.
 * 
 * @see TypeParameter
 * @see ValuesParameter
 * @see DistributionParameter
 *
 * @author Adrien Jakubiak
 */
@FactoryObject(types = {}, generate = false)
public class WeightsParameter extends TypeParameter {

	private static final String NULL_ERROR_MESSAGE = "Weights must not be null!";

	private static final String ERROR_MESSAGE = "Weight value must be an integer!";

	/** The parameter name. */
	public static final String PARAMETER_NAME = "weights";

	/** The weights. */
	private List<Integer> weights;

	/**
	 * Instantiates a new weights parameter with default weights.
	 *
	 * @param weights the weights
	 */
	public WeightsParameter(int... weights) {
		this();
		this.weights = new ArrayList<Integer>();
		for (int weight : weights) {
			this.weights.add(weight);
		}
	}

	/**
	 * Instantiates a new weights parameter.
	 */
	WeightsParameter() {
		super(PARAMETER_NAME);
		this.weights = new ArrayList<Integer>();
	}

	/**
	 * Adds a weight.
	 *
	 * @param weight the weight
	 */
	public void addWeight(int weight) {
		weights.add(weight);
	}

	/**
	 * Edits the weight at the specified index.
	 *
	 * @param index  the index
	 * @param weight the weight
	 */
	public void editWeight(int index, int weight) {
		weights.set(index, weight);
	}

	/**
	 * Returns the weights as an int array.
	 *
	 * @return the weights
	 */
	public int[] getWeights() {
		return weights.stream().mapToInt(value -> value.intValue()).toArray();
	}

	/**
	 * Removes the weight at the specified index.
	 *
	 * @param index the index
	 */
	public void removeWeight(int index) {
		weights.remove(index);
	}

	@Override
	public void stringToValue(String stringValue) throws ParseException {
		if (stringValue == null) {
			throw new ParseException(this.getClass(), NULL_ERROR_MESSAGE);
		}
		if (stringValue.isBlank()) {
			// No weight to put
			return;
		}

		final String separator = Consts.ELEMENT_SEPARATOR;
		String[] values = stringValue.split(separator);
		weights.clear();

		try {
			for (int i = 0; i < values.length; i++) {
				int value = Integer.valueOf(values[i]);
				weights.add(value);
			}
		} catch (NumberFormatException e) {
			throw new ParseException(this.getClass(), ERROR_MESSAGE);
		}
	}

	@Override
	@NotNull
	public String valueToString() {
		final String separator = Consts.ELEMENT_SEPARATOR;
		String valueStr = "";

		for (int i = 0; i < weights.size(); i++) {
			valueStr += String.valueOf(weights.get(i));

			if (i != weights.size() - 1) {
				valueStr += separator;
			}
		}

		return valueStr;
	}

}
