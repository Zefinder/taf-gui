package com.taf.logic.type.parameter;

import java.lang.reflect.InvocationTargetException;

import com.taf.exception.ParseException;
import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;

abstract class StringTypeParameterTest extends TypeParameterTest {

	private static final String DEFAULT_STRING_VALUE = "a";

	public StringTypeParameterTest(Class<? extends TypeParameter> typeParameterClass, String name)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		super(createTypeParameter(typeParameterClass), name, DEFAULT_STRING_VALUE, MinMaxTypeParameterType.NONE);
	}

	@Override
	void testTypeParameterValueImpl() throws ParseException {
		assertTypeParameterValue("baba");
		assertTypeParameterValue("sls");
		assertBadTypeParameterValue("");
	}

	private static final TypeParameter createTypeParameter(Class<? extends TypeParameter> typeParameterClass)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		return typeParameterClass.getConstructor(String.class).newInstance(DEFAULT_STRING_VALUE);
	}

}
