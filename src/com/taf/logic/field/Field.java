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
import com.taf.annotation.NotNull;
import com.taf.annotation.Nullable;
import com.taf.logic.Entity;
import com.taf.logic.type.FieldType;
import com.taf.util.Consts;

/**
 * <p>
 * The Field class is the base for {@link Type} and {@link Parameter}. A Field
 * has a name and a {@link FieldType}.
 * </p>
 * 
 * <p>
 * Field uniqueness is determined by its class, parent and name. If two fields
 * are two {@link Parameter}s sharing the same name and parent, they are the
 * same whether they have the same field type or not.
 * </p>
 * 
 * @see Entity
 * @see Type
 * @see Parameter
 * @author Adrien Jakubiak
 */
public abstract class Field implements Entity {

	/** The field name. */
	private String name;

	/** The field type (which is not {@link Type}!) */
	@NotNull
	private FieldType type;

	/** The field parent. */
	@Nullable
	private Type parent;

	/** The indentation level. */
	protected int indentationLevel;

	/**
	 * Instantiates a new field with a name and a field type.
	 *
	 * @param name the field name
	 * @param type the field type
	 */
	public Field(@NotEmpty String name, FieldType type) {
		this.name = name;
		this.type = type;
		this.indentationLevel = 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (!obj.getClass().equals(this.getClass())) {
			return false;
		}

		Field other = (Field) obj;
		boolean parentsEquals = parent == null ? other.parent == null : parent.equals(other.parent);

		// Class checked at first line
		return this.name.equals(other.name) && parentsEquals;
	}

	@Override
	@NotEmpty
	public String getName() {
		return name;
	}

	@Override
	@Nullable
	public Type getParent() {
		return this.parent;
	}

	/**
	 * Returns the field type of this field.
	 *
	 * @return the field type of this field
	 */
	@NotNull
	public FieldType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		String parentName = parent == null ? "" : parent.getName();
		return (this.getClass().toString() + Consts.HASH_SEPARATOR + getName() + parentName).hashCode();
	}

	@Override
	public void setName(@NotEmpty String name) {
		if (!name.isBlank()) {
			// Because hashcode is not updated in a set, you need to remove and add again to
			// the parent.
			Type parent = this.parent;
			if (parent != null) {
				parent.removeEntity(this);
			}
			
			this.name = name;
			
			if (parent != null) {
				parent.addEntity(this);
			}
		}
	}

	@Override
	public void setParent(Type parent) {
		this.parent = parent;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(@NotNull FieldType type) {
		if (type != null) {
			this.type = type;
		}
	}

	@Override
	public String toString() {
		return formatField();
	}

	/**
	 * Returns the indentation level of the field.
	 *
	 * @return the indentation level of the field
	 */
	protected String getIndentation() {
		final String tab = Consts.TAB;
		String indent = "";
		for (int i = 0; i < indentationLevel; i++) {
			indent += tab;
		}

		return indent;
	}
	
	protected String formatField() {
		return Consts.FIELD_STRING_FORMAT.formatted(name, type.toString()).strip();
	}

	/**
	 * <p>
	 * Sets the indentation level of the field.
	 * </p>
	 * 
	 * <p>
	 * Note this is just for nice output. This is package level since it will be
	 * modified by either the root or a node.
	 * </p>
	 * 
	 * @param indentationLevel the new indentation level
	 */
	void setIndentationLevel(int indentationLevel) {
		this.indentationLevel = indentationLevel;
	}

}
