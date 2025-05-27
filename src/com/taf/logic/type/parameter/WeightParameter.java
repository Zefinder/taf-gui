package com.taf.logic.type.parameter;

import com.taf.manager.ConstantManager;

public class WeightParameter extends TypeParameter {

	private static final String PARAMETER_NAME = "weights";

	private final int[] weights;

	public WeightParameter(int... weights) {
		super(PARAMETER_NAME);
		this.weights = weights;
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
