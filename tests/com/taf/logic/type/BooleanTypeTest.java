package com.taf.logic.type;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.taf.manager.ConstantManager;

class BooleanTypeTest extends TypeTest {
	
	private BooleanType type;
	
	public BooleanTypeTest() {
		type = new BooleanType();
	}
	
	@Override
	void testTypeDefaultValuesImpl() {
		assertEquals(ConstantManager.DEFAULT_WEIGHT_VALUE, type.getFalseWeight());
		assertEquals(ConstantManager.DEFAULT_WEIGHT_VALUE, type.getTrueWeight());
	}
	
	@Test
	void testEditTrueFalseWeights() {
		type.editFalseWeight(ConstantManager.DEFAULT_WEIGHT_VALUE + 1);
		assertEquals(ConstantManager.DEFAULT_WEIGHT_VALUE + 1, type.getFalseWeight());
		
		type.editTrueWeight(ConstantManager.DEFAULT_WEIGHT_VALUE + 1);
		assertEquals(ConstantManager.DEFAULT_WEIGHT_VALUE + 1, type.getTrueWeight());
	}

}
