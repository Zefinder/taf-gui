package com.taf.logic.type.parameter;

import static org.junit.jupiter.api.Assertions.fail;

import com.taf.exception.ParseException;
import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;

class ValuesParameterTest extends TypeParameterTest {

	public ValuesParameterTest(TypeParameter typeParameter, String name, String defaultValue,
			MinMaxTypeParameterType parameterType) {
		super(typeParameter, name, defaultValue, parameterType);
	}

	@Override
	void testTypeParameterValueImpl() throws ParseException {
		fail("Not yet implemented");
		
	}	

}
