package com.taf.logic.field;

import com.taf.logic.Entity;
import com.taf.logic.type.Type;
import com.taf.manager.ConstantManager;

public abstract class Field implements Entity {

	private String name;
	private Type type;

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

	public void setType(Type type) {
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
	public String toString() {
		return ConstantManager.FIELD_STRING_FORMAT.formatted(name, type.toString());
	}

}
