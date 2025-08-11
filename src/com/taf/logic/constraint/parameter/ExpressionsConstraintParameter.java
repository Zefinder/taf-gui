package com.taf.logic.constraint.parameter;

import java.util.ArrayList;
import java.util.List;

import com.taf.exception.ParseException;
import com.taf.util.Consts;

public class ExpressionsConstraintParameter extends ConstraintParameter {

	static final String CONSTRAINT_PARAMETER_NAME = "expressions";
	
	private static final String NULL_ERROR_MESSAGE = "Expressions must not be null!";
	
	private List<String> expressions;
	
	public ExpressionsConstraintParameter() {
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
	public void stringToValue(String stringValue) throws ParseException {
		if (stringValue == null) {
			throw new ParseException(this.getClass(), NULL_ERROR_MESSAGE);
		} if (stringValue.isBlank()) {
			return;
		}
		
		final String separator = Consts.ELEMENT_SEPARATOR;
		String[] values = stringValue.split(separator);
		expressions.clear();
		
		for (String value : values) {
			if (!value.isBlank()) {
				addExpression(value);
			}
		}
	}

}
