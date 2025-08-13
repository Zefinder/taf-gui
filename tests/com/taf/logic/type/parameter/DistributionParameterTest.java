package com.taf.logic.type.parameter;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.taf.logic.type.parameter.RangesParameter.Range;
import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;
import com.taf.util.Consts;
import com.taf.util.Pair;

class DistributionParameterTest extends TypeParameterTest {

	private DistributionParameter distributionParameter;

	public DistributionParameterTest() {
		super(new DistributionParameter(DistributionType.UNIFORM), "distribution",
				DistributionType.UNIFORM.getDistributionString(), MinMaxTypeParameterType.NONE);
		distributionParameter = (DistributionParameter) typeParameter;
	}

	@Override
	public Stream<Pair<String, String>> valueProvider() {
		return Stream.of(new Pair<String, String>("u", "u"), new Pair<String, String>("i", "i"),
				new Pair<String, String>("u", "u"), new Pair<String, String>("i", "i"),
				new Pair<String, String>("boom", "u"));
	}

	@Override
	public Stream<String> badValueProvider() {
		return Stream.of((String) null);
	}

	@ParameterizedTest
	@EnumSource(value = DistributionType.class)
	void testDistributionParameterChangeDistributionType(DistributionType distributionType) {
		distributionParameter.setDistributionType(distributionType);
		assertEquals(distributionType, distributionParameter.getDistributionType());
	}

	@Test
	void testDistributionParameterChangeMean() {
		double mean = Consts.DEFAULT_MEAN_VALUE + 1;
		distributionParameter.editMean(mean);
		assertEquals(mean, distributionParameter.getMean());
	}

	@Test
	void testDistributionParameterChangeVariance() {
		double variance = Consts.DEFAULT_VARIANCE_VALUE + 1;
		distributionParameter.editMean(variance);
		assertEquals(variance, distributionParameter.getMean());
	}

	@Test
	void testDistributionParameterAddInterval() {
		int lowerBound = 1;
		int upperBound = 4;
		int weight = Consts.DEFAULT_WEIGHT_VALUE;
		distributionParameter.addInterval(lowerBound, upperBound, weight);

		RangesParameter rangesParameter = (RangesParameter) distributionParameter.getRangesParameter();
		WeightsParameter weightsParameter = (WeightsParameter) distributionParameter.getWeightsParameter();

		List<Range> expectedRanges = new ArrayList<Range>();
		int[] expectedWeights = { weight };

		expectedRanges.add(new Range(lowerBound, upperBound));

		assertIterableEquals(expectedRanges, rangesParameter.getRanges());
		assertArrayEquals(expectedWeights, weightsParameter.getWeights());
	}

	@Test
	void testDistributionParameterEditInterval() {
		int lowerBound = 1;
		int upperBound = 4;
		int weight = Consts.DEFAULT_WEIGHT_VALUE;
		distributionParameter.addInterval(lowerBound, upperBound, weight);
		distributionParameter.editLowerBound(0, lowerBound + 1);
		distributionParameter.editUpperBound(0, upperBound + 1);
		distributionParameter.editWeight(0, weight + 1);

		RangesParameter rangesParameter = (RangesParameter) distributionParameter.getRangesParameter();
		WeightsParameter weightsParameter = (WeightsParameter) distributionParameter.getWeightsParameter();

		List<Range> expectedRanges = new ArrayList<Range>();
		int[] expectedWeights = { weight + 1 };

		expectedRanges.add(new Range(lowerBound + 1, upperBound + 1));

		assertIterableEquals(expectedRanges, rangesParameter.getRanges());
		assertArrayEquals(expectedWeights, weightsParameter.getWeights());
	}

	@Test
	void testDistributionParameterRemoveInterval() {
		int lowerBound = 1;
		int upperBound = 4;
		int weight = Consts.DEFAULT_WEIGHT_VALUE;
		distributionParameter.addInterval(lowerBound, upperBound, weight);
		distributionParameter.removeInterval(0);

		RangesParameter rangesParameter = (RangesParameter) distributionParameter.getRangesParameter();
		WeightsParameter weightsParameter = (WeightsParameter) distributionParameter.getWeightsParameter();

		int[] expectedWeights = {};

		assertIterableEquals(new ArrayList<Range>(), rangesParameter.getRanges());
		assertArrayEquals(expectedWeights, weightsParameter.getWeights());
	}

}
