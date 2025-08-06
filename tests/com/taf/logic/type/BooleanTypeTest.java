package com.taf.logic.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import com.taf.logic.type.parameter.ValuesParameter;
import com.taf.logic.type.parameter.WeightsParameter;
import com.taf.util.Consts;
import com.taf.util.HashSetBuilder;

class BooleanTypeTest extends TypeTest {
	
	private BooleanType type;
	
	public BooleanTypeTest() {
		type = new BooleanType();
	}
	
	@Override
	void testTypeDefaultValuesImpl() {
		assertEquals(Consts.DEFAULT_WEIGHT_VALUE, type.getFalseWeight());
		assertEquals(Consts.DEFAULT_WEIGHT_VALUE, type.getTrueWeight());
	}
	
	@Override
	void testTypeMandatoryParametersImpl() {
		assertIterableEquals(new HashSet<String>(), type.getMandatoryParametersName());
	}

	@Override
	void testTypeOptionalParametersImpl() {
		HashSet<String> optionalTypeParameters = new HashSetBuilder<String>()
				.add(ValuesParameter.PARAMETER_NAME).add(WeightsParameter.PARAMETER_NAME).build();
		assertIterableEquals(optionalTypeParameters, type.getOptionalParametersName());
	}
	
	@Test
	void testEditTrueFalseWeights() {
		type.editFalseWeight(Consts.DEFAULT_WEIGHT_VALUE + 1);
		assertEquals(Consts.DEFAULT_WEIGHT_VALUE + 1, type.getFalseWeight());
		
		type.editTrueWeight(Consts.DEFAULT_WEIGHT_VALUE + 1);
		assertEquals(Consts.DEFAULT_WEIGHT_VALUE + 1, type.getTrueWeight());
	}

}
