package com.taf.logic.type.parameter;

public class WeightParameter extends TypeParameter {

	private static final String PARAMETER_NAME = "weights";

	private final int[] weights;

	public WeightParameter(int... weights) {
		super(PARAMETER_NAME);
		this.weights = weights;
	}

	@Override
	public String valueToString() {
		String valueStr = "";
		for (int i = 0; i < weights.length; i++) {
			valueStr += String.valueOf(weights[i]);
			if (i != weights.length - 1) {
				valueStr += ";";
			}
		}

		return valueStr;
	}

}
