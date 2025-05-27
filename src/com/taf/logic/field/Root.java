package com.taf.logic.field;

import com.taf.logic.type.AnonymousType;

public class Root extends Node {

	private static final String ROOT_STRING_FORMAT = """
			<root name=\"%s\">
			%s
			</root>""";

	public Root(String name) {
		super(name, new AnonymousType());
	}
	
	@Override
	public String toString() {
		return ROOT_STRING_FORMAT.formatted(super.getName(), super.insideFieldsToString());
	}

}
