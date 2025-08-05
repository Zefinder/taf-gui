package com.taf.logic.type;

import org.junit.jupiter.api.Test;

abstract class TypeTest {
	
	@Test
	void testTypeDefaultValues() {
		testTypeDefaultValuesImpl();
	}
	
	abstract void testTypeDefaultValuesImpl();

}
