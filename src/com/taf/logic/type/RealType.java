package com.taf.logic.type;

import java.util.HashSet;
import java.util.Set;

import com.taf.logic.type.parameter.DistributionParameter;
import com.taf.logic.type.parameter.MaxRealParameter;
import com.taf.logic.type.parameter.MeanParameter;
import com.taf.logic.type.parameter.MinRealParameter;
import com.taf.logic.type.parameter.RangesParameter;
import com.taf.logic.type.parameter.RangesParameter.Range;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.logic.type.parameter.VarianceParameter;
import com.taf.logic.type.parameter.WeightsParameter;
import com.taf.manager.ConstantManager;
import com.taf.util.HashSetBuilder;

public class RealType extends NumericalType {

	public static final String TYPE_NAME = "real";
	private static final HashSet<String> MANDATORY_TYPE_PARAMETERS = new HashSetBuilder<String>()
			.add(MaxRealParameter.PARAMETER_NAME).add(MinRealParameter.PARAMETER_NAME).build();

	private static final HashSet<String> OPTIONAL_TYPE_PARAMETERS = new HashSetBuilder<String>()
			.add(DistributionParameter.PARAMETER_NAME).add(MeanParameter.PARAMETER_NAME)
			.add(VarianceParameter.PARAMETER_NAME).add(RangesParameter.PARAMETER_NAME)
			.add(WeightsParameter.PARAMETER_NAME).build();

	public RealType() {
		super(TYPE_NAME, new MinRealParameter(ConstantManager.DEFAULT_MIN_VALUE),
				new MaxRealParameter(ConstantManager.DEFAULT_MAX_VALUE));
	}

	public void editMin(long minValue) {
		super.editMin(minValue);
	}

	public double getMin() {
		return super.getMinNumber().doubleValue();
	}

	public void editMax(long maxValue) {
		super.editMax(maxValue);
	}

	public double getMax() {
		return super.getMaxNumber().doubleValue();
	}

	public void addInterval(double lowerBound, double upperBound, int weight) {
		super.addInterval(lowerBound, upperBound, weight);
	}

	public void editLowerBound(int index, double lowerBound) {
		super.editLowerBound(index, lowerBound);
	}

	public void editUpperBound(int index, double upperBound) {
		super.editUpperBound(index, upperBound);
	}
	
	@Override
	public void addTypeParameter(TypeParameter typeParameter) {
		if (typeParameter instanceof MinRealParameter) {
			editMin(((MinRealParameter) typeParameter).getValue().doubleValue());
		} else if (typeParameter instanceof MaxRealParameter) {
			editMax(((MaxRealParameter) typeParameter).getValue().doubleValue());
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

	@Override
	public String getName() {
		return TYPE_NAME;
	}

}
