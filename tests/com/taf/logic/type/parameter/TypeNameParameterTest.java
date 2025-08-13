package com.taf.logic.type.parameter;

import java.lang.reflect.InvocationTargetException;

class TypeNameParameterTest extends StringTypeParameterTest {

	public TypeNameParameterTest() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		super(TypeNameParameter.class, "type");
	}

}
