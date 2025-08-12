package com.taf.logic.type.parameter;

import java.lang.reflect.InvocationTargetException;

import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;

class InstanceNumberParameterTest extends PositiveTypeParameterTest {

	public InstanceNumberParameterTest() throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		super(InstanceNumberParameter.class, "nb_instances", MinMaxTypeParameterType.NONE);
	}

}
