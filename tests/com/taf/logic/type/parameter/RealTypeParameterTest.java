package com.taf.logic.type.parameter;

import java.lang.reflect.InvocationTargetException;

import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;

abstract class RealTypeParameterTest extends TypeParameterTest {

	private static final double DEFAULT_REAL_VALUE = 0d;
	private static final String DEFAULT_STRING_VALUE = "0";

	public RealTypeParameterTest(Class<? extends TypeParameter> typeParameterClass, String name,
			MinMaxTypeParameterType parameterType) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		super(createTypeParameter(typeParameterClass), name, DEFAULT_STRING_VALUE, parameterType);
	}
	
	@Override
	void testTypeParameterValueImpl() {
		assertTypeParameterValue("3.0", "3");
		assertTypeParameterValue("-5.0", "-5");
		assertTypeParameterValue("2.3");
		assertBadTypeParameterValue("a");
	}
	
	private static final TypeParameter createTypeParameter(Class<? extends TypeParameter> typeParameterClass)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		return typeParameterClass.getConstructor(double.class).newInstance(DEFAULT_REAL_VALUE);
	}
	
}
