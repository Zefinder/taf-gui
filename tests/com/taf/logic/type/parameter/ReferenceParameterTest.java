package com.taf.logic.type.parameter;

import java.lang.reflect.InvocationTargetException;

class ReferenceParameterTest extends StringTypeParameterTest {

	public ReferenceParameterTest() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		super(ReferenceParameter.class, "ref");
	}

}
