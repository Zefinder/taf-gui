package com.taf.logic;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.taf.util.Consts;

public interface ListTest<K> {

	/**
	 * Provides a list of values of the specified type not in String representation
	 * that will be input to the type parameter. This list is used with
	 * {@link #listProviderConsumer()}.
	 */
	Stream<List<K>> listProvider();

	/**
	 * Provides a consumer for a list of values (given by {@link #listProvider()})
	 * to input the values in the type parameter.
	 */
	Consumer<List<K>> listProviderConsumer();
	
	/**
	 * Provides a way to remove the values inputed in the type parameter after the
	 * test. This is needed because JUnit 5's dynamic test life cycle is not the
	 * same as a normal test and so a new object is not created each time.
	 * 
	 * Only instantiate if this is used with a {@link FactoryTest}.
	 */
	abstract Consumer<List<K>> valuesRemover();
	
	default String valuesToStringList(List<K> values) {
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
