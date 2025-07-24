package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public class MeanParameter extends TypeParameter {

	private static final String ERROR_MESSAGE = "Mean value must be an integer or a real!";

	public static final String PARAMETER_NAME = "mean";

	private double mean;

	MeanParameter() {
		super(PARAMETER_NAME);
	}

	MeanParameter(double mean) {
		this();
		this.mean = mean;
	}
	
	public void setMean(double mean) {
		this.mean = mean;
	}
	
	public double getMean() {
		return mean;
	}

	@Override
	public String valueToString() {
		return String.valueOf(mean);
	}

	@Override
	void stringToValue(String stringValue) throws ParseException {
		try {
			this.mean = Double.valueOf(stringValue);
		} catch (NumberFormatException e) {
			throw new ParseException(this.getClass(), ERROR_MESSAGE);
		}
	}

}
