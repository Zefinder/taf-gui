package com.taf.logic.type;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.taf.logic.type.parameter.DistributionType;
import com.taf.util.Consts;

abstract class NumericalTypeTest extends TypeTest {

	protected NumericalType type;

	public NumericalTypeTest(NumericalType type) {
		this.type = type;
	}

	@Override
	void testTypeDefaultValuesImpl() {
		assertEquals(Consts.DEFAULT_MIN_VALUE, type.getMinNumber());
		assertEquals(Consts.DEFAULT_MAX_VALUE, type.getMaxNumber());
		assertEquals(Consts.DEFAULT_DISTRIBUTION, type.getDistribution());
		assertEquals(Consts.DEFAULT_MEAN_VALUE, type.getMean());
		assertEquals(Consts.DEFAULT_VARIANCE_VALUE, type.getVariance());
		assertEquals(0, type.getRangeNumber());
		assertEquals(0, type.getWeights().length);
	}

	@ParameterizedTest
	@EnumSource(value = DistributionType.class)
	void testNumericalTypeChangeDistribution(DistributionType distributionType) {
		type.setDistribution(distributionType);
		assertEquals(distributionType, type.getDistribution());
	}

	@Test
	void testNumericalTypeChangeMeanVariance() {
		type.editMean(Consts.DEFAULT_MEAN_VALUE + 1.5);
		type.editVariance(Consts.DEFAULT_VARIANCE_VALUE + 1.5);

		assertEquals(Consts.DEFAULT_MEAN_VALUE + 1.5, type.getMean());
		assertEquals(Consts.DEFAULT_VARIANCE_VALUE + 1.5, type.getVariance());
	}

	@Test
	void testNumericalTypeChangeMinMax() {
		testNumericalTypeChangeMinMaxImpl();
	}
	
	@Test
	void testNumericalTypeAddRange() {
		testNumericalTypeAddRangeImpl();
	}
	
	@Test
	void testNumericalTypeEditRange() {
		testNumericalTypeEditRangeImpl();
	}
	
	@Test
	void testNumericalTypeRemoveRange() {
		testNumericalTypeRemoveRangeImpl();
	}
	
	abstract void testNumericalTypeChangeMinMaxImpl();

	abstract void testNumericalTypeAddRangeImpl();

	abstract void testNumericalTypeEditRangeImpl();

	abstract void testNumericalTypeRemoveRangeImpl();
}
