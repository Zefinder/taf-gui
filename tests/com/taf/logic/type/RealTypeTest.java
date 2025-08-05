package com.taf.logic.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.ArrayList;
import java.util.List;

import com.taf.logic.type.parameter.RangesParameter.Range;
import com.taf.manager.ConstantManager;

class RealTypeTest extends NumericalTypeTest{

	private RealType realType;
	
	public RealTypeTest() {
		super(new RealType());
		realType = (RealType) type;
	}

	@Override
	void testNumericalTypeChangeMinMaxImpl() {
		realType.editMin(ConstantManager.DEFAULT_MIN_VALUE + 1);
		realType.editMax(ConstantManager.DEFAULT_MAX_VALUE + 1);
		assertEquals(ConstantManager.DEFAULT_MIN_VALUE + 1, realType.getMin());
		assertEquals(ConstantManager.DEFAULT_MAX_VALUE + 1, realType.getMax());
	}

	@Override
	void testNumericalTypeAddRangeImpl() {
		double lowerBound = 1.5;
		double upperBound = 2.5;
		int weight = 3;

		realType.addInterval(lowerBound, upperBound, weight);

		List<Range> ranges = new ArrayList<Range>();
		int[] weights = { weight };
		Range range = new Range(lowerBound, upperBound);
		ranges.add(range);

		assertIterableEquals(ranges, realType.getRanges());
		assertArrayEquals(weights, realType.getWeights());
	}

	@Override
	void testNumericalTypeEditRangeImpl() {
		double lowerBound = 1.5;
		double upperBound = 2.5;
		int weight = 3;

		realType.addInterval(lowerBound, upperBound, weight);

		realType.editLowerBound(0, lowerBound + 1);
		realType.editUpperBound(0, upperBound + 1);
		realType.editWeight(0, weight + 1);

		List<Range> ranges = new ArrayList<Range>();
		int[] weights = { weight + 1 };
		Range range = new Range(lowerBound + 1, upperBound + 1);
		ranges.add(range);

		assertIterableEquals(ranges, realType.getRanges());
		assertArrayEquals(weights, realType.getWeights());
	}

	@Override
	void testNumericalTypeRemoveRangeImpl() {
		double lowerBound = 1.5;
		double upperBound = 2.5;
		int weight = 3;

		realType.addInterval(lowerBound, upperBound, weight);
		realType.removeInterval(0);
		
		assertIterableEquals(new ArrayList<Range>(), realType.getRanges());
		assertArrayEquals(new int[0], realType.getWeights());
	}

}
