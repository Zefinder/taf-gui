package com.taf.logic.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import com.taf.logic.type.parameter.WeightsParameter;
import com.taf.util.Consts;
import com.taf.util.HashSetBuilder;

class BooleanTypeTest extends TypeTest {

	private BooleanType booleanType;

	public BooleanTypeTest() {
		super(new BooleanType(), "boolean");
		booleanType = (BooleanType) fieldType;
	}

	@Override
	void testTypeDefaultValuesImpl() {
		assertEquals(Consts.DEFAULT_WEIGHT_VALUE, booleanType.getFalseWeight());
		assertEquals(Consts.DEFAULT_WEIGHT_VALUE, booleanType.getTrueWeight());
	}

	@Override
	void testTypeMandatoryParametersImpl() {
		assertIterableEquals(new HashSet<String>(), booleanType.getMandatoryParametersName());
	}

	@Override
	void testTypeOptionalParametersImpl() {
		HashSet<String> optionalTypeParameters = new HashSetBuilder<String>().add(WeightsParameter.PARAMETER_NAME)
				.build();
		assertIterableEquals(optionalTypeParameters, booleanType.getOptionalParametersName());
	}

	@Test
	void testEditTrueFalseWeights() {
		booleanType.editFalseWeight(Consts.DEFAULT_WEIGHT_VALUE + 1);
		assertEquals(Consts.DEFAULT_WEIGHT_VALUE + 1, booleanType.getFalseWeight());

		booleanType.editTrueWeight(Consts.DEFAULT_WEIGHT_VALUE + 1);
		assertEquals(Consts.DEFAULT_WEIGHT_VALUE + 1, booleanType.getTrueWeight());
	}

}
