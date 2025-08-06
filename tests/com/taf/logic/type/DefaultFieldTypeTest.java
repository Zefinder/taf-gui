package com.taf.logic.type;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.HashSet;

class DefaultFieldTypeTest extends TypeTest {

	private DefaultFieldType defaultType;

	public DefaultFieldTypeTest() {
		defaultType = new DefaultFieldType();
	}

	@Override
	void testTypeDefaultValuesImpl() {
		// Nothing here
	}

	@Override
	void testTypeMandatoryParametersImpl() {
		assertIterableEquals(new HashSet<String>(), defaultType.getMandatoryParametersName());
	}

	@Override
	void testTypeOptionalParametersImpl() {
		assertIterableEquals(new HashSet<String>(), defaultType.getOptionalParametersName());
	}

}
