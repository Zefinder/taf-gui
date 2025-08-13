package com.taf.logic.type.parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;
import com.taf.util.Pair;

abstract class PositiveTypeParameterTest extends IntegerTypeParameterTest {

	public PositiveTypeParameterTest(Class<? extends TypeParameter> typeParameterClass, String name,
			MinMaxTypeParameterType parameterType) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		super(typeParameterClass, name, parameterType);
	}

	@Override
	public Stream<Pair<String, String>> valueProvider() {
		return Stream.of(
				new Pair<String, String>("3", "3"), 
				new Pair<String, String>("-5", "0"), 
				new Pair<String, String>("0", "0")
			);
	}
	
	@Test
	void testPositiveTypeParameterNegativeConstructorValue() throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		TypeParameter newParameter = typeParameter.getClass().getConstructor(int.class).newInstance(-1);
		assertEquals("0", newParameter.valueToString());
	}

}
