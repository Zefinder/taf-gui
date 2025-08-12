package com.taf.logic.type.parameter;

import java.lang.reflect.InvocationTargetException;

import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;

class MinInstanceParameterTest extends PositiveTypeParameterTest {

	public MinInstanceParameterTest() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		super(MinInstanceParameter.class, "min", MinMaxTypeParameterType.INSTANCE);
	}

}
