package com.taf.logic.constraint.parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import com.taf.exception.ParseException;
import com.taf.logic.FactoryTest;
import com.taf.logic.ListTest;
import com.taf.util.Consts;
import com.taf.util.Pair;

abstract class ConstraintParameterTest<K> extends FactoryTest implements ListTest<K> {

	private static final String DEFAULT_VALUE = "";

	protected ConstraintParameter constraintParameter;
	private String name;

	public ConstraintParameterTest(ConstraintParameter constraintParameter, String name) {
		this.constraintParameter = constraintParameter;
		this.name = name;
	}

	@Override
	public Stream<Pair<String, String>> valueProvider() {
		return listProvider().<Pair<String, String>>map(
				values -> new Pair<String, String>(valuesToStringList(values), valuesToStringList(values)));
	}

	@Override
	@Test
	public void testFactoryCreation() throws ParseException {
		ConstraintParameter created = ConstraintParameterFactory.createConstraintParameter(name, DEFAULT_VALUE);
		assertEquals(constraintParameter, created);
	}

	@Test
	void testConstraintParameterToString() {
		assertEquals(Consts.formatParameter(constraintParameter.name, constraintParameter.valueToString()),
				constraintParameter.toString());
	}

	@TestFactory
	Iterable<DynamicTest> testConstraintParameterValue() {
		return valueProvider().<DynamicTest>map(pair -> DynamicTest.dynamicTest(createTestName(pair.getValue()),
				() -> assertConstraintParameterValue(pair.getKey(), pair.getValue()))).toList();
	}

	@TestFactory
	Iterable<DynamicTest> testConstraintParameterBadValue() {
		return badValueProvider().<DynamicTest>map(
				value -> DynamicTest.dynamicTest(createTestName(value), () -> assertBadConstraintParameterValue(value)))
				.toList();
	}
	
	@TestFactory
	Iterable<DynamicTest> testConstraintTypeParameterListValue() {
		return listProvider()
				.<DynamicTest>map(values -> DynamicTest.dynamicTest(createTestName(valuesToStringList(values)),
						() -> {
							listProviderConsumer().accept(values);
							assertConstraintParameterValue(valuesToStringList(values), constraintParameter.valueToString());
							valuesRemover().accept(values);
						}))
				.toList();
	}

	protected void assertConstraintParameterValue(String value) {
		super.assertParameterValue(constraintParameter, value);
	}

	protected void assertConstraintParameterValue(String value, String expected) {
		super.assertParameterValue(constraintParameter, value, expected);
	}

	protected void assertBadConstraintParameterValue(String value) {
		super.assertTypeParameterValue(constraintParameter, value);
	}

}
