package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;
import com.taf.util.Consts;

public class VarianceParameter extends TypeParameter {

	private static final String ERROR_MESSAGE = "Variance value must be an integer or a real!";
	
	public static final String PARAMETER_NAME = "variance";
	
	private double variance;

	VarianceParameter() {
		super(PARAMETER_NAME);
	}
	
	public VarianceParameter(double variance) {
		this();
		this.variance = variance;
	}
	
	public void setVariance(double variance) {
		this.variance = variance;
	}
	
	public double getVariance() {
		return variance;
	}

	@Override
	public String valueToString() {
		return Consts.REAL_FORMATTER.format(variance);
	}

	@Override
	void stringToValue(String stringValue) throws ParseException {
		try {			
			this.variance = Double.valueOf(stringValue);
		} catch (NumberFormatException | NullPointerException e) {
			throw new ParseException(this.getClass(), ERROR_MESSAGE);
		}
	}
	
}
