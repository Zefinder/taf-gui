package com.taf.logic.type;

import java.util.List;

import com.taf.logic.type.parameter.DistributionParameter;
import com.taf.logic.type.parameter.DistributionType;
import com.taf.logic.type.parameter.MaxParameter;
import com.taf.logic.type.parameter.MinParameter;
import com.taf.logic.type.parameter.RangesParameter;
import com.taf.logic.type.parameter.RangesParameter.Range;
import com.taf.logic.type.parameter.TypeNameParameter;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.logic.type.parameter.WeightsParameter;
import com.taf.manager.ConstantManager;

public abstract class NumericalType extends FieldType {

	private final String typeName;
	private TypeParameter typeNameParameter;
	private MinParameter min;
	private MaxParameter max;
	private DistributionParameter distribution;
	
	public NumericalType(String typeName, MinParameter minParameter, MaxParameter maxParameter) {
		this.typeName = typeName;
		this.typeNameParameter = new TypeNameParameter(typeName);
		this.min = minParameter;
		this.max = maxParameter;
		this.distribution = new DistributionParameter(ConstantManager.DEFAULT_DISTRIBUTION);
	}

	public void editMinNumber(Number minValue) {
		min.setValue(minValue);
	}

	public Number getMinNumber() {
		return min.getValue();
	}

	public void editMaxNumber(Number maxValue) {
		max.setValue(maxValue);
	}

	public Number getMaxNumber() {
		return max.getValue();
	}

	public void setDistribution(DistributionType distributionType) {
		distribution.setDistributionType(distributionType);
	}

	public DistributionType getDistribution() {
		return distribution.getDistributionType();
	}

	public void editMean(double mean) {
		distribution.editMean(mean);
	}

	public double getMean() {
		return distribution.getMean();
	}

	public void editVariance(double variance) {
		distribution.editVariance(variance);
	}

	public double getVariance() {
		return distribution.getVariance();
	}

	public void addInterval(Number lowerBound, Number upperBound, int weight) {
		distribution.addInterval(lowerBound, upperBound, weight);
	}

	public void editLowerBound(int index, Number lowerBound) {
		distribution.editLowerBound(index, lowerBound);
	}

	public void editUpperBound(int index, Number upperBound) {
		distribution.editUpperBound(index, upperBound);
	}

	public void editWeight(int index, int weight) {
		distribution.editWeight(index, weight);
	}
	
	public void removeInterval(int index) {
		distribution.removeInterval(index);
	}
	
	protected int getRangeNumber() {
		return ((RangesParameter) distribution.getRangesParameter()).size();
	}
	
	public List<Range> getRanges(){
		return ((RangesParameter) distribution.getRangesParameter()).getRanges();
	}
	
	public int[] getWeights(){
		return ((WeightsParameter) distribution.getWeightsParameter()).getWeights();
	}
	
	@Override
	public String getName() {
		return typeName;
	}
	
	@Override
	public String typeToString() {
		final String separator = ConstantManager.PARAMETER_SEPARATOR;
		String typeStr = typeNameParameter.toString();

		if (min != null) {
			typeStr += separator + min.toString();
		}

		if (max != null) {
			typeStr += separator + max.toString();
		}

		typeStr += separator + distribution.toString();
		switch (distribution.getDistributionType()) {
		case NORMAL:
			typeStr += separator + distribution.getMeanParameter().toString();
			typeStr += separator + distribution.getVarianceParameter().toString();
			break;

		case INTERVAL:
			typeStr += separator + distribution.getRangesParameter().toString();
			typeStr += separator + distribution.getWeightsParameter().toString();
			break;

		default:
			break;
		}

		return typeStr;
	}
}
