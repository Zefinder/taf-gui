package com.taf.logic.constraint;

import java.util.ArrayList;
import java.util.List;

import com.taf.logic.Entity;
import com.taf.logic.constraint.parameter.ConstraintParameter;
import com.taf.manager.ConstantManager;

public class Constraint implements Entity {

	private static final String CONSTRAINT_STRING_FORMAT = """
			<constraint %s/>""";

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
		for (ConstraintParameter parameter : parameterList) {
			paramStr += separator + parameter.toString();
		}

		return CONSTRAINT_STRING_FORMAT.formatted(ConstantManager.FIELD_STRING_FORMAT.formatted(name, paramStr).stripTrailing());
	}

}
