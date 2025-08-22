package com.taf.logic.field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.HashSet;
import java.util.LinkedHashSet;

import org.junit.jupiter.api.Test;

import com.taf.exception.EntityCreationException;
import com.taf.logic.constraint.Constraint;
import com.taf.logic.constraint.parameter.QuantifierType;
import com.taf.logic.type.BooleanType;
import com.taf.logic.type.IntegerType;
import com.taf.logic.type.NodeType;
import com.taf.logic.type.RealType;
import com.taf.logic.type.StringType;
import com.taf.logic.type.parameter.DistributionType;
import com.taf.util.Consts;

public class RootTest extends TypeTest {

	public static final Root COMPLETE_ROOT = buildCompleteRoot();
	public static final String COMPLETE_ROOT_XML = """
			<root name="test">
			\t<type name="empty_type">

			\t</type>
			\t<type name="filled_type">
			\t\t<node name="empty_type_node" nb_instances="1">

			\t\t</node>
			\t\t<constraint name="empty_type_constraint"/>
			\t</type>
			\t<node name="filled_node" min="1" max="1">
			\t\t<parameter name="boolean_parameter" type="boolean" values="False;True" weights="1;1"/>
			\t\t<parameter name="integer_parameter" type="integer" min="0" max="10" distribution="u"/>
			\t\t<parameter name="integer_normal_parameter" type="integer" min="0" max="10" distribution="n" mean="0" variance="0"/>
			\t\t<parameter name="integer_interval_parameter" type="integer" min="0" max="10" distribution="i" ranges="[0, 10]" weights="1"/>
			\t\t<parameter name="real_parameter" type="real" min="0" max="10" distribution="u"/>
			\t\t<parameter name="real_normal_parameter" type="real" min="0" max="10" distribution="n" mean="0" variance="0"/>
			\t\t<parameter name="real_interval_parameter" type="real" min="0" max="10" distribution="i" ranges="[0, 10]" weights="1"/>
			\t\t<parameter name="string_parameter" type="string" values="a;b" weights="1;2"/>
			\t\t<constraint name="filled_constraint" expressions="i + j INFEQ 10" quantifiers="i;j" ranges="[0, 10];[0, filled_node.nb_instances]" types="forall;exists"/>
			\t</node>
			\t<node name="typed_node" type="empty_type" nb_instances="1" depth="1">
			\t\t<node name="ref_node" ref="filled_node" nb_instances="1" min_depth="1" max_depth="1">

			\t\t</node>

			\t</node>

			</root>""";

	private Root root;

	public RootTest() throws EntityCreationException {
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
		assertEquals(Consts.NODE_ENTITY_NAME, type.getEntityTypeName());
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
	void testFieldSetParent() throws EntityCreationException {
		Root root2 = new Root("a");

		root2.addEntity(root);
		assertNull(root.getParent());
		assertEquals(0, root.indentationLevel);
	}

	@Override
	@Test
	void testTypeAddType() throws EntityCreationException {
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
	void testRootRemoveType() throws EntityCreationException {
		System.out.println();
		Type type = new Type("type");
		root.addEntity(type);
		root.removeEntity(type);

		assertIterableEquals(new LinkedHashSet<Type>(), root.getTypeSet());
	}

	@Test
	void testTypeAddModifyRemoveType() throws EntityCreationException {
		Type type = new Type("type");
		type.addEntity(type);
		type.setName("aaaa");
		type.removeEntity(type);

		assertIterableEquals(new LinkedHashSet<Type>(), root.getTypeSet());
	}

	@Test
	void testRootToString() {
		String message = "";
		String[] expectedLines = COMPLETE_ROOT_XML.split("\n");
		String[] gotLines = COMPLETE_ROOT.toString().split("\n");

		if (expectedLines.length != gotLines.length) {
			message = "There are not the same number of lines... (expected %d but got %d)".formatted(gotLines.length,
					expectedLines.length);
		}

		for (int i = 0; i < expectedLines.length; i++) {
			if (!expectedLines[i].equals(gotLines[i])) {
				message = "Difference at line %d:\nExpected:\n%s\nBut got:\n%s\n\n".formatted(i + 1, expectedLines[i],
						gotLines[i]);
			}
		}

		assertEquals(COMPLETE_ROOT_XML, COMPLETE_ROOT.toString(), message);
	}

	private static final Root buildCompleteRoot() {
		try {
			Root root = new Root("test");
			Type emptyType = new Type("empty_type");
			Type filledType = new Type("filled_type");
			Node emptyTypeNode = new Node("empty_type_node");
			Constraint emptyTypeConstraint = new Constraint("empty_type_constraint");

			NodeType filledNodeType = new NodeType();
			filledNodeType.setMinMaxInstance(true);
			Node filledNode = new Node("filled_node", filledNodeType);
			Parameter booleanParameter = new Parameter("boolean_parameter", new BooleanType());
			Parameter integerParameter = new Parameter("integer_parameter", new IntegerType());
			Parameter integerNormalParameter = new Parameter("integer_normal_parameter", new IntegerType());
			((IntegerType) integerNormalParameter.getType()).setDistribution(DistributionType.NORMAL);
			IntegerType integerRangesType = new IntegerType();
			integerRangesType.setDistribution(DistributionType.INTERVAL);
			integerRangesType.addInterval(0, 10, 1);
			Parameter integerIntervalParameter = new Parameter("integer_interval_parameter", integerRangesType);
			Parameter realParameter = new Parameter("real_parameter", new RealType());
			Parameter realNormalParameter = new Parameter("real_normal_parameter", new RealType());
			((RealType) realNormalParameter.getType()).setDistribution(DistributionType.NORMAL);
			RealType realRangesType = new RealType();
			realRangesType.setDistribution(DistributionType.INTERVAL);
			realRangesType.addInterval(0, 10, 1);
			Parameter realIntervalParameter = new Parameter("real_interval_parameter", realRangesType);
			StringType stringType = new StringType();
			stringType.addValue("a", 1);
			stringType.addValue("b", 2);
			Parameter stringParameter = new Parameter("string_parameter", stringType);

			Constraint filledConstraint = new Constraint("filled_constraint");
			filledConstraint.addExpression("i + j INFEQ 10");
			filledConstraint.addQuantifier("i", "0", "10", QuantifierType.FORALL);
			filledConstraint.addQuantifier("j", "0", "filled_node.nb_instances", QuantifierType.EXISTS);

			Node typedNode = new Node("typed_node");
			typedNode.setType("empty_type");
			NodeType refNodeType = new NodeType();
			refNodeType.setMinMaxDepth(true);
			Node refNode = new Node("ref_node", refNodeType);
			refNode.setReference("filled_node");

			root.addEntity(emptyType);
			root.addEntity(filledType);
			root.addEntity(filledNode);
			root.addEntity(typedNode);

			filledType.addEntity(emptyTypeNode);
			filledType.addEntity(emptyTypeConstraint);

			filledNode.addEntity(booleanParameter);
			filledNode.addEntity(integerParameter);
			filledNode.addEntity(integerNormalParameter);
			filledNode.addEntity(integerIntervalParameter);
			filledNode.addEntity(realParameter);
			filledNode.addEntity(realNormalParameter);
			filledNode.addEntity(realIntervalParameter);
			filledNode.addEntity(stringParameter);
			filledNode.addEntity(filledConstraint);

			typedNode.addEntity(refNode);

			return root;
		} catch (EntityCreationException e) {
			// Never happens
		}
		return null;
	}
}
