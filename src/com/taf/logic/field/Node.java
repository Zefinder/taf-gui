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

import com.taf.annotation.NotEmpty;
import com.taf.logic.constraint.Constraint;
import com.taf.logic.type.FieldType;
import com.taf.logic.type.NodeType;
import com.taf.manager.TypeManager;
import com.taf.util.Consts;

/**
 * <p>
 * A Node is a TAF entity which represents a field that can contain
 * {@link Parameter}s, {@link Node}s, and {@link Constraint}s exactly as
 * {@link Type}s.
 * </p>
 * 
 * <p>
 * A Node can be recursive, either by having a type, or by making a reference to
 * another Node. For more information, see {@link NodeType}.
 * </p>
 * 
 * <p>
 * Note that any Node can serve as a reference, which are stored inside the
 * {@link TypeManager}. If you want to remove a Node, do not forget to remove it
 * also from the {@link TypeManager}.
 * </p>
 * 
 * @see Type
 * @see Root
 * @see NodeType
 *
 * @author Adrien Jakubiak
 */
public class Node extends Type {

	/** Used to format a node to a XML string representation. */
	private static final String NODE_STRING_FORMAT = """
			<node %s>
			%s
			%s</node>""";

	/** The node type. */
	private NodeType type;

	/**
	 * New node with node type with default values.
	 * 
	 * @param name the node name
	 */
	public Node(@NotEmpty String name) {
		this(name, new NodeType());
	}

	/**
	 * Instantiates a new node with the specified node type.
	 *
	 * @param name the node name
	 * @param type the node type
	 */
	public Node(@NotEmpty String name, NodeType type) {
		super(name, type);
		this.type = type;
	}

	@Override
	public String getEntityTypeName() {
		FieldType type = getType();
		if (getTypeName().isBlank()) {
			return Consts.NODE_ENTITY_NAME;
		}

		return type.getName();
	}

	/**
	 * Returns the node type name.
	 *
	 * @return the node type name
	 */
	public String getTypeName() {
		return type.getName();
	}

	/**
	 * Checks for node reference.
	 *
	 * @return true, if the node has a type
	 */
	public boolean hasRef() {
		return type.hasRef();
	}

	/**
	 * Checks for node type.
	 *
	 * @return true, if the node has a type
	 */
	public boolean hasType() {
		return type.hasType();
	}

	/**
	 * Removes the node type.
	 */
	public void removeType() {
		type.removeType();
	}

	/**
	 * Sets the node reference.
	 *
	 * @param referenceName the new node reference
	 */
	public void setReference(String referenceName) {
		type.setReference(referenceName);
	}

	/**
	 * Sets the node type.
	 *
	 * @param typeName the new node type
	 */
	public void setType(String typeName) {
		type.setType(typeName);
	}

	@Override
	public String toString() {
		String nodeStr = "";
		if (!getFieldSet().isEmpty()) {
			nodeStr += insideFieldsToString() + Consts.LINE_JUMP;
		}

		if (!getConstraintSet().isEmpty()) {
			nodeStr += constraintsToString();
		}

		return NODE_STRING_FORMAT.formatted(formatField(), nodeStr, getIndentation());
	}

}
