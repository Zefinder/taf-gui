package com.taf.logic.type.parameter;

import java.lang.reflect.InvocationTargetException;

import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;

class MinDepthParameterTest extends PositiveTypeParameterTest {

	public MinDepthParameterTest() throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		super(MinDepthParameter.class, "min_depth", MinMaxTypeParameterType.NONE);
	}

}
