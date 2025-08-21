package com.taf.logic.type;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import com.taf.logic.type.parameter.DepthNumberParameter;
import com.taf.logic.type.parameter.InstanceNumberParameter;
import com.taf.logic.type.parameter.MaxDepthParameter;
import com.taf.logic.type.parameter.MaxInstanceParameter;
import com.taf.logic.type.parameter.MinDepthParameter;
import com.taf.logic.type.parameter.MinInstanceParameter;
import com.taf.logic.type.parameter.ReferenceParameter;
import com.taf.logic.type.parameter.TypeNameParameter;
import com.taf.util.Consts;
import com.taf.util.HashSetBuilder;

class NodeTypeTest extends FieldTypeTest {

	private NodeType nodeType;

	public NodeTypeTest() {
		super(new NodeType(), "");
		nodeType = (NodeType) fieldType;
	}

	@Override
	void testFieldTypeDefaultValuesImpl() {
		assertEquals(Consts.DEFAULT_INSTANCE_NUMBER, nodeType.getInstanceNumber());
		assertEquals(Consts.DEFAULT_MAX_INSTANCE_NUMBER, nodeType.getMaxInstanceNumber());
		assertEquals(Consts.DEFAULT_MIN_INSTANCE_NUMBER, nodeType.getMinInstanceNumber());
		assertEquals(Consts.DEFAULT_DEPTH_NUMBER, nodeType.getDepthNumber());
		assertEquals(Consts.DEFAULT_MAX_DEPTH_NUMBER, nodeType.getMaxDepth());
		assertEquals(Consts.DEFAULT_MIN_DEPTH_NUMBER, nodeType.getMinDepth());
		assertFalse(nodeType.hasMinMaxInstance());
		assertFalse(nodeType.hasMinMaxDepth());
		assertFalse(nodeType.isRecursiveNode());
	}
	
	@Override
	void testFieldTypeMandatoryParametersImpl() {
		assertIterableEquals(new HashSet<String>(), nodeType.getMandatoryParametersName());
	}

	@Override
	void testFieldTypeOptionalParametersImpl() {
		HashSet<String> optionalTypeParameters = new HashSetBuilder<String>()
				.add(InstanceNumberParameter.PARAMETER_NAME).add(MinInstanceParameter.PARAMETER_NAME)
				.add(MaxInstanceParameter.PARAMETER_NAME).add(TypeNameParameter.PARAMETER_NAME)
				.add(ReferenceParameter.PARAMETER_NAME).add(DepthNumberParameter.PARAMETER_NAME)
				.add(MinDepthParameter.PARAMETER_NAME).add(MaxDepthParameter.PARAMETER_NAME).build();
		assertIterableEquals(optionalTypeParameters, nodeType.getOptionalParametersName());
	}

	@Test
	void testNodeTypeChangeInstanceNumber() {
		nodeType.editInstanceNumber(Consts.DEFAULT_INSTANCE_NUMBER + 1);
		assertEquals(Consts.DEFAULT_INSTANCE_NUMBER + 1, nodeType.getInstanceNumber());

		nodeType.editMinInstanceNumber(Consts.DEFAULT_MIN_INSTANCE_NUMBER + 1);
		assertEquals(Consts.DEFAULT_MIN_INSTANCE_NUMBER + 1, nodeType.getMinInstanceNumber());

		nodeType.editMaxInstanceNumber(Consts.DEFAULT_MAX_INSTANCE_NUMBER + 1);
		assertEquals(Consts.DEFAULT_MAX_INSTANCE_NUMBER + 1, nodeType.getMaxInstanceNumber());
	}

	@Test
	void testNodeTypeChangeDepthNumber() {
		nodeType.editDepthNumber(Consts.DEFAULT_DEPTH_NUMBER + 1);
		assertEquals(Consts.DEFAULT_DEPTH_NUMBER + 1, nodeType.getDepthNumber());

		nodeType.editMinDepth(Consts.DEFAULT_MIN_DEPTH_NUMBER + 1);
		assertEquals(Consts.DEFAULT_MIN_DEPTH_NUMBER + 1, nodeType.getMinDepth());

		nodeType.editMaxDepth(Consts.DEFAULT_MAX_DEPTH_NUMBER + 1);
		assertEquals(Consts.DEFAULT_MAX_DEPTH_NUMBER + 1, nodeType.getMaxDepth());
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
