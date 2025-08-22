package com.taf.logic.field;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.LinkedHashSet;

import org.junit.jupiter.api.Test;

import com.taf.exception.EntityCreationException;
import com.taf.logic.constraint.Constraint;
import com.taf.logic.type.DefaultFieldType;
import com.taf.util.Consts;

class TypeTest extends FieldTest {
	
	protected Type type;

	public TypeTest() throws EntityCreationException {
		super(new Type(name));
		type = (Type) field;
	}

	@Override
	void testFieldDefaultValuesImpl() {
		assertEquals(name, type.getName());
		assertNull(type.getParent());
		assertInstanceOf(DefaultFieldType.class, type.getType());
		assertEquals(Consts.TYPE_ENTITY_NAME, type.getEntityTypeName());
		assertEquals(0, type.getFieldSet().size());
		assertEquals(0, type.getConstraintSet().size());
	}

	@Override
	void testFieldEditTypeImpl() {
		assertInstanceOf(DefaultFieldType.class, type.getType());
	}

	@Test
	void testTypeAddField() throws EntityCreationException {
		Field parameter = new Parameter("param", new DefaultFieldType());
		type.addEntity(parameter);

		assertEquals(type, parameter.getParent());

		HashSet<Field> expected = new LinkedHashSet<Field>();
		expected.add(parameter);
		assertIterableEquals(expected, type.getFieldSet());
	}

	@Test
	void testTypeAddFields() throws EntityCreationException {
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
	void testTypeAddConstraint() throws EntityCreationException {
		Constraint constraint = new Constraint("constr");
		type.addEntity(constraint);

		assertEquals(type, constraint.getParent());

		HashSet<Constraint> expected = new LinkedHashSet<Constraint>();
		expected.add(constraint);
		assertIterableEquals(expected, type.getConstraintSet());
	}

	@Test
	void testTypeAddType() throws EntityCreationException {
		Type type = new Type("type");
		this.type.addEntity(type);

		assertEquals(null, type.getParent());
		assertTrue(type.getFieldSet().isEmpty());
		assertTrue(type.getConstraintSet().isEmpty());
		assertEquals(0, type.indentationLevel);
	}

	@Test
	void testTypeRemoveField() throws EntityCreationException {
		Field parameter = new Parameter("param", new DefaultFieldType());
		type.addEntity(parameter);
		type.removeEntity(parameter);
		
		assertIterableEquals(new LinkedHashSet<Field>(), type.getFieldSet());
	}
	
	@Test
	void testTypeRemoveFields() throws EntityCreationException {
		Field parameter = new Parameter("param", new DefaultFieldType());
		type.addEntity(parameter);

		Field node2 = new Node("node");
		type.addEntity(node2);

		type.removeEntity(node2);
		type.removeEntity(parameter);

		assertIterableEquals(new LinkedHashSet<Field>(), type.getFieldSet());
	}

	@Test
	void testTypeRemoveConstraint() throws EntityCreationException {
		Constraint constraint = new Constraint("constr");
		type.addEntity(constraint);
		type.removeEntity(constraint);

		assertIterableEquals(new LinkedHashSet<Constraint>(), type.getConstraintSet());
	}

	@Test
	void testTypeAddModifyRemoveField() throws EntityCreationException {
		Field parameter = new Parameter("param", new DefaultFieldType());
		type.addEntity(parameter);
		parameter.setName("aaaa");
		type.removeEntity(parameter);
		
		assertIterableEquals(new LinkedHashSet<Field>(), type.getFieldSet());
	}
	
	@Test
	void testTypeAddModifyRemoveConstraint() throws EntityCreationException {
		Constraint constraint = new Constraint("constr");
		type.addEntity(constraint);
		constraint.setName("aaaa");
		type.removeEntity(constraint);

		assertIterableEquals(new LinkedHashSet<Constraint>(), type.getConstraintSet());
	}
	
	@Test
	void testTypeAddTwiceSameName() throws EntityCreationException {
		Field parameter1 = new Parameter("param", new DefaultFieldType());
		Field parameter2 = new Parameter("param", new DefaultFieldType());
		type.addEntity(parameter1);
		type.addEntity(parameter2);
	}
	
}
