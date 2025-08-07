package com.taf.logic.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.taf.logic.type.IntegerType;

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
	void testFieldEditName() {
		String newName = name + "a";
		field.setName(newName);
		
		assertEquals(newName, field.getName());
		
		field.setName(""); // Should be ignored
		assertEquals(newName, field.getName());
	}
	
	@Test
	void testFieldEditType() {
		IntegerType type = new IntegerType();
		field.setType(type);
		testFieldEditTypeImpl();
	}
	
	@Test
	void testFieldDefaultValues() {
		testFieldDefaultValuesImpl();
	}
	
	abstract void testFieldDefaultValuesImpl();
	
	/**
	 * The type has already been changed to integer type when calling this
	 */
	abstract void testFieldEditTypeImpl();
	
}
