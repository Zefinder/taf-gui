package com.taf.logic.type.parameter;

import java.lang.reflect.InvocationTargetException;

import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;

class MaxInstanceParameterTest extends IntegerTypeParameterTest {

	public MaxInstanceParameterTest() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		super(MaxInstanceParameter.class, "max", MinMaxTypeParameterType.INSTANCE);
	}

}
