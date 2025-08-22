package com.taf.logic.type;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.taf.logic.type.parameter.DistributionType;
import com.taf.util.Consts;

abstract class NumericalTypeTest extends FieldTypeTest {

	protected NumericalType numericalType;

	public NumericalTypeTest(NumericalType type, String name) {
		super(type, name);
		this.numericalType = (NumericalType) fieldType;
	}

	@Override
	void testFieldTypeDefaultValuesImpl() {
		assertEquals(Consts.DEFAULT_DISTRIBUTION, numericalType.getDistribution());
		assertEquals(Consts.DEFAULT_MEAN_VALUE, numericalType.getMean());
		assertEquals(Consts.DEFAULT_VARIANCE_VALUE, numericalType.getVariance());
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
		numericalType.editMean(Consts.DEFAULT_MEAN_VALUE + 1.5);
		numericalType.editVariance(Consts.DEFAULT_VARIANCE_VALUE + 1.5);

		assertEquals(Consts.DEFAULT_MEAN_VALUE + 1.5, numericalType.getMean());
		assertEquals(Consts.DEFAULT_VARIANCE_VALUE + 1.5, numericalType.getVariance());
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
