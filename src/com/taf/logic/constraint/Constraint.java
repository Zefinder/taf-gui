package com.taf.logic.constraint;

import java.util.ArrayList;
import java.util.List;

import com.taf.logic.constraint.parameter.ConstraintParameter;

public class Constraint {

	private static final String CONSTRAINT_STRING_FORMAT = """
			<constraint %s%s/>""";
	
	private String name;
	
	private List<ConstraintParameter> parameterList;
	
	public Constraint(String name) {
		this.name = name;
		parameterList = new ArrayList<ConstraintParameter>();
	}
	
	public Constraint addConstraintParameter(ConstraintParameter parameter) {
		parameterList.add(parameter);
		return this;
	}
	
	@Override
	public String toString() {
		String paramStr = "";
		for (ConstraintParameter parameter : parameterList) {
			paramStr += " " + parameter.toString();
		}
		
		return CONSTRAINT_STRING_FORMAT.formatted(name, paramStr);
	}

}
