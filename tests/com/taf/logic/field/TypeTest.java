package com.taf.logic.field;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.LinkedHashSet;

import org.junit.jupiter.api.Test;

import com.taf.logic.constraint.Constraint;
import com.taf.logic.type.DefaultFieldType;
import com.taf.manager.ConstantManager;

class TypeTest extends FieldTest {

	protected Type type;
	
	public TypeTest() {
		super(new Type(name));
		type = (Type) field;
	}
	
	@Override
	void testFieldDefaultValuesImpl() {
		assertEquals(name, type.getName());
		assertNull(type.getParent());
		assertInstanceOf(DefaultFieldType.class, type.getType());
		assertEquals(ConstantManager.TYPE_ENTITY_NAME, type.getEntityTypeName());
		assertEquals(0, type.getFieldSet().size());
		assertEquals(0, type.getConstraintSet().size());
	}

	@Test
	void testTypeAddField() {
		Field parameter = new Parameter("param", new DefaultFieldType());
		type.addEntity(parameter);
		
		assertEquals(type, parameter.getParent());
		
		HashSet<Field> expected = new LinkedHashSet<Field>();
		expected.add(parameter);
		assertIterableEquals(expected, type.getFieldSet());
	}
	
	@Test
	void testTypeAddFields() {
		Field parameter = new Parameter("param", new DefaultFieldType());
		type.addEntity(parameter);
		
		Field node2 = new Node("node");
		type.addEntity(node2);
		
		assertEquals(type, parameter.getParent());
		assertEquals(type, node2.getParent());
		
		HashSet<Field> expected = new LinkedHashSet<Field>();
		expected.add(parameter);
		expected.add(node2);
		assertIterableEquals(expected, type.getFieldSet());
	}
	
	@Test
	void testTypeAddConstraint() {
		Constraint constraint = new Constraint("constr");
		type.addEntity(constraint);
		
		assertEquals(type, constraint.getParent());
		
		HashSet<Constraint> expected = new LinkedHashSet<Constraint>();
		expected.add(constraint);
		assertIterableEquals(expected, type.getConstraintSet());
	}
	
	@Test
	void testTypeAddType() {
		Type type = new Type("type");
		this.type.addEntity(type);
		
		assertEquals(null, type.getParent());
		assertTrue(type.getFieldSet().isEmpty());
		assertTrue(type.getConstraintSet().isEmpty());
		assertEquals(0, type.indentationLevel);
	}
	
}
