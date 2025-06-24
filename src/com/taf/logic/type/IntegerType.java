package com.taf.logic.type;

import java.util.HashSet;
import java.util.Set;

import com.taf.logic.type.parameter.DistributionParameter;
import com.taf.logic.type.parameter.DistributionType;
import com.taf.logic.type.parameter.MaxIntegerParameter;
import com.taf.logic.type.parameter.MeanParameter;
import com.taf.logic.type.parameter.MinIntegerParameter;
import com.taf.logic.type.parameter.RangesParameter;
import com.taf.logic.type.parameter.TypeNameParameter;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.logic.type.parameter.VarianceParameter;
import com.taf.logic.type.parameter.WeightsParameter;
import com.taf.manager.ConstantManager;
import com.taf.util.HashSetBuilder;

public class IntegerType extends Type {

	public static final String TYPE_NAME = "integer";
	private static final HashSet<Class<? extends TypeParameter>> ALLOWED_TYPE_PARAMETERS = new HashSetBuilder<Class<? extends TypeParameter>>()
			.add(MaxIntegerParameter.class).add(MinIntegerParameter.class).build();

	private static final HashSet<String> MANDATORY_TYPE_PARAMETERS = new HashSetBuilder<String>()
			.add(MaxIntegerParameter.PARAMETER_NAME).add(MinIntegerParameter.PARAMETER_NAME).build();

	private static final HashSet<String> OPTIONAL_TYPE_PARAMETERS = new HashSetBuilder<String>()
			.add(DistributionParameter.PARAMETER_NAME).add(MeanParameter.PARAMETER_NAME)
			.add(VarianceParameter.PARAMETER_NAME).add(RangesParameter.PARAMETER_NAME)
			.add(WeightsParameter.PARAMETER_NAME).build();

	private TypeParameter typeName;
	private MinIntegerParameter min;
	private MaxIntegerParameter max;
	private DistributionParameter distribution;

	public IntegerType() {
		typeName = new TypeNameParameter(TYPE_NAME);
		distribution = new DistributionParameter(DistributionType.UNIFORM);
	}

	public void addMinParameter(long minValue) {
		if (min == null) {
			min = new MinIntegerParameter(minValue);
		} else {
			min.setValue(minValue);
		}
	}

	public void editMinParameter(long minValue) {
		if (min != null) {
			min.setValue(minValue);
		}
	}

	public long getMinParameter() {
		if (min == null) {
			return 0;
		}

		return min.getValue().longValue();
	}

	public void removeMinParameter() {
		min = null;
	}

	public boolean hasMinParameter() {
		return min != null;
	}

	public void addMaxParameter(long maxValue) {
		if (max == null) {
			max = new MaxIntegerParameter(maxValue);
		} else {
			max.setValue(maxValue);
		}
	}

	public void editMaxParameter(long maxValue) {
		if (max != null) {
			max.setValue(maxValue);
		}
	}

	public long getMaxParameter() {
		if (max == null) {
			return 0;
		}

		return max.getValue().longValue();
	}

	public void removeMaxParameter() {
		max = null;
	}

	public boolean hasMaxParameter() {
		return max != null;
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

	public void addInterval(int lowerBound, int upperBound, int weight) {
		distribution.addInterval(lowerBound, upperBound, weight);
	}

	public void editLowerBound(int index, int lowerBound) {
		distribution.editLowerBound(index, lowerBound);
	}

	public void editUpperBound(int index, int upperBound) {
		distribution.editUpperBound(index, upperBound);
	}

	public void editWeight(int index, int weight) {
		distribution.editWeight(index, weight);
	}

	@Override
	public void addTypeParameter(TypeParameter typeParameter) {
		if (typeParameter instanceof MinIntegerParameter) {
			addMinParameter(((MinIntegerParameter) typeParameter).getValue().longValue());
		} else if (typeParameter instanceof MaxIntegerParameter) {
			addMaxParameter(((MaxIntegerParameter) typeParameter).getValue().longValue());
		}
	}

	@Override
	public Set<String> getMandatoryParametersName() {
		return MANDATORY_TYPE_PARAMETERS;
	}

	@Override
	public Set<String> getOptionalParametersName() {
		return OPTIONAL_TYPE_PARAMETERS;
	}

	@Override
	public boolean isAllowedTypeParameter(TypeParameter typeParameter) {
		return ALLOWED_TYPE_PARAMETERS.contains(typeParameter.getClass());
	}

	@Override
	public String getName() {
		return TYPE_NAME;
	}

	@Override
	public String typeToString() {
		final String separator = ConstantManager.PARAMETER_SEPARATOR;
		String typeStr = typeName.toString();

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
