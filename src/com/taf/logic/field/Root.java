package com.taf.logic.field;

import java.util.LinkedHashSet;
import java.util.Set;

import com.taf.logic.Entity;
import com.taf.logic.type.NodeType;
import com.taf.manager.ConstantManager;

public class Root extends Node {

	private static final String ROOT_STRING_FORMAT = """
			<root name=\"%s\">
			%s
			%s
			</root>""";

	private Set<Type> typeSet;

	public Root(String name) {
		super(name, new NodeType());
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

	private void addType(Type type) {
		typeSet.add(type);
	}
	
	public Set<Type> getTypeList() {
		return typeSet;
	}
	
	private String insideTypesToString() {
		final String lineJump = ConstantManager.LINE_JUMP;
		final String indent = getIndentation() + ConstantManager.TAB;

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
	public Node getParent() {
		// Root has no parent
		return null;
	}

	@Override
	public String toString() {
		
		return ROOT_STRING_FORMAT.formatted(super.getName(), insideTypesToString(), super.insideFieldsToString());
	}

}
