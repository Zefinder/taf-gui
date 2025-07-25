package com.taf.logic.type;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.taf.logic.type.parameter.DistributionType;
import com.taf.manager.ConstantManager;

abstract class NumericalTypeTest {

	private NumericalType numericalType;

	public NumericalTypeTest(NumericalType numericalType) {
		this.numericalType = numericalType;
	}

	@Test
	void testNumericalTypeDefaultValues() {
		assertEquals(ConstantManager.DEFAULT_MIN_VALUE, numericalType.getMinNumber());
		assertEquals(ConstantManager.DEFAULT_MAX_VALUE, numericalType.getMaxNumber());
		assertEquals(ConstantManager.DEFAULT_DISTRIBUTION, numericalType.getDistribution());
		assertEquals(ConstantManager.DEFAULT_MEAN_VALUE, numericalType.getMean());
		assertEquals(ConstantManager.DEFAULT_VARIANCE_VALUE, numericalType.getVariance());
		assertEquals(0, numericalType.getRangeNumber());
		assertEquals(0, numericalType.getWeights().length);
	}

	@ParameterizedTest
	@EnumSource(value = DistributionType.class)
	void testNumericalTypeChangeDistribution(DistributionType distributionType) {
		numericalType.setDistribution(distributionType);
		assertEquals(distributionType, numericalType.getDistribution());
	}

	@Test
	void testNumericalTypeChangeMeanVariance() {
		numericalType.editMean(ConstantManager.DEFAULT_MEAN_VALUE + 1.5);
		numericalType.editVariance(ConstantManager.DEFAULT_VARIANCE_VALUE + 1.5);

		assertEquals(ConstantManager.DEFAULT_MEAN_VALUE + 1.5, numericalType.getMean());
		assertEquals(ConstantManager.DEFAULT_VARIANCE_VALUE + 1.5, numericalType.getVariance());
	}

	abstract void testNumericalTypeChangeMinMax();

	abstract void testNumericalTypeAddRange();

	abstract void testNumericalTypeEditRange();

	abstract void testNumericalTypeRemoveRange();
}
