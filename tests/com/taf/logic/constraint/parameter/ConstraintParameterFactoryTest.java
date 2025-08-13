package com.taf.logic.constraint.parameter;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.taf.exception.ParseException;

class ConstraintParameterFactoryTest {

	@Test
	void testTypeParameterFactoryUnknownName() {
		assertThrows(ParseException.class, () -> ConstraintParameterFactory.createConstraintParameter("taf", ""));
	}

}
