package com.taf.logic.type;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.taf.util.Consts;

abstract class FieldTypeTest {

	protected FieldType fieldType;
	private String name;

	public FieldTypeTest(FieldType fieldType, String name) {
		this.fieldType = fieldType;
		this.name = name;
	}

	@Test
	void testFieldTypeName() {
		assertEquals(fieldType.getName(), name);
	}

	@Test
	void testFieldTypeDefaultValues() {
		testFieldTypeDefaultValuesImpl();
	}

	@Test
	void testFieldTypeMandatoryParameters() {
		testFieldTypeMandatoryParametersImpl();
	}

	@Test
	void testFieldTypeOptionalParameters() {
		testFieldTypeOptionalParametersImpl();
	}

	@Test
	void testFieldTypeHashCode() {
		assertEquals(
				(fieldType.getClass().toString() + Consts.HASH_SEPARATOR + fieldType.getName() + fieldType.toString())
						.hashCode(),
				fieldType.hashCode());
	}

	abstract void testFieldTypeDefaultValuesImpl();

	abstract void testFieldTypeMandatoryParametersImpl();

	abstract void testFieldTypeOptionalParametersImpl();

}
