package com.taf.logic.type;

import java.util.HashSet;
import java.util.Set;

import com.taf.logic.type.parameter.DistributionParameter;
import com.taf.logic.type.parameter.MaxIntegerParameter;
import com.taf.logic.type.parameter.MeanParameter;
import com.taf.logic.type.parameter.MinIntegerParameter;
import com.taf.logic.type.parameter.RangesParameter;
import com.taf.logic.type.parameter.RangesParameter.Range;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.logic.type.parameter.VarianceParameter;
import com.taf.logic.type.parameter.WeightsParameter;
import com.taf.util.Consts;
import com.taf.util.HashSetBuilder;

public class IntegerType extends NumericalType {

	public static final String TYPE_NAME = "integer";

	private static final HashSet<String> MANDATORY_TYPE_PARAMETERS = new HashSetBuilder<String>()
			.add(MaxIntegerParameter.PARAMETER_NAME).add(MinIntegerParameter.PARAMETER_NAME).build();

	private static final HashSet<String> OPTIONAL_TYPE_PARAMETERS = new HashSetBuilder<String>()
			.add(DistributionParameter.PARAMETER_NAME).add(MeanParameter.PARAMETER_NAME)
			.add(VarianceParameter.PARAMETER_NAME).add(RangesParameter.PARAMETER_NAME)
			.add(WeightsParameter.PARAMETER_NAME).build();

	public IntegerType() {
		super(TYPE_NAME, new MinIntegerParameter(Consts.DEFAULT_MIN_VALUE),
				new MaxIntegerParameter(Consts.DEFAULT_MAX_VALUE));
	}

	public void editMin(long minValue) {
		super.editMinNumber(minValue);
	}

	public long getMin() {
		return super.getMinNumber().longValue();
	}

	public void editMax(long maxValue) {
		super.editMaxNumber(maxValue);
	}

	public long getMax() {
		return super.getMaxNumber().longValue();
	}

	public void addInterval(long lowerBound, long upperBound, int weight) {
		super.addInterval(lowerBound, upperBound, weight);
	}

	public void editLowerBound(int index, long lowerBound) {
		super.editLowerBound(index, lowerBound);
	}

	public void editUpperBound(int index, long upperBound) {
		super.editUpperBound(index, upperBound);
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
				addInterval(range.getLowerBound(), range.getUpperBound(), Consts.DEFAULT_WEIGHT_VALUE);
			}
		} else if (typeParameter instanceof WeightsParameter) {
			int[] weights = ((WeightsParameter) typeParameter).getWeights();
			int rangeSize = super.getRangeNumber();
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

}
