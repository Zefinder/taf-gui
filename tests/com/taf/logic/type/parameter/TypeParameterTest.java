package com.taf.logic.type.parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.taf.exception.ParseException;
import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;
import com.taf.util.Consts;

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
	void testTypeParameterValue() throws ParseException {
		testTypeParameterValueImpl();
	}

	@Test
	void testTypeParameterToString() {
		assertEquals(formatTypeParameter(typeParameter.name, typeParameter.valueToString()), typeParameter.toString());
	}

	abstract void testTypeParameterValueImpl() throws ParseException;

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

	private String formatTypeParameter(String typeParameterName, String value) {
		return Consts.PARAMETER_STRING_FORMAT.formatted(typeParameterName, value);
	}
}
