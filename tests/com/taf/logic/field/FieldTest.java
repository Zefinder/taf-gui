package com.taf.logic.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

abstract class FieldTest {

	protected static final String name = "test";
	
	protected Field field;
	
	public FieldTest(Field field) {
		this.field = field;
	}
	
	@Test
	void testFieldSetParent() {
		Root root = new Root("a");
		
		root.addEntity(field);
		assertEquals(root, field.getParent());
		assertEquals(1, field.indentationLevel);
	}
	
	@Test
	void testFieldDefaultValues() {
		testFieldDefaultValuesImpl();
	}
	
	abstract void testFieldDefaultValuesImpl();

}
