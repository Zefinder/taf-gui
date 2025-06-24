package com.taf.logic.type;

import java.util.HashSet;
import java.util.Set;

import com.taf.logic.type.parameter.TypeNameParameter;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.logic.type.parameter.ValuesParameter;
import com.taf.logic.type.parameter.WeightsParameter;
import com.taf.manager.ConstantManager;

public class BooleanType extends Type {
	
	private static final int FALSE_INDEX = 0;
	private static final int TRUE_INDEX = 1;
	
	private static final String PARAMETER_FORMAT = "%s %s";
	
	public static final String TYPE_NAME = "boolean";

	private TypeNameParameter typeName;
	private ValuesParameter values;
	private WeightsParameter weights;
	
	public BooleanType() {
		typeName = new TypeNameParameter(TYPE_NAME);
		values = new ValuesParameter();
		values.addValue(ConstantManager.FALSE_VALUE);
		values.addValue(ConstantManager.TRUE_VALUE);
	}
	
	public void editFalseWeight(int weight) {
		weights.editWeight(FALSE_INDEX, weight);
	}
	
	public void editTrueWeight(int weight) {
		weights.editWeight(TRUE_INDEX, weight);
	}
	
	@Override
	public void addTypeParameter(TypeParameter typeParameter) {
	}
	
	@Override
	public Set<String> getMandatoryParametersName() {
		return new HashSet<String>();
	}
	
	@Override
	public Set<String> getOptionalParametersName() {
		return new HashSet<String>();
	}

	@Override
	public boolean isAllowedTypeParameter(TypeParameter typeParameter) {
		return false;
	}
	
	@Override
	public String getName() {
		return TYPE_NAME;
	}

	@Override
	public String typeToString() {
		return PARAMETER_FORMAT.formatted(typeName.toString(), weights.toString());
	}

}
