package com.taf.logic.type.parameter;

import java.lang.reflect.InvocationTargetException;

import com.taf.exception.ParseException;
import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;

abstract class IntegerTypeParameterTest extends TypeParameterTest {

	private static final int DEFAULT_INT_VALUE = 0;
	private static final String DEFAULT_STRING_VALUE = "0";

	public IntegerTypeParameterTest(Class<? extends TypeParameter> typeParameterClass, String name,
			MinMaxTypeParameterType parameterType) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		super(createTypeParameter(typeParameterClass), name, DEFAULT_STRING_VALUE, parameterType);
	}

	@Override
	void testTypeParameterValueImpl() throws ParseException {
		assertTypeParameterValue("3");
		assertTypeParameterValue("-5");
		assertBadTypeParameterValue("2.3");
		assertBadTypeParameterValue("a");
	}

	private static final TypeParameter createTypeParameter(Class<? extends TypeParameter> typeParameterClass)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		return typeParameterClass.getConstructor(int.class).newInstance(DEFAULT_INT_VALUE);
	}

}
