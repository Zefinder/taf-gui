package com.taf.logic.constraint.parameter;

import java.util.ArrayList;
import java.util.List;

import com.taf.util.Consts;

public class ExpressionConstraintParameter extends ConstraintParameter {

	static final String CONSTRAINT_PARAMETER_NAME = "expressions";
	
	private List<String> expressions;
	
	public ExpressionConstraintParameter() {
		super(CONSTRAINT_PARAMETER_NAME);
		expressions = new ArrayList<String>();
	}
	
	public void addExpression(String expression) {
		expressions.add(expression.strip());
	}

	public void removeExpression(int index) {
		expressions.remove(index);
	}
	
	public void editExpression(int index, String expression) {
		expressions.set(index, expression.strip());
	}
	
	public List<String> getExpressions() {
		return expressions;
	}
	
	@Override
	public String valueToString() {
		if (expressions.isEmpty()) {
			return "";
		}
		
		final String separator = Consts.ELEMENT_SEPARATOR;
		String res = expressions.get(0);
		for (int i = 1; i < expressions.size(); i++) {
			res += separator + expressions.get(i);
		}
		
		return res;
	}
	
	@Override
	void stringToValue(String stringValue) {
		final String separator = Consts.ELEMENT_SEPARATOR;
		String[] values = stringValue.split(separator);

		for (String value : values) {
			if (!value.isBlank()) {
				addExpression(value);
			}
		}
	}

}
