package com.taf.logic.field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.HashSet;
import java.util.LinkedHashSet;

import org.junit.jupiter.api.Test;

import com.taf.logic.constraint.Constraint;
import com.taf.logic.type.DefaultFieldType;
import com.taf.logic.type.NodeType;

class RootTest extends FieldTest {

	private Root root;
	
	public RootTest() {
		super(new Root(name));
		root = (Root) field;
	}
	
	@Override
	void testFieldDefaultValuesImpl() {
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
		root.setType("type");
		assertFalse(root.hasType());
		assertFalse(root.hasRef());
		
		root.removeType();
		root.setReference("ref");
		assertFalse(root.hasType());
		assertFalse(root.hasRef());
	}
	
	@Override
	@Test
	void testFieldSetParent() {
		Root root2 = new Root("a");
		
		root.setParent(root2);
		assertNull(root.getParent());
	}
	
	@Test
	void testRootAddField() {
		Field parameter = new Parameter("param", new DefaultFieldType());
		root.addEntity(parameter);
		
		assertEquals(root, parameter.getParent());
		
		HashSet<Field> expected = new LinkedHashSet<Field>();
		expected.add(parameter);
		assertIterableEquals(expected, root.getFieldSet());
	}
	
	@Test
	void testRootAddFields() {
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
		Constraint constraint = new Constraint("constr");
		root.addEntity(constraint);
		
		assertEquals(root, constraint.getParent());
		
		HashSet<Constraint> expected = new LinkedHashSet<Constraint>();
		expected.add(constraint);
		assertIterableEquals(expected, root.getConstraintSet());
	}
	
	@Test
	void testRootAddType() {
		Type type = new Type("type");
		root.addEntity(type);
		
		assertEquals(root, type.getParent());
		
		HashSet<Type> expected = new LinkedHashSet<Type>();
		expected.add(type);
		assertIterableEquals(expected, root.getTypeSet());
	}
	
	@Test
	void testRootToString() {
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
		assertEquals(expected, root.toString());
	}
}
