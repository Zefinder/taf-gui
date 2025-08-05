package com.taf.logic.field;

import static org.junit.Assert.assertFalse;
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
import com.taf.logic.type.NodeType;

class NodeTest extends FieldTest {

	private Node node;
	
	public NodeTest() {
		super(new Node(name));
		node = (Node) field;
	}
	
	@Override
	void testFieldDefaultValuesImpl() {
		// Everything should be empty and parent null
		assertEquals(name, node.getName());
		assertNull(node.getParent());
		assertInstanceOf(NodeType.class, node.getType());
		assertFalse(node.hasType());
		assertFalse(node.hasRef());
		assertEquals(0, node.getFieldSet().size());
		assertEquals(0, node.getConstraintSet().size());
	}
	
	@Test
	void testNodeRecursive() {
		node.setType("type");
		assertTrue(node.hasType());
		assertFalse(node.hasRef());
		
		node.removeType();
		assertFalse(node.hasType());
		assertFalse(node.hasRef());
		
		node.setReference("ref");
		assertFalse(node.hasType());
		assertTrue(node.hasRef());
	}
	
	@Test
	void testNodeAddField() {
		Field parameter = new Parameter("param", new DefaultFieldType());
		node.addEntity(parameter);
		
		assertEquals(node, parameter.getParent());
		
		HashSet<Field> expected = new LinkedHashSet<Field>();
		expected.add(parameter);
		assertIterableEquals(expected, node.getFieldSet());
	}
	
	@Test
	void testNodeAddFields() {
		Field parameter = new Parameter("param", new DefaultFieldType());
		node.addEntity(parameter);
		
		Field node2 = new Node("node");
		node.addEntity(node2);
		
		assertEquals(node, parameter.getParent());
		assertEquals(node, node2.getParent());
		
		HashSet<Field> expected = new LinkedHashSet<Field>();
		expected.add(parameter);
		expected.add(node2);
		assertIterableEquals(expected, node.getFieldSet());
	}
	
	@Test
	void testNodeAddConstraint() {
		Constraint constraint = new Constraint("constr");
		node.addEntity(constraint);
		
		assertEquals(node, constraint.getParent());
		
		HashSet<Constraint> expected = new LinkedHashSet<Constraint>();
		expected.add(constraint);
		assertIterableEquals(expected, node.getConstraintSet());
	}
	
	@Test
	void testNodeAddType() {
		Type type = new Type("type");
		node.addEntity(type);
		
		assertEquals(null, type.getParent());
		assertTrue(node.getFieldSet().isEmpty());
		assertTrue(node.getConstraintSet().isEmpty());
	}
}
