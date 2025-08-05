package com.taf.logic.field;

import java.util.LinkedHashSet;
import java.util.Set;

import com.taf.logic.Entity;
import com.taf.logic.constraint.Constraint;
import com.taf.logic.type.DefaultFieldType;
import com.taf.logic.type.FieldType;
import com.taf.manager.ConstantManager;

public class Type extends Field {

	private static final String TYPE_STRING_FORMAT = """
			<type %s>
			%s
			\t</type>""";

	private Set<Field> fieldSet;
	private Set<Constraint> constraintSet;

	protected Type(String name, FieldType type) {
		super(name, type);
		fieldSet = new LinkedHashSet<Field>();
		constraintSet = new LinkedHashSet<Constraint>();
	}

	public Type(String name) {
		this(name, new DefaultFieldType());
	}

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

	public void removeEntity(Entity entity) {
		if (entity instanceof Field) {
			removeField((Field) entity);
		} else if (entity instanceof Constraint) {
			removeConstraint((Constraint) entity);
		}
	}

	private void addField(Field field) {
		field.setIndentationLevel(indentationLevel + 1);
		fieldSet.add(field);
		field.setParent(this);
	}

	private void removeField(Field field) {
		fieldSet.remove(field);
	}

	private void addConstraint(Constraint constraint) {
		constraintSet.add(constraint);
		constraint.setParent(this);
	}

	private void removeConstraint(Constraint constraint) {
		constraintSet.remove(constraint);
	}

	protected final String insideFieldsToString() {
		final String lineJump = ConstantManager.LINE_JUMP;
		final String indent = getIndentation() + ConstantManager.TAB;

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

	protected final String constraintsToString() {
		final String lineJump = ConstantManager.LINE_JUMP;
		final String indent = getIndentation() + ConstantManager.TAB;

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

	protected final String fieldToString() {
		return super.toString();
	}

	public Set<Field> getFieldSet() {
		return fieldSet;
	}

	public Set<Constraint> getConstraintSet() {
		return constraintSet;
	}

	@Override
	public String getEntityTypeName() {
		return ConstantManager.TYPE_ENTITY_NAME;
	}

	@Override
	public String toString() {
		String typeStr = "";
		if (!fieldSet.isEmpty()) {
			typeStr += insideFieldsToString() + ConstantManager.LINE_JUMP;
		}

		if (!constraintSet.isEmpty()) {
			typeStr += constraintsToString();
		}

		return TYPE_STRING_FORMAT.formatted(fieldToString(), typeStr);
	}

}
