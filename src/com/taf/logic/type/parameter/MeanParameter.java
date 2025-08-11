package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;
import com.taf.util.Consts;

public class MeanParameter extends TypeParameter {

	private static final String ERROR_MESSAGE = "Mean value must be an integer or a real!";

	public static final String PARAMETER_NAME = "mean";

	private double mean;

	MeanParameter() {
		super(PARAMETER_NAME);
	}

	public MeanParameter(double mean) {
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
		return Consts.REAL_FORMATTER.format(mean);
	}

	@Override
	public void stringToValue(String stringValue) throws ParseException {
		try {
			this.mean = Double.valueOf(stringValue);
		} catch (NumberFormatException | NullPointerException e) {
			throw new ParseException(this.getClass(), ERROR_MESSAGE);
		}
	}

}
