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
import com.taf.annotation.Nullable;
import com.taf.logic.Entity;
import com.taf.logic.constraint.Constraint;
import com.taf.logic.type.DefaultFieldType;
import com.taf.logic.type.FieldType;
import com.taf.util.Consts;

/**
 * <p>
 * A Type is a TAF entity which represents a field that can contain
 * {@link Parameter}s, {@link Node}s (only {@link Root} can contain "pure" Type
 * objects) and {@link Constraint}s.
 * </p>
 * 
 * @see Field
 * @see Node
 * 
 * @author Adrien Jakubiak
 */
public class Type extends Field {

	/** Used to format a type to a XML string representation. */
	private static final String TYPE_STRING_FORMAT = """
			<type %s>
			%s
			\t</type>""";

	/** The set of contained fields. */
	private Set<Field> fieldSet;

	/** The constraint set. */
	private Set<Constraint> constraintSet;

	/**
	 * Instantiates a new type with a name and a field type. Used to instantiate a
	 * {@link Node} since it has its own type.
	 *
	 * @param name the type name
	 * @param type the type's field type
	 */
	protected Type(@NotEmpty String name, FieldType type) {
		super(name, type);
		fieldSet = new LinkedHashSet<Field>();
		constraintSet = new LinkedHashSet<Constraint>();
	}

	/**
	 * Instantiates a new type with a name and a default field type.
	 *
	 * @param name the type name
	 */
	public Type(@NotEmpty String name) {
		this(name, new DefaultFieldType());
	}

	/**
	 * Adds an entity to the type.
	 *
	 * @param entity the entity to add
	 */
	public void addEntity(Entity entity) {
		if (entity instanceof Field) {
			// Add field if not a pure type
			if (entity instanceof Node || entity instanceof Parameter) {
				if (!(entity instanceof Root)) {
					addField((Field) entity);
				}
			}
		} else if (entity instanceof Constraint) {
			addConstraint((Constraint) entity);
		}
	}

	@Override
	public void setType(@Nullable FieldType type) {
		// Cannot change the type of a type!
	}

	/**
	 * Removes the entity of the type.
	 *
	 * @param entity the entity to remove
	 */
	public void removeEntity(Entity entity) {
		if (entity instanceof Field) {
			removeField((Field) entity);
		} else if (entity instanceof Constraint) {
			removeConstraint((Constraint) entity);
		}
	}

	/**
	 * Adds a field to the type. Increases its indentation level for display
	 * purpose.
	 *
	 * @param field the field to add
	 */
	private void addField(Field field) {
		field.setIndentationLevel(indentationLevel + 1);
		fieldSet.add(field);
		field.setParent(this);
	}

	/**
	 * Removes the field from the type.
	 *
	 * @param field the field to remove
	 */
	private void removeField(Field field) {
		fieldSet.remove(field);
	}

	/**
	 * Adds the constraint to the type.
	 *
	 * @param constraint the constraint to add
	 */
	private void addConstraint(Constraint constraint) {
		constraintSet.add(constraint);
		constraint.setParent(this);
	}

	/**
	 * Removes the constraint from the type.
	 *
	 * @param constraint the constraint to remove
	 */
	private void removeConstraint(Constraint constraint) {
		constraintSet.remove(constraint);
	}

	/**
	 * Returns a nice string representation of the fields inside the type with the
	 * correct indentation.
	 *
	 * @return the fields string representation
	 */
	protected final String insideFieldsToString() {
		final String lineJump = Consts.LINE_JUMP;
		final String indent = getIndentation() + Consts.TAB;

		String strFields = "";
		int i = 0;
		for (Field field : fieldSet) {
			strFields += indent + field.toString();

			if (i++ != fieldSet.size() - 1) {
				strFields += lineJump;
			}
		}

		return strFields;
	}

	/**
	 * Returns a nice string representation of the constraints inside the type with the
	 * correct indentation.
	 *
	 * @return the constraints string representation
	 */
	protected final String constraintsToString() {
		final String lineJump = Consts.LINE_JUMP;
		final String indent = getIndentation() + Consts.TAB;

		String strConstraints = "";
		int i = 0;
		for (Constraint constraint : constraintSet) {
			strConstraints += indent + constraint.toString();

			if (i++ != constraintSet.size() - 1) {
				strConstraints += lineJump;
			}
		}
		return strConstraints;
	}

	/**
	 * Returns the field set.
	 *
	 * @return the field set
	 */
	public Set<Field> getFieldSet() {
		return fieldSet;
	}

	/**
	 * Returns the constraint set.
	 *
	 * @return the constraint set
	 */
	public Set<Constraint> getConstraintSet() {
		return constraintSet;
	}

	@Override
	public String getEntityTypeName() {
		return Consts.TYPE_ENTITY_NAME;
	}

	@Override
	public String toString() {
		String typeStr = "";
		if (!fieldSet.isEmpty()) {
			typeStr += insideFieldsToString() + Consts.LINE_JUMP;
		}

		if (!constraintSet.isEmpty()) {
			typeStr += constraintsToString();
		}

		return TYPE_STRING_FORMAT.formatted(super.toString(), typeStr);
	}

}
