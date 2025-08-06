package com.taf.logic.type.parameter;

import java.util.ArrayList;
import java.util.List;

import com.taf.exception.ParseException;
import com.taf.util.Consts;

public class WeightsParameter extends TypeParameter {

	private static final String ERROR_MESSAGE = "Weight value must be an integer!";

	public static final String PARAMETER_NAME = "weights";

	private List<Integer> weights;

	WeightsParameter() {
		super(PARAMETER_NAME);
		this.weights = new ArrayList<Integer>();
	}

	public WeightsParameter(int... weights) {
		this();
		// TODO Check if positive values
		this.weights = new ArrayList<Integer>();
		for (int weight : weights) {
			this.weights.add(weight);
		}
	}

	public int[] getWeights() {
		return weights.stream().mapToInt(value -> value.intValue()).toArray();
	}

	public void addWeight(int weight) {
		weights.add(weight);
	}

	public void removeWeight(int index) {
		weights.remove(index);
	}
	
	public void editWeight(int index, int weight) {
		weights.set(index, weight);
	}

	@Override
	void stringToValue(String stringValue) throws ParseException {
		final String separator = Consts.ELEMENT_SEPARATOR;
		String[] values = stringValue.split(separator);
		weights = new ArrayList<Integer>();

		try {
			for (int i = 0; i < values.length; i++) {
				int value = Integer.valueOf(values[i]);
				weights.add(value < 0 ? 0 : value);
			}
		} catch (NumberFormatException e) {
			throw new ParseException(this.getClass(), ERROR_MESSAGE);
		}
	}

	@Override
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
