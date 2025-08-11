package com.taf.logic.type.parameter;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;
import com.taf.util.Pair;

abstract class RealTypeParameterTest extends TypeParameterTest {

	private static final double DEFAULT_REAL_VALUE = 0d;
	private static final String DEFAULT_STRING_VALUE = "0";

	public RealTypeParameterTest(Class<? extends TypeParameter> typeParameterClass, String name,
			MinMaxTypeParameterType parameterType) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		super(createTypeParameter(typeParameterClass), name, DEFAULT_STRING_VALUE, parameterType);
	}
	
	@Override
	public Stream<Pair<String, String>> valueProvider() {
		return Stream.of(
				new Pair<String, String>("3", "3"), 
				new Pair<String, String>("-5", "-5"), 
				new Pair<String, String>("0", "0"),
				new Pair<String, String>("2.3", "2.3")
			);
	}

	@Override
	public Stream<String> badValueProvider() {
		return Stream.of("a", "", null);
	}
	
	private static final TypeParameter createTypeParameter(Class<? extends TypeParameter> typeParameterClass)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		return typeParameterClass.getConstructor(double.class).newInstance(DEFAULT_REAL_VALUE);
	}
	
}
