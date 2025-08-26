package com.taf.logic.type.parameter;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.taf.exception.ParseException;

class TypeParameterFactoryTest {

	@Test
	void testTypeParameterFactoryUnknownName() {
		assertThrows(ParseException.class, () -> TypeParameterFactory.createTypeParameter("taf", ""));
	}

	@Test
	void testTypeParameterFactoryMinNoType() {
		assertThrows(ParseException.class, () -> TypeParameterFactory.createTypeParameter("min", ""));
	}
	
	@Test
	void testTypeParameterFactoryMaxNoType() {
		assertThrows(ParseException.class, () -> TypeParameterFactory.createTypeParameter("max", ""));
	}
}
