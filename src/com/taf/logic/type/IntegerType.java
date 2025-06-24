package com.taf.logic.type;

import java.util.HashSet;
import java.util.Set;

import com.taf.logic.type.parameter.DistributionParameter;
import com.taf.logic.type.parameter.DistributionType;
import com.taf.logic.type.parameter.MaxIntegerParameter;
import com.taf.logic.type.parameter.MeanParameter;
import com.taf.logic.type.parameter.MinIntegerParameter;
import com.taf.logic.type.parameter.RangesParameter;
import com.taf.logic.type.parameter.RangesParameter.Range;
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
		min = new MinIntegerParameter(ConstantManager.DEFAULT_MIN_VALUE);
		max = new MaxIntegerParameter(ConstantManager.DEFAULT_MAX_VALUE);
		distribution = new DistributionParameter(DistributionType.UNIFORM);
	}

	public void editMin(long minValue) {
		min.setValue(minValue);
	}

	public long getMin() {
		return min.getValue().longValue();
	}

	public void editMax(long maxValue) {
		max.setValue(maxValue);
	}

	public long getMax() {
		return max.getValue().longValue();
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
			editMin(((MinIntegerParameter) typeParameter).getValue().longValue());
		} else if (typeParameter instanceof MaxIntegerParameter) {
			editMax(((MaxIntegerParameter) typeParameter).getValue().longValue());
		} else if (typeParameter instanceof DistributionParameter) {
			setDistribution(((DistributionParameter) typeParameter).getDistributionType());
		} else if (typeParameter instanceof MeanParameter) {
			editMean(((MeanParameter) typeParameter).getMean());
		} else if (typeParameter instanceof VarianceParameter) {
			editVariance(((VarianceParameter) typeParameter).getVariance());
		} else if (typeParameter instanceof RangesParameter) {
			for (Range range : ((RangesParameter) typeParameter).getRanges()) {
				addInterval(range.getLowerBound(), range.getUpperBound(), ConstantManager.DEFAULT_WEIGHT_VALUE);
			}
		} else if (typeParameter instanceof WeightsParameter) {
			int[] weights = ((WeightsParameter) typeParameter).getWeights();
			int rangeSize = ((RangesParameter) distribution.getRangesParameter()).size();
			for (int i = 0; i < Math.min(rangeSize, weights.length); i++) {
				editWeight(i, weights[i]);
			}
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
