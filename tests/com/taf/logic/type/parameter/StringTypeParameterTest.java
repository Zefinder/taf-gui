package com.taf.logic.type.parameter;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;
import com.taf.util.Pair;

abstract class StringTypeParameterTest extends TypeParameterTest {

	private static final String DEFAULT_STRING_VALUE = "a";

	public StringTypeParameterTest(Class<? extends TypeParameter> typeParameterClass, String name)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		super(createTypeParameter(typeParameterClass), name, DEFAULT_STRING_VALUE, MinMaxTypeParameterType.NONE);
	}

	@Override
	Stream<Pair<String, String>> valueProvider() {
		return Stream.of(
				new Pair<String, String>("baba", "baba"), 
				new Pair<String, String>("sls", "sls"), 
				new Pair<String, String>("0", "0")
			);
	}

	@Override
	Stream<String> badValueProvider() {
		return Stream.of("", null);
	}

	private static final TypeParameter createTypeParameter(Class<? extends TypeParameter> typeParameterClass)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		return typeParameterClass.getConstructor(String.class).newInstance(DEFAULT_STRING_VALUE);
	}

}
