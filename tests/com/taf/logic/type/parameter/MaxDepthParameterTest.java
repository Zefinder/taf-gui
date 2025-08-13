package com.taf.logic.type.parameter;

import java.lang.reflect.InvocationTargetException;

import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;

class MaxDepthParameterTest extends PositiveTypeParameterTest {

	public MaxDepthParameterTest() throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		super(MaxDepthParameter.class, "max_depth", MinMaxTypeParameterType.NONE);
	}

}
