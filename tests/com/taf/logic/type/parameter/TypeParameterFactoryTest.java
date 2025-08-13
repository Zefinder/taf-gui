package com.taf.logic.type.parameter;

import static org.junit.jupiter.api.Assertions.*;

import com.taf.exception.ParseException;

import org.junit.jupiter.api.Test;

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
