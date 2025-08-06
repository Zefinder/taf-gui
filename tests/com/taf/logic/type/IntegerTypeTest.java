package com.taf.logic.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.taf.logic.type.parameter.DistributionParameter;
import com.taf.logic.type.parameter.MaxIntegerParameter;
import com.taf.logic.type.parameter.MeanParameter;
import com.taf.logic.type.parameter.MinIntegerParameter;
import com.taf.logic.type.parameter.RangesParameter;
import com.taf.logic.type.parameter.VarianceParameter;
import com.taf.logic.type.parameter.WeightsParameter;
import com.taf.logic.type.parameter.RangesParameter.Range;
import com.taf.util.Consts;
import com.taf.util.HashSetBuilder;

class IntegerTypeTest extends NumericalTypeTest {

	private IntegerType integerType;

	public IntegerTypeTest() {
		super(new IntegerType());
		integerType = (IntegerType) type;
	}

	@Override
	void testTypeMandatoryParametersImpl() {
		HashSet<String> mandatoryTypeParameters = new HashSetBuilder<String>().add(MaxIntegerParameter.PARAMETER_NAME)
				.add(MinIntegerParameter.PARAMETER_NAME).build();
		assertIterableEquals(mandatoryTypeParameters, type.getMandatoryParametersName());
	}

	@Override
	void testTypeOptionalParametersImpl() {
		HashSet<String> optionalTypeParameters = new HashSetBuilder<String>().add(DistributionParameter.PARAMETER_NAME)
				.add(MeanParameter.PARAMETER_NAME).add(VarianceParameter.PARAMETER_NAME)
				.add(RangesParameter.PARAMETER_NAME).add(WeightsParameter.PARAMETER_NAME).build();
		assertIterableEquals(optionalTypeParameters, type.getOptionalParametersName());
	}
	
	@Override
	void testNumericalTypeChangeMinMaxImpl() {
		integerType.editMin(Consts.DEFAULT_MIN_VALUE + 1);
		integerType.editMax(Consts.DEFAULT_MAX_VALUE + 1);
		assertEquals(Consts.DEFAULT_MIN_VALUE + 1, integerType.getMin());
		assertEquals(Consts.DEFAULT_MAX_VALUE + 1, integerType.getMax());
	}

	@Override
	void testNumericalTypeAddRangeImpl() {
		long lowerBound = 1;
		long upperBound = 2;
		int weight = 3;

		integerType.addInterval(lowerBound, upperBound, weight);

		List<Range> ranges = new ArrayList<Range>();
		int[] weights = { weight };
		Range range = new Range(lowerBound, upperBound);
		ranges.add(range);

		assertIterableEquals(ranges, integerType.getRanges());
		assertArrayEquals(weights, integerType.getWeights());
	}

	@Override
	void testNumericalTypeEditRangeImpl() {
		long lowerBound = 1;
		long upperBound = 2;
		int weight = 3;

		integerType.addInterval(lowerBound, upperBound, weight);

		integerType.editLowerBound(0, lowerBound + 1);
		integerType.editUpperBound(0, upperBound + 1);
		integerType.editWeight(0, weight + 1);

		List<Range> ranges = new ArrayList<Range>();
		int[] weights = { weight + 1 };
		Range range = new Range(lowerBound + 1, upperBound + 1);
		ranges.add(range);

		assertIterableEquals(ranges, integerType.getRanges());
		assertArrayEquals(weights, integerType.getWeights());
	}

	@Override
	void testNumericalTypeRemoveRangeImpl() {
		long lowerBound = 1;
		long upperBound = 2;
		int weight = 3;

		integerType.addInterval(lowerBound, upperBound, weight);
		integerType.removeInterval(0);

		assertIterableEquals(new ArrayList<Range>(), integerType.getRanges());
		assertArrayEquals(new int[0], integerType.getWeights());
	}

}
