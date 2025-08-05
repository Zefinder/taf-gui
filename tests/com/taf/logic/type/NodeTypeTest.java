package com.taf.logic.type;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.taf.manager.ConstantManager;

class NodeTypeTest extends TypeTest {

	private NodeType nodeType;
	
	public NodeTypeTest() {
		nodeType = new NodeType();
	}
	
	@Override
	void testTypeDefaultValuesImpl() {
		assertEquals(ConstantManager.DEFAULT_INSTANCE_NUMBER, nodeType.getInstanceNumber());
		assertEquals(ConstantManager.DEFAULT_MAX_INSTANCE_NUMBER, nodeType.getMaxInstanceNumber());
		assertEquals(ConstantManager.DEFAULT_MIN_INSTANCE_NUMBER, nodeType.getMinInstanceNumber());
		assertEquals(ConstantManager.DEFAULT_DEPTH_NUMBER, nodeType.getDepthNumber());
		assertEquals(ConstantManager.DEFAULT_MAX_DEPTH_NUMBER, nodeType.getMaxDepth());
		assertEquals(ConstantManager.DEFAULT_MIN_DEPTH_NUMBER, nodeType.getMinDepth());
		assertFalse(nodeType.hasMinMaxInstance());
		assertFalse(nodeType.hasMinMaxDepth());
		assertFalse(nodeType.isRecursiveNode());
	}

	@Test
	void testNodeTypeChangeInstanceNumber() {
		nodeType.editInstanceNumber(ConstantManager.DEFAULT_INSTANCE_NUMBER + 1);
		assertEquals(ConstantManager.DEFAULT_INSTANCE_NUMBER + 1, nodeType.getInstanceNumber());

		nodeType.editMinInstanceNumber(ConstantManager.DEFAULT_MIN_INSTANCE_NUMBER + 1);
		assertEquals(ConstantManager.DEFAULT_MIN_INSTANCE_NUMBER + 1, nodeType.getMinInstanceNumber());

		nodeType.editMaxInstanceNumber(ConstantManager.DEFAULT_MAX_INSTANCE_NUMBER + 1);
		assertEquals(ConstantManager.DEFAULT_MAX_INSTANCE_NUMBER + 1, nodeType.getMaxInstanceNumber());
	}

	@Test
	void testNodeTypeChangeDepthNumber() {
		nodeType.editDepthNumber(ConstantManager.DEFAULT_DEPTH_NUMBER + 1);
		assertEquals(ConstantManager.DEFAULT_DEPTH_NUMBER + 1, nodeType.getDepthNumber());

		nodeType.editMinDepth(ConstantManager.DEFAULT_MIN_DEPTH_NUMBER + 1);
		assertEquals(ConstantManager.DEFAULT_MIN_DEPTH_NUMBER + 1, nodeType.getMinDepth());

		nodeType.editMaxDepth(ConstantManager.DEFAULT_MAX_DEPTH_NUMBER + 1);
		assertEquals(ConstantManager.DEFAULT_MAX_DEPTH_NUMBER + 1, nodeType.getMaxDepth());
	}

	@Test
	void testNodeTypeRecursion() {
		String typeName = "type";
		String refName = "ref";

		// No type to type
		nodeType.setType(typeName);
		assertTrue(nodeType.hasType());
		assertFalse(nodeType.hasRef());
		assertTrue(nodeType.isRecursiveNode());
		assertEquals(typeName, nodeType.getName());

		// Type to no type
		nodeType.removeType();
		assertFalse(nodeType.hasType());
		assertFalse(nodeType.hasRef());
		assertFalse(nodeType.isRecursiveNode());
		assertTrue(nodeType.getName().isEmpty());

		// No type to ref
		nodeType.setReference(refName);
		assertFalse(nodeType.hasType());
		assertTrue(nodeType.hasRef());
		assertTrue(nodeType.isRecursiveNode());
		assertEquals(refName, nodeType.getName());

		// Ref to type
		nodeType.setType(typeName);
		assertTrue(nodeType.hasType());
		assertFalse(nodeType.hasRef());
		assertTrue(nodeType.isRecursiveNode());
		assertEquals(typeName, nodeType.getName());

		// Type to ref
		nodeType.setReference(refName);
		assertFalse(nodeType.hasType());
		assertTrue(nodeType.hasRef());
		assertTrue(nodeType.isRecursiveNode());
		assertEquals(refName, nodeType.getName());

		// Ref to no type
		nodeType.removeType();
		assertFalse(nodeType.hasType());
		assertFalse(nodeType.hasRef());
		assertFalse(nodeType.isRecursiveNode());
		assertTrue(nodeType.getName().isEmpty());
	}

}
