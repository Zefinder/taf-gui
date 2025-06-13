package com.taf.logic.field;

import java.util.LinkedHashSet;
import java.util.Set;

import com.taf.logic.Entity;
import com.taf.logic.constraint.Constraint;
import com.taf.logic.type.AnonymousType;
import com.taf.logic.type.Type;
import com.taf.manager.ConstantManager;

public class Node extends Field {

	private static final String NODE_STRING_FORMAT = """
			<node %s>
			%s
			%s</node>""";

	private Set<Field> fieldSet;
	private Set<Constraint> constraintSet;

	public Node(String name, Type type) {
		super(name, type);
		fieldSet = new LinkedHashSet<Field>();
		constraintSet = new LinkedHashSet<Constraint>();
	}

	public void addEntity(Entity entity) {
		if (entity instanceof Field) {
			addField((Field) entity);
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
	
	public void addField(Field field) {
		field.setIndentationLevel(indentationLevel + 1);
		fieldSet.add(field);
		field.setParent(this);
	}

	public void removeField(Field field) {
		fieldSet.remove(field);
	}

	public void addConstraint(Constraint constraint) {
		constraintSet.add(constraint);
		constraint.setParent(this);
	}
	
	public void removeConstraint(Constraint constraint) {
		constraintSet.remove(constraint);
	}

	protected String insideFieldsToString() {
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

	private String constraintsToString() {
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

	public Set<Field> getFieldList() {
		return fieldSet;
	}

	public Set<Constraint> getConstraintList() {
		return constraintSet;
	}

	@Override
	public String getEntityTypeName() {
		Type type = getType();
		if (type instanceof AnonymousType) {
			return ConstantManager.NODE_ENTITY_NAME;
		}

		return type.getName();
	}

	@Override
	public String toString() {
		String nodeStr = "";
		if (!fieldSet.isEmpty()) {
			nodeStr += insideFieldsToString() + ConstantManager.LINE_JUMP;
		}

		if (!constraintSet.isEmpty()) {
			nodeStr += constraintsToString();
		}

		return NODE_STRING_FORMAT.formatted(super.toString(), nodeStr, getIndentation());
	}

}
