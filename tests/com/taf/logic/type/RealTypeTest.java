package com.taf.logic.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.taf.logic.type.parameter.RangesParameter.Range;
import com.taf.manager.ConstantManager;

class RealTypeTest extends NumericalTypeTest{

	public RealTypeTest() {
		super(new RealType());
	}

	@Override
	@Test
	void testNumericalTypeChangeMinMax() {
		RealType integerType = new RealType();
		integerType.editMin(ConstantManager.DEFAULT_MIN_VALUE + 1);
		integerType.editMax(ConstantManager.DEFAULT_MAX_VALUE + 1);
		assertEquals(ConstantManager.DEFAULT_MIN_VALUE + 1, integerType.getMin());
		assertEquals(ConstantManager.DEFAULT_MAX_VALUE + 1, integerType.getMax());
	}

	@Override
	@Test
	void testNumericalTypeAddRange() {
		RealType integerType = new RealType();
		double lowerBound = 1.5;
		double upperBound = 2.5;
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
	@Test
	void testNumericalTypeEditRange() {
		RealType integerType = new RealType();
		double lowerBound = 1.5;
		double upperBound = 2.5;
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
	@Test
	void testNumericalTypeRemoveRange() {
		RealType integerType = new RealType();
		double lowerBound = 1.5;
		double upperBound = 2.5;
		int weight = 3;

		integerType.addInterval(lowerBound, upperBound, weight);
		integerType.removeInterval(0);
		
		assertIterableEquals(new ArrayList<Range>(), integerType.getRanges());
		assertArrayEquals(new int[0], integerType.getWeights());
	}

}
