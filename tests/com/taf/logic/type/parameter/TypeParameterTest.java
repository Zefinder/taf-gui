package com.taf.logic.type.parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import com.taf.exception.ParseException;
import com.taf.logic.FactoryTest;
import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;
import com.taf.util.Consts;

abstract class TypeParameterTest extends FactoryTest {

	protected TypeParameter typeParameter;
	private String name;
	private String defaultValue;
	private MinMaxTypeParameterType parameterType;

	public TypeParameterTest(TypeParameter typeParameter, String name, String defaultValue,
			MinMaxTypeParameterType parameterType) {
		this.typeParameter = typeParameter;
		this.name = name;
		this.defaultValue = defaultValue;
		this.parameterType = parameterType;
	}

	@Override
	@Test
	public void testFactoryCreation() throws ParseException {
		TypeParameter created = TypeParameterFactory.createTypeParameter(name, defaultValue, parameterType);
		assertEquals(typeParameter, created);
	}

	@Test
	void testTypeParameterToString() {
		assertEquals(Consts.formatParameter(typeParameter.name, typeParameter.valueToString()),
				typeParameter.toString());
	}

	@TestFactory
	Iterable<DynamicTest> testTypeParameterValue() {
		return valueProvider().<DynamicTest>map(pair -> DynamicTest.dynamicTest(createTestName(pair.getValue()),
				() -> assertTypeParameterValue(pair.getKey(), pair.getValue()))).toList();
	}

	@TestFactory
	Iterable<DynamicTest> testTypeParameterBadValue() {
		return badValueProvider().<DynamicTest>map(
				value -> DynamicTest.dynamicTest(createTestName(value), () -> assertBadTypeParameterValue(value)))
				.toList();
	}

	@Test
	void testTypeParameterHashCode() {
		assertEquals((typeParameter.getClass().toString() + Consts.HASH_SEPARATOR + typeParameter.name
				+ typeParameter.valueToString()).hashCode(), typeParameter.hashCode());
	}

	protected void assertTypeParameterValue(String value) {
		super.assertParameterValue(typeParameter, value);
	}

	protected void assertTypeParameterValue(String value, String expected) {
		super.assertParameterValue(typeParameter, value, expected);
	}

	protected void assertBadTypeParameterValue(String value) {
		super.assertTypeParameterValue(typeParameter, value);
	}
}
