package com.taf.logic.field;

import com.taf.logic.type.FieldType;
import com.taf.logic.type.NodeType;
import com.taf.manager.ConstantManager;

public class Node extends Type {

	private static final String NODE_STRING_FORMAT = """
			<node %s>
			%s
			%s</node>""";

	private NodeType type;

	protected Node(String name, NodeType type) {
		super(name, type);
		this.type = type;
	}

	public Node(String name) {
		this(name, new NodeType());
	}
	
	public void editMin(int minValue) {
		type.editMin(minValue);
	}

	public int getMin() {
		return type.getMin();
	}

	public void editMax(int maxValue) {
		type.editMax(maxValue);
	}

	public int getMax() {
		return type.getMax();
	}

	public void editInstanceNumber(int number) {
		type.editInstanceNumber(number);
	}

	public int getInstanceNumber() {
		return type.getInstanceNumber();
	}

	@Override
	public String getEntityTypeName() {
		FieldType type = getType();
		if (type.getName().isBlank()) {
			return ConstantManager.NODE_ENTITY_NAME;
		} 

		return type.getName();
	}

	@Override
	public String toString() {
		String nodeStr = "";
		if (!getFieldSet().isEmpty()) {
			nodeStr += insideFieldsToString() + ConstantManager.LINE_JUMP;
		}

		if (!getConstraintSet().isEmpty()) {
			nodeStr += constraintsToString();
		}

		return NODE_STRING_FORMAT.formatted(fieldToString(), nodeStr, getIndentation());
	}

}
