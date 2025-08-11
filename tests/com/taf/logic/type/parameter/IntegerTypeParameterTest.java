package com.taf.logic.type.parameter;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;
import com.taf.util.Pair;

abstract class IntegerTypeParameterTest extends TypeParameterTest {

	private static final int DEFAULT_INT_VALUE = 0;
	private static final String DEFAULT_STRING_VALUE = "0";

	public IntegerTypeParameterTest(Class<? extends TypeParameter> typeParameterClass, String name,
			MinMaxTypeParameterType parameterType) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		super(createTypeParameter(typeParameterClass), name, DEFAULT_STRING_VALUE, parameterType);
	}

	@Override
	public Stream<Pair<String, String>> valueProvider() {
		return Stream.of(
				new Pair<String, String>("3", "3"), 
				new Pair<String, String>("-5", "-5"), 
				new Pair<String, String>("0", "0")
			);
	}

	@Override
	public Stream<String> badValueProvider() {
		return Stream.of("2.3", "a", "", null);
	}

	private static final TypeParameter createTypeParameter(Class<? extends TypeParameter> typeParameterClass)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		return typeParameterClass.getConstructor(int.class).newInstance(DEFAULT_INT_VALUE);
	}

}
