package com.taf.logic.field;

import java.util.ArrayList;
import java.util.List;

import com.taf.logic.constraint.Constraint;
import com.taf.logic.type.Type;

public class Node extends Field {

	private static final String NODE_STRING_FORMAT = """
			<node %s>
			%s
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
		String indent = getIndentation() + "\t";

		String strFields = "";
		for (int i = 0; i < fieldList.size(); i++) {
			strFields += indent + fieldList.get(i).toString();

			if (i != fieldList.size() - 1) {
				strFields += "\n";
			}
		}

		return strFields;
	}

	private String constraintsToString() {
		String indent = getIndentation() + "\t";

		String strConstraints = "";
		for (int i = 0; i < constraintList.size(); i++) {
			strConstraints += indent + constraintList.get(i).toString();

			if (i != fieldList.size() - 1) {
				strConstraints += "\n";
			}
		}

		return strConstraints;

	}

	@Override
	public String toString() {
		return NODE_STRING_FORMAT.formatted(super.toString(), insideFieldsToString(), constraintsToString(),
				getIndentation());
	}

}
