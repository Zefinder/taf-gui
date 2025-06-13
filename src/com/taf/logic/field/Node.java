package com.taf.logic.field;

import java.util.ArrayList;
import java.util.List;

import com.taf.logic.constraint.Constraint;
import com.taf.logic.type.AnonymousType;
import com.taf.logic.type.Type;
import com.taf.manager.ConstantManager;

public class Node extends Field {
	
	private static final String NODE_STRING_FORMAT = """
			<node %s>
			%s
			%s</node>""";

	private List<Field> fieldList;
	private List<Constraint> constraintList;

	public Node(String name, Type type) {
		super(name, type);
		fieldList = new ArrayList<Field>();
		constraintList = new ArrayList<Constraint>();
	}

	public void addField(Field field) {
		field.setIndentationLevel(indentationLevel + 1);
		fieldList.add(field);
	}

	public void addConstraint(Constraint constraint) {
		constraintList.add(constraint);
	}

	protected String insideFieldsToString() {
		final String lineJump = ConstantManager.LINE_JUMP;
		String indent = getIndentation() + ConstantManager.TAB;

		String strFields = "";
		for (int i = 0; i < fieldList.size(); i++) {
			strFields += indent + fieldList.get(i).toString();

			if (i != fieldList.size() - 1) {
				strFields += lineJump;
			}
		}

		return strFields;
	}

	private String constraintsToString() {
		final String lineJump = ConstantManager.LINE_JUMP;
		String indent = getIndentation() + ConstantManager.TAB;

		String strConstraints = "";
		for (int i = 0; i < constraintList.size(); i++) {
			strConstraints += indent + constraintList.get(i).toString();

			if (i != constraintList.size() - 1) {
				strConstraints += lineJump;
			}
		}

		return strConstraints;
	}
	
	// TODO change lists to arrays
	public List<Field> getFieldList() {
		return fieldList;
	}
	
	public List<Constraint> getConstraintList() {
		return constraintList;
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
		if (!fieldList.isEmpty()) {
			nodeStr += insideFieldsToString() + ConstantManager.LINE_JUMP;
		}
		
		if (!constraintList.isEmpty()) {
			nodeStr += constraintsToString();
		}

		return NODE_STRING_FORMAT.formatted(super.toString(), nodeStr, getIndentation());
	}

}
