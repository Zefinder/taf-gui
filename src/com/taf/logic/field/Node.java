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

	public Node(String name, NodeType type) {
		super(name, type);
		this.type = type;
	}

	/**
	 * New node with node type with default values.
	 * 
	 * @param name the node name
	 */
	public Node(String name) {
		this(name, new NodeType());
	}

	public void setType(String typeName) {
		type.setType(typeName);
	}

	public void setReference(String referenceName) {
		type.setReference(referenceName);
	}

	public void removeType() {
		type.removeType();
	}

	public String getTypeName() {
		return type.getName();
	}

	public boolean hasType() {
		return type.hasType();
	}

	public boolean hasRef() {
		return type.hasRef();
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
