package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;
import com.taf.manager.ConstantManager;

public class WeightParameter extends TypeParameter {

	private static final String PARAMETER_NAME = "weights";

	private int[] weights;

	public WeightParameter() {		
		super(PARAMETER_NAME);
	}
	
	public WeightParameter(int... weights) {
		this();
		this.weights = weights;
	}

	@Override
	protected void valuefromString(String stringValue) throws ParseException {
		final String separator = ConstantManager.ELEMENT_SEPARATOR;
		String[] values = stringValue.split(separator);
		weights = new int[values.length];
		
		for (String value : values) {
			// TOOO
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
