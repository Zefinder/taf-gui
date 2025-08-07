package com.taf.logic.type.parameter;

import java.lang.reflect.InvocationTargetException;

import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;

class MeanParameterTest extends RealTypeParameterTest {

	public MeanParameterTest() throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		super(MeanParameter.class, "mean", MinMaxTypeParameterType.NONE);
	}

}
