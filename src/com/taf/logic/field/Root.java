/*
 * Copyright or © or Copr.
 * 
 * This software is a computer program whose purpose is to generate random test
 * case from a template file describing the data model.
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software. You can use, modify
 * and/or redistribute the software under the terms of the CeCILL-B license as
 * circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * 
 * As a counterpart to the access to the source code and rights to copy, modify
 * and redistribute granted by the license, users are provided only with a
 * limited warranty and the software's author, the holder of the economic
 * rights, and the successive licensors have only limited liability.
 * 
 * In this respect, the user's attention is drawn to the risks associated with
 * loading, using, modifying and/or developing or reproducing the software by
 * the user in light of its specific status of free software, that may mean that
 * it is complicated to manipulate, and that also therefore means that it is
 * reserved for developers and experienced professionals having in-depth
 * computer knowledge. Users are therefore encouraged to load and test the
 * software's suitability as regards their requirements in conditions enabling
 * the security of their systems and/or data to be ensured and, more generally,
 * to use and operate it in the same conditions as regards security.
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package com.taf.logic.field;

import java.util.LinkedHashSet;
import java.util.Set;

import com.taf.annotation.NotEmpty;
import com.taf.logic.Entity;
import com.taf.logic.constraint.Constraint;
import com.taf.util.Consts;

/**
 * <p>
 * A Root is a TAF entity which represents a field that is the root of the whole
 * TAF hierarchy. It can contain {@link Parameter}s, {@link Node}s,
 * {@link Constraint}s as well as {@link Type}s.
 * </p>
 * 
 * <p>
 * Because a Root is the starting point of any project, it has no parent, no
 * type, cannot be recursive and there can be only one in the project.
 * </p>
 * 
 * @see Type
 * @see Node
 *
 * @author Adrien Jakubiak
 */
public class Root extends Node {

	/** Used to format a root to a XML string representation. */
	private static final String ROOT_STRING_FORMAT = """
			<root name=\"%s\">
			%s
			%s
			%s
			</root>""";

	/** The pure type fields set. */
	private Set<Type> typeSet;

	/**
	 * Instantiates a new root.
	 *
	 * @param name the name
	 */
	public Root(@NotEmpty String name) {
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
	public Node getParent() {
		// A root has no parent
		return null;
	}

	/**
	 * Returns the root type set.
	 *
	 * @return the root type set
	 */
	public Set<Type> getTypeSet() {
		return typeSet;
	}

	@Override
	public boolean hasRef() {
		// A root cannot have a reference
		return false;
	}

	@Override
	public boolean hasType() {
		// A root cannot have a type
		return false;
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

	@Override
	public void setParent(Type parent) {
		// A root has no parent
	}

	@Override
	public void setReference(String referenceName) {
		// A root cannot have a reference
	}

	@Override
	public void setType(String typeName) {
		// A root cannot have a type
	}

	@Override
	public String toString() {
		return ROOT_STRING_FORMAT.formatted(super.getName(), insideTypesToString(), super.insideFieldsToString(),
				super.constraintsToString());
	}

	/**
	 * Adds the type to the root.
	 *
	 * @param type the type to add to the root
	 */
	private void addType(Type type) {
		type.setIndentationLevel(indentationLevel + 1);
		typeSet.add(type);
		type.setParent(this);
	}

	/**
	 * Returns a nice string representation of the types inside the root with the
	 * correct indentation.
	 *
	 * @return the types string representation
	 */
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

	/**
	 * Removes the type from the root.
	 *
	 * @param type the type to remove
	 */
	private void removeType(Type type) {
		typeSet.remove(type);
	}

}
