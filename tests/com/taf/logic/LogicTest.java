package com.taf.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import com.taf.exception.ParseException;

public abstract class LogicTest {

	private static final String PARSE_ERROR = "The value couldn't be parsed!";
	
	protected void assertParameterValue(Parsable parameter, String value) {
		try {
			parameter.stringToValue(value);
		} catch (ParseException e) {
			fail(PARSE_ERROR);
		}

		assertEquals(value, parameter.valueToString());
	}

	protected void assertParameterValue(Parsable parameter, String value, String expected) {
		try {
			parameter.stringToValue(value);
		} catch (ParseException e) {
			fail(PARSE_ERROR);
		}

		assertEquals(expected, parameter.valueToString());
	}

	protected void assertTypeParameterValue(Parsable parameter, String value) {
		assertThrows(ParseException.class, () -> parameter.stringToValue(value));
	}

}
