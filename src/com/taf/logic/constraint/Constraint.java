package com.taf.logic.constraint;

import java.util.List;

import com.taf.logic.Entity;
import com.taf.logic.constraint.parameter.ConstraintParameter;
import com.taf.logic.constraint.parameter.ExpressionConstraintParameter;
import com.taf.logic.constraint.parameter.QuantifierType;
import com.taf.logic.constraint.parameter.QuantifiersConstraintParameter;
import com.taf.logic.constraint.parameter.RangesConstraintParameter;
import com.taf.logic.constraint.parameter.RangesConstraintParameter.Range;
import com.taf.logic.constraint.parameter.TypesConstraintParameter;
import com.taf.logic.field.Type;
import com.taf.util.Consts;

public class Constraint implements Entity {

	private static final String CONSTRAINT_STRING_FORMAT = """
			<constraint %s/>""";

	private String name;
	private Type parent;

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

	public void addConstraintParameter(ConstraintParameter parameter) {
		// We assume that the parameters arrive in that order
		if (parameter instanceof ExpressionConstraintParameter) {
			for (String expression : ((ExpressionConstraintParameter) parameter).getExpressions()) {
				expressionsConstraintParameter.addExpression(expression);
			}
			
		} else if (parameter instanceof QuantifiersConstraintParameter) {
			for (String expression : ((QuantifiersConstraintParameter) parameter).getQuantifiers()) {
				quantifiersConstraintParameter.addQuantifier(expression);
			}

		} else if (parameter instanceof RangesConstraintParameter) {
			List<Range> ranges = ((RangesConstraintParameter) parameter).getRanges();
			int quantifierNumber = quantifiersConstraintParameter.getQuantifiers().size();
			int rangesNumber = ranges.size();
			for (int i = 0; i < Math.min(quantifierNumber, rangesNumber); i++) {
				Range range = ranges.get(i);
				rangesConstraintParameter.addRange(range.getLeft(), range.getRight());
			}
			
		} else if (parameter instanceof TypesConstraintParameter) {
			List<QuantifierType> types = ((TypesConstraintParameter) parameter).getTypes();
			int quantifierNumber = quantifiersConstraintParameter.getQuantifiers().size();
			int rangesNumber = types.size();
			for (int i = 0; i < Math.min(quantifierNumber, rangesNumber); i++) {
				QuantifierType type = types.get(i);
				typesConstraintParameter.addType(type);
			}
		}
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

	public List<String> getExpressions() {
		return expressionsConstraintParameter.getExpressions();
	}

	public List<String> getQuantifiers() {
		return quantifiersConstraintParameter.getQuantifiers();
	}

	public List<RangesConstraintParameter.Range> getRanges() {
		return rangesConstraintParameter.getRanges();
	}

	public List<QuantifierType> getTypes() {
		return typesConstraintParameter.getTypes();
	}

	public String parametersToString() {
		final String separator = Consts.PARAMETER_SEPARATOR;
		String paramStr = "";
		if (!expressionsConstraintParameter.getExpressions().isEmpty()) {
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

		return paramStr;
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
	public Type getParent() {
		return parent;
	}
	
	@Override
	public void setParent(Type parent) {
		this.parent = parent;
	}
	
	@Override
	public String getEntityTypeName() {
		return Consts.CONSTRAINT_ENTITY_NAME;
	}

	@Override
	public String toString() {
		String paramStr = parametersToString();
		return CONSTRAINT_STRING_FORMAT
				.formatted(Consts.FIELD_STRING_FORMAT.formatted(name, paramStr.strip()).stripTrailing());
	}

}
