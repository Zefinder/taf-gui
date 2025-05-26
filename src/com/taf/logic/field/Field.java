package com.taf.logic.field;

import com.taf.logic.type.Type;

public abstract class Field {

	private static final String FIELD_STRING_FORMAT = "name=\"%s\" %s";

	private String name;
	private final Type type;

	protected int indentationLevel;

	public Field(String name, Type type) {
		this.name = name;
		this.type = type;
		this.indentationLevel = 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	/**
	 * Sets the indentation level of the field, this is just for nice output. This
	 * is package level since it will be modified by either the root or a node.
	 * 
	 * @param indentationLevel the new indentation level
	 */
	void setIndentationLevel(int indentationLevel) {
		this.indentationLevel = indentationLevel;
	}

	protected String getIndentation() {
		String indent = "";
		for (int i = 0; i < indentationLevel; i++) {
			indent += "\t";
		}

		return indent;
	}

	@Override
	public String toString() {
		return FIELD_STRING_FORMAT.formatted(name, type.toString());
	}

}
