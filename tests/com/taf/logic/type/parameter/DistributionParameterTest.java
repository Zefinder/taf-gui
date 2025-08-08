package com.taf.logic.type.parameter;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.stream.Stream;

import com.taf.exception.ParseException;
import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;
import com.taf.util.Pair;

class DistributionParameterTest extends TypeParameterTest {

	public DistributionParameterTest(TypeParameter typeParameter, String name, String defaultValue,
			MinMaxTypeParameterType parameterType) {
		super(typeParameter, name, defaultValue, parameterType);
	}
	
	@Override
	Stream<String> badValueProvider() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	Stream<Pair<String, String>> valueProvider() {
		// TODO Auto-generated method stub
		return null;
	}

}
