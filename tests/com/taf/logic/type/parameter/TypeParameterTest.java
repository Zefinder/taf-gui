package com.taf.logic.type.parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import com.taf.exception.ParseException;
import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;
import com.taf.util.Consts;
import com.taf.util.Pair;

abstract class TypeParameterTest {

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

	@Test
	void testTypeParameterFactoryCreation() throws ParseException {
		TypeParameter created = TypeParameterFactory.createTypeParameter(name, defaultValue, parameterType);
		assertEquals(typeParameter, created);
	}

	@Test
	void testTypeParameterToString() {
		assertEquals(formatTypeParameter(typeParameter.name, typeParameter.valueToString()), typeParameter.toString());
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

	/**
	 * Provider for values and expected values to put in
	 * {@link TypeParameter#stringToValue(String)}. The order in the pair is
	 * important: first the value to input and second the expected value.
	 */
	abstract Stream<Pair<String, String>> valueProvider();

	/**
	 * Same as {@link #valueProvider()} but with inputs throwing
	 * {@link ParseException}. The provider only needs the value since it is
	 * expected to throw an exception.
	 */
	abstract Stream<String> badValueProvider();

	protected void assertTypeParameterValue(String value) {
		try {
			typeParameter.stringToValue(value);
		} catch (ParseException e) {
			fail("The value couldn't be parsed!");
		}

		assertEquals(value, typeParameter.valueToString());
	}

	protected void assertTypeParameterValue(String value, String expected) {
		try {
			typeParameter.stringToValue(value);
		} catch (ParseException e) {
			fail("The value couldn't be parsed!");
		}
		
		assertEquals(expected, typeParameter.valueToString());
	}

	protected void assertBadTypeParameterValue(String value) {
		assertThrows(ParseException.class, () -> typeParameter.stringToValue(value));
	}

	protected String createTestName(String name) {
		String testName = name;
		if (testName == null) {
			testName = "<null>";
		} else if (testName.isBlank()) {
			testName = "<empty>";
		}

		return testName;
	}

	private String formatTypeParameter(String typeParameterName, String value) {
		return Consts.PARAMETER_STRING_FORMAT.formatted(typeParameterName, value);
	}
}
