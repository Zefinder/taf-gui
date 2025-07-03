package com.taf.logic.field;

import com.taf.logic.type.NodeType;

public class Root extends Node {

	private static final String ROOT_STRING_FORMAT = """
			<root name=\"%s\">
			%s
			</root>""";

	public Root(String name) {
		super(name, new NodeType());
	}

	@Override
	public Node getParent() {
		// Root has no parent
		return null;
	}

	@Override
	public String toString() {
		return ROOT_STRING_FORMAT.formatted(super.getName(), super.insideFieldsToString());
	}

}
