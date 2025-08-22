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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.taf.annotation.FactoryObject;
import com.taf.annotation.NotNull;
import com.taf.exception.ParseException;
import com.taf.logic.type.StringType;
import com.taf.util.Consts;

/**
 * The ValuesParameter parameter represents a list of string values that is used
 * in a {@link StringType}. This is a complex type and hides the
 * {@link WeightsParameter} so no one has to deal with it.
 * 
 * @see TypeParameter
 * @see StringType
 *
 * @author Adrien Jakubiak
 */
@FactoryObject(types = {}, generate = false)
public class ValuesParameter extends TypeParameter {

	/** The parameter name. */
	public static final String PARAMETER_NAME = "values";

	private static final String NULL_ERROR_MESSAGE = "Values must not be null!";

	/** The value to weight map. */
	private final HashMap<String, Integer> valueMap;

	/**
	 * Instantiates a new values parameter.
	 */
	public ValuesParameter() {
		super(PARAMETER_NAME);
		valueMap = new LinkedHashMap<String, Integer>();
	}

	/**
	 * Instantiates a new values parameter with a list of values.
	 *
	 * @param values the values
	 */
	public ValuesParameter(String... values) {
		this();
		for (String value : values) {
			addValue(value);
		}
	}

	/**
	 * Adds the value with a default weight to the parameter if and only if the
	 * value is not already present. Returns true if the value was added.
	 *
	 * @param value the value
	 * @return true if the value was added (if value was not already present)
	 */
	public boolean addValue(String value) {
		return addValue(value, Consts.DEFAULT_WEIGHT_VALUE);
	}

	/**
	 * Adds the value to the parameter if and only if the value is not already
	 * present. Returns true if the value was added.
	 *
	 * @param value  the value
	 * @param weight the weight
	 * @return true if the value was added (if value was not already present)
	 */
	public boolean addValue(String value, int weight) {
		if (!valueMap.containsKey(value)) {
			valueMap.put(value, weight);
			return true;
		}

		return false;
	}

	/**
	 * Creates the weight parameter.
	 *
	 * @return the type parameter
	 */
	public TypeParameter createWeightParameter() {
		return new WeightsParameter(valueMap.values().stream().mapToInt(Integer::intValue).toArray());
	}

	/**
	 * Edits the value name if the old value does not exist or if the new value
	 * already exists. Returns true if the value name was changed.
	 * 
	 * @param oldValue the value to replace
	 * @param newValue the new value
	 * @return false if the old value does not exist or if the new value already
	 *         exists, true otherwise
	 */
	public boolean editValueName(String oldValue, String newValue) {
		if (!valueMap.containsKey(newValue) && valueMap.containsKey(oldValue)) {
			Integer weight = valueMap.get(oldValue);
			valueMap.remove(oldValue);
			valueMap.put(newValue, weight);
			return true;
		}

		return false;
	}

	/**
	 * Returns the values with their corresponding weights.
	 *
	 * @return the values and weights
	 */
	public Set<Entry<String, Integer>> getValues() {
		return valueMap.entrySet();
	}

	/**
	 * Returns the weight associated to the value or -1 if the value does not exist.
	 * 
	 * @param value the value to get the weight
	 * @return the associated weight, or -1 if the values does not exist
	 */
	public int getWeight(String value) {
		Integer weight = valueMap.get(value);
		return weight == null ? -1 : weight;
	}

	/**
	 * Removes a value if it exists. Returns true if a value was removed.
	 * 
	 * @param value the value to remove
	 * @return false if the value does not exist, true otherwise
	 */
	public boolean removeValue(String value) {
		Integer retVal = valueMap.remove(value);
		return retVal != null;
	}

	/**
	 * Sets the weight of a value if the value exists. Returns true if the weight
	 * was set.
	 * 
	 * @param value  the value to change the weight
	 * @param weight the weight value
	 * @return false if the value does not exist, true otherwise
	 */
	public boolean setWeight(String value, int weight) {
		// replace method checks if value exists
		Integer retVal = valueMap.replace(value, weight);
		return retVal != null;
	}

	/**
	 * <p>
	 * Sets the weights for the values. The weights will be attributed in order,
	 * meaning that the first element will receive the first weight, etc...
	 * </p>
	 * <p>
	 * If there are less weights than values, it will not change the remaining
	 * values. If there are more weights than values, the exceeding values will be
	 * ignored.
	 * </p>
	 * 
	 * @param weights the weights
	 */
	public void setWeights(int... weights) {
		String[] values = valueMap.keySet().toArray(String[]::new);
		int maxIndex = Math.min(values.length, weights.length);

		for (int i = 0; i < maxIndex; i++) {
			setWeight(values[i], weights[i]);
		}
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
		valueMap.clear();

		for (String value : values) {
			if (!value.isBlank()) {
				addValue(value);
			}
		}
	}

	@Override
	@NotNull
	public String valueToString() {
		final String separator = Consts.ELEMENT_SEPARATOR;
		String valueStr = "";

		for (String value : valueMap.keySet()) {
			valueStr += value + separator;
		}

		// Remove last separator if there is an element
		if (!valueStr.isBlank()) {
			valueStr = valueStr.substring(0, valueStr.length() - separator.length());
		}

		return valueStr;
	}

}
