package com.taf.logic.type.parameter;

import java.lang.reflect.InvocationTargetException;

import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;

class MinRealParameterTest extends RealTypeParameterTest {

	public MinRealParameterTest() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		super(MinRealParameter.class, "min", MinMaxTypeParameterType.REAL);
	}

}
