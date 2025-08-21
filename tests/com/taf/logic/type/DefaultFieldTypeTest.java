package com.taf.logic.type;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.HashSet;

class DefaultFieldTypeTest extends FieldTypeTest {

	public DefaultFieldTypeTest() {
		super(new DefaultFieldType(), "");
	}

	@Override
	void testFieldTypeDefaultValuesImpl() {
		// Nothing here
	}

	@Override
	void testFieldTypeMandatoryParametersImpl() {
		assertIterableEquals(new HashSet<String>(), fieldType.getMandatoryParametersName());
	}

	@Override
	void testFieldTypeOptionalParametersImpl() {
		assertIterableEquals(new HashSet<String>(), fieldType.getOptionalParametersName());
	}

}
