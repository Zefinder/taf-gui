package com.taf.logic.field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.taf.logic.type.NodeType;
import com.taf.util.Consts;

class NodeTest extends TypeTest {

	private Node node;
	
	public NodeTest() {
		node = new Node(name);
		
		// Update hierarchy
		field = node;
		type = node;
	}
	
	@Override
	void testFieldDefaultValuesImpl() {
		assertEquals(name, type.getName());
		assertNull(type.getParent());
		assertInstanceOf(NodeType.class, type.getType());
		assertEquals(Consts.NODE_ENTITY_NAME, type.getEntityTypeName());
		assertEquals(0, type.getFieldSet().size());
		assertEquals(0, type.getConstraintSet().size());
		assertFalse(node.hasType());
		assertFalse(node.hasRef());
	}
	
	@Override
	void testFieldEditTypeImpl() {
		assertInstanceOf(NodeType.class, node.getType());
	}
	
	@Test
	void testNodeRecursive() {
		String typeName = "type";
		String refName = "ref";
				
		node.setType(typeName);
		assertTrue(node.hasType());
		assertFalse(node.hasRef());
		assertEquals(typeName, node.getEntityTypeName());
		
		node.removeType();
		assertFalse(node.hasType());
		assertFalse(node.hasRef());
		assertEquals(Consts.NODE_ENTITY_NAME, node.getEntityTypeName());
		
		node.setReference(refName);
		assertFalse(node.hasType());
		assertTrue(node.hasRef());
		assertEquals(refName, node.getEntityTypeName());
	}
	
}
