package com.taf.logic;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.taf.logic.type.NodeType;
import com.taf.manager.ConstantManager;

class NodeTypeTest {
	
	@Test
	void testNodeTypeDefaultValues() {
		NodeType nodeType = new NodeType();
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
		NodeType nodeType = new NodeType();
		
		nodeType.editInstanceNumber(ConstantManager.DEFAULT_INSTANCE_NUMBER + 1);
		assertEquals(ConstantManager.DEFAULT_INSTANCE_NUMBER, nodeType.getInstanceNumber());
		
		nodeType.editMinInstanceNumber(ConstantManager.DEFAULT_MIN_INSTANCE_NUMBER + 1);
		assertEquals(ConstantManager.DEFAULT_MIN_INSTANCE_NUMBER, nodeType.getMinInstanceNumber());
		
		nodeType.editMaxInstanceNumber(ConstantManager.DEFAULT_MAX_INSTANCE_NUMBER + 1);
		assertEquals(ConstantManager.DEFAULT_MAX_INSTANCE_NUMBER, nodeType.getMaxInstanceNumber());
	}

}
