package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;
import com.taf.manager.ConstantManager;

public class WeightParameter extends TypeParameter {

	public static final String PARAMETER_NAME = "weights";

	private int[] weights;

	WeightParameter() {
		super(PARAMETER_NAME);
		this.weights = new int[0];
	}

	public WeightParameter(int... weights) {
		this();
		// TODO Check if positive values
		this.weights = weights;
	}
	
	public int[] getWeights() {
		return weights;
	}

	@Override
	public void valuefromString(String stringValue) throws ParseException {
		final String separator = ConstantManager.ELEMENT_SEPARATOR;
		String[] values = stringValue.split(separator);
		weights = new int[values.length];

		try {
			for (int i = 0; i < values.length; i++) {
				int value = Integer.valueOf(values[i]);
				weights[i] = value < 0 ? 0 : value;
			}
		} catch (NumberFormatException e) {
			throw new ParseException("Weight value must be an integer!");
		}
	}

	@Override
	public String valueToString() {
		final String separator = ConstantManager.ELEMENT_SEPARATOR;
		String valueStr = "";

		for (int i = 0; i < weights.length; i++) {
			valueStr += String.valueOf(weights[i]);

			if (i != weights.length - 1) {
				valueStr += separator;
			}
		}

		return valueStr;
	}

}
