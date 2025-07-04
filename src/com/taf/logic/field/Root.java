package com.taf.logic.field;

import java.util.LinkedHashSet;
import java.util.Set;

import com.taf.logic.Entity;
import com.taf.logic.type.NodeType;

public class Root extends Node {

	private static final String ROOT_STRING_FORMAT = """
			<root name=\"%s\">
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
