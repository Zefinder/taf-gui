package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;
import com.taf.util.Consts;

public class DistributionParameter extends TypeParameter {

	public static final String PARAMETER_NAME = "distribution";

	private DistributionType distributionType;

	private MeanParameter meanParameter;
	private VarianceParameter varianceParameter;

	private RangesParameter rangesParameter;
	private WeightsParameter weightsParameter;

	public DistributionParameter() {
		super(PARAMETER_NAME);
		meanParameter = new MeanParameter(Consts.DEFAULT_MEAN_VALUE);
		varianceParameter = new VarianceParameter(Consts.DEFAULT_VARIANCE_VALUE);
		rangesParameter = new RangesParameter();
		weightsParameter = new WeightsParameter();
	}

	public DistributionParameter(DistributionType distributionType) {
		this();
		this.distributionType = distributionType;
	}

	public DistributionType getDistributionType() {
		return distributionType;
	}

	public void setDistributionType(DistributionType distributionType) {
		this.distributionType = distributionType;
	}

	public void editMean(double mean) {
		meanParameter.setMean(mean);
	}
	
	public double getMean() {
		return meanParameter.getMean();
	}
	
	public void editVariance(double variance) {
		varianceParameter.setVariance(variance);
	}
	
	public double getVariance() {
		return varianceParameter.getVariance();
	}
	
	public void addInterval(Number lowerBound, Number upperBound, int weight) {
		rangesParameter.addRange(lowerBound, upperBound);
		weightsParameter.addWeight(weight);
	}
	
	public void editLowerBound(int index, Number lowerBound) {
		rangesParameter.editLowerBound(index, lowerBound);
	}
	
	public void editUpperBound(int index, Number upperBound) {
		rangesParameter.editUpperBound(index, upperBound);
	}
	
	public void editWeight(int index, int weight) {
		weightsParameter.editWeight(index, weight);
	}
	
	public void removeInterval(int index) {
		rangesParameter.removeRange(index);
		weightsParameter.removeWeight(index);
	}
	
	public TypeParameter getMeanParameter() {
		return meanParameter;
	}
	
	public TypeParameter getVarianceParameter() {
		return varianceParameter;
	}
	
	public TypeParameter getRangesParameter() {
		return rangesParameter;
	}
	
	public TypeParameter getWeightsParameter() {
		return weightsParameter;
	}
	
	@Override
	public String valueToString() {		
		return distributionType.getDistributionString();
	}

	@Override
	void stringToValue(String stringValue) throws ParseException {
		distributionType = DistributionType.fromDistributionString(stringValue);
	}

}
