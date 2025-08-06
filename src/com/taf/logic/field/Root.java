package com.taf.logic.field;

import java.util.LinkedHashSet;
import java.util.Set;

import com.taf.logic.Entity;
import com.taf.util.Consts;

public class Root extends Node {

	private static final String ROOT_STRING_FORMAT = """
			<root name=\"%s\">
			%s
			%s
			%s
			</root>""";

	private Set<Type> typeSet;

	public Root(String name) {
		super(name);
		typeSet = new LinkedHashSet<Type>();
	}

	@Override
	public void addEntity(Entity entity) {
		// Root can add all types of fields
		if (entity instanceof Type && !(entity instanceof Node)) {
			addType((Type) entity);
		} else {
			super.addEntity(entity);	
		}		
	}

	@Override
	public void removeEntity(Entity entity) {
		// Remove pure type items
		if (entity instanceof Type && !(entity instanceof Node)) {
			removeType((Type) entity);
		} else {			
			super.removeEntity(entity);
		}
	}
	
	private void addType(Type type) {
		type.setIndentationLevel(indentationLevel + 1);
		typeSet.add(type);
		type.setParent(this);
	}
	
	private void removeType(Type type) {
		typeSet.remove(type);
	}
	
	public Set<Type> getTypeSet() {
		return typeSet;
	}
	
	@Override
	public void setType(String typeName) {
		// A root cannot have a type
	}
	
	@Override
	public void setReference(String referenceName) {
		// A root cannot have a reference
	}
	
	@Override
	public void setParent(Type parent) {
		// A root has no parent
	}
	
	@Override
	public boolean hasType() {
		// A root cannot have a type
		return false;
	}
	
	@Override
	public boolean hasRef() {
		// A root cannot have a reference
		return false;
	}
	
	@Override
	public Node getParent() {
		// A root has no parent
		return null;
	}
	
	private String insideTypesToString() {
		final String lineJump = Consts.LINE_JUMP;
		final String indent = getIndentation() + Consts.TAB;

		String strFields = "";
		int i = 0;
		for (Type type : typeSet) {
			strFields += indent + type.toString();

			if (i++ != typeSet.size() - 1) {
				strFields += lineJump;
			}
		}

		return strFields;
	}

	@Override
	public String toString() {
		return ROOT_STRING_FORMAT.formatted(super.getName(), insideTypesToString(), super.insideFieldsToString(), super.constraintsToString());
	}

}
