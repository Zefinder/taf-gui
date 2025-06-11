package com.taf.logic.constraint;

import java.util.List;

import com.taf.logic.Entity;
import com.taf.logic.constraint.parameter.ExpressionConstraintParameter;
import com.taf.logic.constraint.parameter.QuantifierType;
import com.taf.logic.constraint.parameter.QuantifiersConstraintParameter;
import com.taf.logic.constraint.parameter.RangesConstraintParameter;
import com.taf.logic.constraint.parameter.TypesConstraintParameter;
import com.taf.manager.ConstantManager;

public class Constraint implements Entity {

	private static final String CONSTRAINT_STRING_FORMAT = """
			<constraint %s/>""";

	private String name;

	private ExpressionConstraintParameter expressionsConstraintParameter;
	private QuantifiersConstraintParameter quantifiersConstraintParameter;
	private RangesConstraintParameter rangesConstraintParameter;
	private TypesConstraintParameter typesConstraintParameter;

	public Constraint(String name) {
		this.name = name;
		expressionsConstraintParameter = new ExpressionConstraintParameter();
		quantifiersConstraintParameter = new QuantifiersConstraintParameter();
		rangesConstraintParameter = new RangesConstraintParameter();
		typesConstraintParameter = new TypesConstraintParameter();
	}

	public void addExpression(String expression) {
		expressionsConstraintParameter.addExpression(expression);
	}

	public void removeExpression(int index) {
		expressionsConstraintParameter.removeExpression(index);
	}

	public void editExpression(int index, String expression) {
		expressionsConstraintParameter.editExpression(index, expression);
	}

	public List<String> getExpressions() {
		return expressionsConstraintParameter.getExpressionList();
	}

	public void addQuantifier(String quantifier, String leftRange, String rightRange, QuantifierType type) {
		quantifiersConstraintParameter.addQuantifier(quantifier);
		rangesConstraintParameter.addRange(leftRange, rightRange);
		typesConstraintParameter.addType(type);
	}

	public void removeQuantifier(int index) {
		quantifiersConstraintParameter.removeQuantifier(index);
		rangesConstraintParameter.removeRange(index);
		typesConstraintParameter.removeType(index);
	}

	public void editQuantifier(int index, String quantifier) {
		quantifiersConstraintParameter.editQuantifier(index, quantifier);
	}

	public void editLeftRange(int index, String leftRange) {
		rangesConstraintParameter.editLeftRange(index, leftRange);
	}

	public void editRightRange(int index, String rightRange) {
		rangesConstraintParameter.editRightRange(index, rightRange);
	}

	public void editQuantifierType(int index, QuantifierType type) {
		typesConstraintParameter.editType(index, type);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getEntityTypeName() {
		return ConstantManager.CONSTRAINT_ENTITY_NAME;
	}

	@Override
	public String toString() {
		final String separator = ConstantManager.PARAMETER_SEPARATOR;
		String paramStr = "";
		if (!expressionsConstraintParameter.getExpressionList().isEmpty()) {
			paramStr += separator + expressionsConstraintParameter.toString();
		}

		if (!quantifiersConstraintParameter.getQuantifiers().isEmpty()) {
			paramStr += separator + quantifiersConstraintParameter.toString();
		}

		if (!rangesConstraintParameter.getRanges().isEmpty()) {
			paramStr += separator + rangesConstraintParameter.toString();
		}

		if (!typesConstraintParameter.getTypes().isEmpty()) {
			paramStr += separator + typesConstraintParameter.toString();
		}

		return CONSTRAINT_STRING_FORMAT
				.formatted(ConstantManager.FIELD_STRING_FORMAT.formatted(name, paramStr.strip()).stripTrailing());
	}

}
