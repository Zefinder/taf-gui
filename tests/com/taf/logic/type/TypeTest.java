package com.taf.logic.type;

import org.junit.jupiter.api.Test;

abstract class TypeTest {
	
	@Test
	void testTypeDefaultValues() {
		testTypeDefaultValuesImpl();
	}
	
	@Test
	void testMandatoryParameters() {
		testTypeMandatoryParametersImpl();
	}
	
	@Test
	void testOptionalParameters() {
		testTypeOptionalParametersImpl();
	}
	
	abstract void testTypeDefaultValuesImpl();

	abstract void testTypeMandatoryParametersImpl();
	
	abstract void testTypeOptionalParametersImpl();
	
}
