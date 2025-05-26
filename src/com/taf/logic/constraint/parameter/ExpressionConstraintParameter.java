package com.taf.logic.constraint.parameter;

import java.util.ArrayList;
import java.util.List;

public class ExpressionConstraintParameter extends ConstraintParameter {

	private static final String CONSTRAINT_PARAMETER_NAME = "expressions";
	
	private List<String> expressionList;
	
	public ExpressionConstraintParameter() {
		super(CONSTRAINT_PARAMETER_NAME);
		expressionList = new ArrayList<String>();
	}
	
	public void addExpression(String expression) {
		expressionList.add(expression);
	}

	@Override
	public String valueToString() {
		String expressionStr = "";
		for (int i = 0; i < expressionList.size(); i++) {
			expressionStr += expressionList.get(i);
			
			if (i != expressionList.size() - 1) {
				expressionStr += ";";
			}
		}
		
		return expressionStr;
	}

}
