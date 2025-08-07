package com.taf.logic.type.parameter;

import java.lang.reflect.InvocationTargetException;

import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;

class MaxIntegerParameterTest extends IntegerTypeParameterTest {

	public MaxIntegerParameterTest() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		super(MaxIntegerParameter.class, "max", MinMaxTypeParameterType.INTEGER);
	}

}
