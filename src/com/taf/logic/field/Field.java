package com.taf.logic.field;

import com.taf.logic.Entity;
import com.taf.logic.type.FieldType;
import com.taf.manager.ConstantManager;

public abstract class Field implements Entity {

	private String name;
	private FieldType type;
	private Type parent;

	protected int indentationLevel;

	public Field(String name, FieldType type) {
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

	public FieldType getType() {
		return type;
	}

	public void setType(FieldType type) {
		this.type = type;
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
		final String tab = ConstantManager.TAB;
		String indent = "";
		for (int i = 0; i < indentationLevel; i++) {
			indent += tab;
		}

		return indent;
	}

	@Override
	public Type getParent() {
		return this.parent;
	}

	@Override
	public void setParent(Type parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return ConstantManager.FIELD_STRING_FORMAT.formatted(name, type.toString());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Field)) {
			return false;
		}

		Field other = (Field) obj;
		return this.name.equals(other.name) && this.type.equals(other.type);
	}

}
