package com.taf.logic.type.parameter;

import java.lang.reflect.InvocationTargetException;

import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;

class MinIntegerParameterTest extends IntegerTypeParameterTest {

	public MinIntegerParameterTest() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		super(MinIntegerParameter.class, "min", MinMaxTypeParameterType.INTEGER);
	}

}
