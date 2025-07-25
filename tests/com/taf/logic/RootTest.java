package com.taf.logic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.HashSet;
import java.util.LinkedHashSet;

import org.junit.jupiter.api.Test;

import com.taf.logic.constraint.Constraint;
import com.taf.logic.field.Field;
import com.taf.logic.field.Node;
import com.taf.logic.field.Parameter;
import com.taf.logic.field.Root;
import com.taf.logic.field.Type;
import com.taf.logic.type.DefaultFieldType;
import com.taf.logic.type.NodeType;

class RootTest {

	@Test
	void testRootEmpty() {
		String name = "test";
		Root root = new Root(name);

		// Everything should be empty and parent null
		assertEquals(name, root.getName());
		assertNull(root.getParent());
		assertInstanceOf(NodeType.class, root.getType());
		assertFalse(root.hasType());
		assertFalse(root.hasRef());
		assertEquals(0, root.getFieldSet().size());
		assertEquals(0, root.getConstraintSet().size());
	}
	
	@Test
	void testRootRecursive() {
		// A root cannot be recursive
		String name = "test";
		Root root = new Root(name);
		
		root.setType("type");
		assertFalse(root.hasType());
		assertFalse(root.hasRef());
		
		root.removeType();
		root.setReference("ref");
		assertFalse(root.hasType());
		assertFalse(root.hasRef());
	}
	
	@Test
	void testRootAddParent() {
		String name = "test";
		Root root = new Root(name);
		Root root2 = new Root("a");
		
		root.setParent(root2);
		assertNull(root.getParent());
	}
	
	@Test
	void testRootAddField() {
		String name = "test";
		Root root = new Root(name);
		Field parameter = new Parameter("param", new DefaultFieldType());
		root.addEntity(parameter);
		
		assertEquals(root, parameter.getParent());
		
		HashSet<Field> expected = new LinkedHashSet<Field>();
		expected.add(parameter);
		assertIterableEquals(expected, root.getFieldSet());
	}
	
	@Test
	void testRootAddFields() {
		String name = "test";
		Root root = new Root(name);
		Field parameter = new Parameter("param", new DefaultFieldType());
		root.addEntity(parameter);
		
		Field node = new Node("node");
		root.addEntity(node);
		
		assertEquals(root, parameter.getParent());
		assertEquals(root, node.getParent());
		
		HashSet<Field> expected = new LinkedHashSet<Field>();
		expected.add(parameter);
		expected.add(node);
		assertIterableEquals(expected, root.getFieldSet());
	}
	
	@Test
	void testRootAddConstraint() {
		String name = "test";
		Root root = new Root(name);
		Constraint constraint = new Constraint("constr");
		root.addEntity(constraint);
		
		assertEquals(root, constraint.getParent());
		
		HashSet<Constraint> expected = new LinkedHashSet<Constraint>();
		expected.add(constraint);
		assertIterableEquals(expected, root.getConstraintSet());
	}
	
	@Test
	void testRootAddType() {
		String name = "test";
		Root root = new Root(name);
		Type type = new Type("type");
		root.addEntity(type);
		
		assertEquals(root, type.getParent());
		
		HashSet<Type> expected = new LinkedHashSet<Type>();
		expected.add(type);
		assertIterableEquals(expected, root.getTypeSet());
	}
	
	@Test
	void testRootToString() {
		String name = "test";
		Root root = new Root(name);
		Field parameter = new Parameter("param", new DefaultFieldType());
		Field node = new Node("node");
		Type type = new Type("type");
		Constraint constraint = new Constraint("constr");
		
		root.addEntity(parameter);
		root.addEntity(node);
		root.addEntity(type);
		root.addEntity(constraint);
		
		String expected = """
				<root name="test">
				\t<type name="type">
				
				\t</type>
				\t<parameter name="param"/>				
				\t<node name="node" nb_instances="1">
				
				\t</node>
				\t<constraint name="constr"/>
				</root>""";
		System.out.println(root.toString());
		assertEquals(expected, root.toString());
	}
}
