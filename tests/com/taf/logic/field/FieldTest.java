package com.taf.logic.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.taf.logic.type.DefaultFieldType;
import com.taf.logic.type.IntegerType;
import com.taf.util.Consts;

abstract class FieldTest {

	protected static final String name = "test";

	protected Field field;

	public FieldTest(Field field) {
		this.field = field;
	}

	// TODO Add test hashcode

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

	@Test
	void testNullName() {
		field.setName(null);
		assertEquals(name, field.getName());
	}

	@Test
	void testEmptyNameConstructor() {
		// Any field will work since it requires a name
		Field field = new Parameter("", new DefaultFieldType());
		assertEquals(name, field.getName());
	}

	@Test
	void testNullNameConstructor() {
		// Any field will work since it requires a name
		Field field = new Parameter(null, new DefaultFieldType());
		assertEquals(name, field.getName());
	}

	@Test
	void testFieldHashCode() {
		assertEquals((this.getClass().toString() + Consts.HASH_SEPARATOR + field.getName() + -1).hashCode(),
				field.hashCode());
	}

	abstract void testFieldDefaultValuesImpl();

	/**
	 * The type has already been changed to integer type when calling this
	 */
	abstract void testFieldEditTypeImpl();

}
