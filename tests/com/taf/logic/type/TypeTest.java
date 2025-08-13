package com.taf.logic.type;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.taf.util.Consts;

abstract class TypeTest {

	protected FieldType fieldType;
	private String name;

	public TypeTest(FieldType fieldType, String name) {
		this.fieldType = fieldType;
		this.name = name;
	}

	@Test
	void testTypeName() {
		assertEquals(fieldType.getName(), name);
	}

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

	@Test
	void testFieldHashCode() {
		assertEquals(
				(fieldType.getClass().toString() + Consts.HASH_SEPARATOR + fieldType.getName() + fieldType.toString())
						.hashCode(),
				fieldType.hashCode());
	}

	abstract void testTypeDefaultValuesImpl();

	abstract void testTypeMandatoryParametersImpl();

	abstract void testTypeOptionalParametersImpl();

}
