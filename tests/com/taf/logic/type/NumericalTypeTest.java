package com.taf.logic.type;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.taf.logic.type.parameter.DistributionType;
import com.taf.manager.ConstantManager;

abstract class NumericalTypeTest extends TypeTest {

	protected NumericalType type;

	public NumericalTypeTest(NumericalType type) {
		this.type = type;
	}

	@Test
	@Override
	void testTypeDefaultValues() {
		assertEquals(ConstantManager.DEFAULT_MIN_VALUE, type.getMinNumber());
		assertEquals(ConstantManager.DEFAULT_MAX_VALUE, type.getMaxNumber());
		assertEquals(ConstantManager.DEFAULT_DISTRIBUTION, type.getDistribution());
		assertEquals(ConstantManager.DEFAULT_MEAN_VALUE, type.getMean());
		assertEquals(ConstantManager.DEFAULT_VARIANCE_VALUE, type.getVariance());
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
		type.editMean(ConstantManager.DEFAULT_MEAN_VALUE + 1.5);
		type.editVariance(ConstantManager.DEFAULT_VARIANCE_VALUE + 1.5);

		assertEquals(ConstantManager.DEFAULT_MEAN_VALUE + 1.5, type.getMean());
		assertEquals(ConstantManager.DEFAULT_VARIANCE_VALUE + 1.5, type.getVariance());
	}

	abstract void testNumericalTypeChangeMinMax();

	abstract void testNumericalTypeAddRange();

	abstract void testNumericalTypeEditRange();

	abstract void testNumericalTypeRemoveRange();
}
