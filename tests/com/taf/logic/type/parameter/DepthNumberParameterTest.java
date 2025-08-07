package com.taf.logic.type.parameter;

import java.lang.reflect.InvocationTargetException;

import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;

class DepthNumberParameterTest extends IntegerTypeParameterTest {

	public DepthNumberParameterTest() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		super(DepthNumberParameter.class, "depth", MinMaxTypeParameterType.NONE);
	}

}
