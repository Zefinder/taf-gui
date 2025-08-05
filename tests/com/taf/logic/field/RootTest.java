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
import com.taf.manager.ConstantManager;

class RootTest extends TypeTest {

	private Root root;
	
	public RootTest() {
		root = new Root(name);
		
		// Update hierarchy
		field = root;
		type = root;
	}
	
	@Override
	void testFieldDefaultValuesImpl() {
		assertEquals(name, type.getName());
		assertNull(type.getParent());
		assertInstanceOf(NodeType.class, type.getType());
		assertEquals(ConstantManager.NODE_ENTITY_NAME, type.getEntityTypeName());
		assertEquals(0, type.getFieldSet().size());
		assertEquals(0, type.getConstraintSet().size());
		assertFalse(root.hasType());
		assertFalse(root.hasRef());
		assertEquals(0, root.getTypeSet().size());
	}
	
	@Override
	void testFieldEditTypeImpl() {
		assertInstanceOf(NodeType.class, root.getType());
	}
	
	@Override
	@Test
	void testFieldSetParent() {
		Root root2 = new Root("a");
		
		root2.addEntity(root);
		assertNull(root.getParent());
		assertEquals(0, root.indentationLevel);
	}
	
	@Override
	@Test
	void testTypeAddType() {
		Type type = new Type("type");
		root.addEntity(type);
		
		assertEquals(root, type.getParent());
		
		HashSet<Type> expected = new LinkedHashSet<Type>();
		expected.add(type);
		assertIterableEquals(expected, root.getTypeSet());
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
	
	@Test
	void testRootRemoveType() {
		Type type = new Type("type");
		root.addEntity(type);
		root.removeEntity(type);
		
		assertIterableEquals(new LinkedHashSet<Type>(), root.getTypeSet());
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
