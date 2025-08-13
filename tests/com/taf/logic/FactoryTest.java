package com.taf.logic;

import java.util.stream.Stream;

import com.taf.exception.ParseException;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.util.Pair;

public abstract class FactoryTest extends LogicTest {

	/**
	 * Provider for values and expected values to put in
	 * {@link TypeParameter#stringToValue(String)}. The order in the pair is
	 * important: first the value to input and second the expected value.
	 */
	protected abstract Stream<Pair<String, String>> valueProvider();

	/**
	 * Same as {@link #valueProvider()} but with inputs throwing
	 * {@link ParseException}. The provider only needs the value since it is
	 * expected to throw an exception.
	 */
	protected abstract Stream<String> badValueProvider();

	protected abstract void testFactoryCreation() throws Exception;

	protected String createTestName(String name) {
		String testName = name;
		if (testName == null) {
			testName = "<null>";
		} else if (testName.isBlank()) {
			testName = "<empty>";
		}

		return testName;
	}
	
}
