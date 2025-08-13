package com.taf.logic.type.parameter;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.taf.logic.ListTest;
import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;
import com.taf.util.Pair;

abstract class ListTypeParameterTest<K> extends TypeParameterTest implements ListTest<K> {

	private static final String DEFAULT_STRING_VALUE = "";

	public ListTypeParameterTest(TypeParameter typeParameter, String name) {
		super(typeParameter, name, DEFAULT_STRING_VALUE, MinMaxTypeParameterType.NONE);
	}

	@Override
	public Stream<Pair<String, String>> valueProvider() {
		return listProvider().<Pair<String, String>>map(
				values -> new Pair<String, String>(valuesToStringList(values), valuesToStringList(values)));
	}

	@TestFactory
	Iterable<DynamicTest> testListTypeParameterValue() {
		return listProvider()
				.<DynamicTest>map(values -> DynamicTest.dynamicTest(createTestName(valuesToStringList(values)),
						() -> {
							listProviderConsumer().accept(values);
							assertTypeParameterValue(valuesToStringList(values), typeParameter.valueToString());
							valuesRemover().accept(values);
						}))
				.toList();
	}
	
}
