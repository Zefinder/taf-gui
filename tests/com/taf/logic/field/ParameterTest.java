package com.taf.logic.field;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.taf.exception.EntityCreationException;
import com.taf.logic.type.DefaultFieldType;
import com.taf.logic.type.IntegerType;

class ParameterTest extends FieldTest {

	private Parameter parameter;

	public ParameterTest() throws EntityCreationException {
		super(new Parameter(name, new DefaultFieldType()));
		parameter = (Parameter) field;
	}

	@Override
	void testFieldDefaultValuesImpl() {
		assertEquals(name, parameter.getName());
		assertNull(parameter.getParent());
		assertEquals(new DefaultFieldType().typeToString(), parameter.getEntityTypeName());
	}
	
	@Override
	void testFieldEditTypeImpl() {
		assertInstanceOf(IntegerType.class, parameter.getType());
	}

}
