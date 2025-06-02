package com.taf.logic.type.parameter;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.taf.manager.ConstantManager;

public class ValuesParameter extends TypeParameter {

	private static final String PARAMETER_NAME = "values";

	private final HashMap<String, Integer> valueMap;

	public ValuesParameter() {
		super(PARAMETER_NAME);
		valueMap = new HashMap<String, Integer>();
	}

	/**
	 * Adds the value to the parameter iff the value is not already present. Returns
	 * true if the value was added.
	 * 
	 * @param value
	 * @param weight
	 * 
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
	 * Adds the value with a weight of 1 to the parameter iff the value is not
	 * already present. Returns true if the value was added.
	 * 
	 * @param value
	 * 
	 * @return true if the value was added (if value was not already present)
	 */
	public boolean addValue(String value) {
		return addValue(value, 1);
	}

	/**
	 * Edits the value name. This returns false if the old value does not exist or
	 * if the new value already exists
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
	 * Sets the weight of a value. If the value does not exist, this returns false.
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
	 * Removes a value. If the value does not exist, this returns false.
	 * 
	 * @param value the value to remove
	 * @return false if the value does not exist, true otherwise
	 */
	public boolean removeValue(String value) {
		Integer retVal = valueMap.remove(value);
		return retVal != null;
	}

	public TypeParameter createWeightParameter() {
		return new WeightParameter(valueMap.values().stream().mapToInt(Integer::intValue).toArray());
	}

	public Set<Entry<String, Integer>> getValues() {
		return valueMap.entrySet();
	}

	@Override
	public String valueToString() {
		final String separator = ConstantManager.ELEMENT_SEPARATOR;
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
