package com.taf.logic.type.parameter;

import java.util.HashMap;

import com.taf.manager.ConstantManager;

public class ValuesParameter extends TypeParameter {

	private static final String PARAMETER_NAME = "values";

	private final HashMap<String, Integer> valueMap;

	public ValuesParameter() {
		super(PARAMETER_NAME);
		valueMap = new HashMap<String, Integer>();
	}

	public void addValue(String value, int weight) {
		valueMap.put(value, weight);
	}

	public void addValue(String value) {
		addValue(value, 1);
	}

	/**
	 * Edits the value name. This returns false if the old value does not exist.
	 * 
	 * @param oldValue the value to replace
	 * @param newValue the new value
	 * @return false if the old value does not exist, true otherwise
	 */
	public boolean editValueName(String oldValue, String newValue) {
		Integer weight = valueMap.get(oldValue);
		if (weight != null) {
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
