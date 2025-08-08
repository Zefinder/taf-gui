package com.taf.logic.type.parameter;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;
import com.taf.util.Consts;
import com.taf.util.Pair;

abstract class ListTypeParameterTest<K> extends TypeParameterTest {

	private static final String DEFAULT_STRING_VALUE = "";

	public ListTypeParameterTest(TypeParameter typeParameter, String name) {
		super(typeParameter, name, DEFAULT_STRING_VALUE, MinMaxTypeParameterType.NONE);
	}

	@Override
	Stream<Pair<String, String>> valueProvider() {
		return listProvider()
				.<Pair<String, String>>map(values -> new Pair<String, String>(valuesToStringList(values), valuesToStringList(values)));
	}

	/**
	 * Provides a list of values of the specified type not in String representation
	 * that will be input to the type parameter. This list is used with
	 * {@link #listProviderConsumer()}.
	 * 
	 * This is used in {@link #testListTypeParameterValue()}, you don't have to call
	 * it.
	 */
	abstract Stream<List<K>> listProvider();

	/**
	 * Provides a consumer for a list of values (given by {@link #listProvider()})
	 * to input the values in the type parameter.
	 * 
	 * This is used in {@link #testListTypeParameterValue()}, you don't have to call
	 * it.
	 */
	abstract Consumer<List<K>> listProviderConsumer();
	
	/**
	 * Provides a way to remove the values inputed in the type parameter after the
	 * test. This is needed because JUnit 5's dynamic test life cycle is not the
	 * same as a normal test and so a new object is not created each time.
	 * 
	 * This is used in {@link #testListTypeParameterValue()}, you don't have to call
	 * it.
	 */
	abstract Consumer<List<K>> valuesRemover();

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
	
	private String valuesToStringList(List<K> values) {
		String stringList;
		if (values.size() == 0) {
			stringList = "";
		} else {
			stringList = values.get(0).toString();
			for (int i = 1; i < values.size(); i++) {
				stringList += Consts.ELEMENT_SEPARATOR + values.get(i).toString();
			}
		}

		return stringList;
	}
	
}
