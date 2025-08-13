package com.taf.logic.type.parameter;

import java.lang.reflect.InvocationTargetException;

import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;

class MaxRealParameterTest extends RealTypeParameterTest {

	public MaxRealParameterTest() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		super(MaxRealParameter.class, "max", MinMaxTypeParameterType.REAL);
	}

}
