package com.taf.logic.type;

import java.util.HashSet;
import java.util.Set;

import com.taf.logic.type.parameter.TypeNameParameter;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.logic.type.parameter.ValuesParameter;
import com.taf.logic.type.parameter.WeightsParameter;
import com.taf.manager.ConstantManager;
import com.taf.util.HashSetBuilder;

public class BooleanType extends Type {
		
	private static final String PARAMETER_FORMAT = "%s %s %s";

	private static final HashSet<String> OPTIONAL_TYPE_PARAMETERS = new HashSetBuilder<String>()
			.add(ValuesParameter.PARAMETER_NAME).add(WeightsParameter.PARAMETER_NAME).build();
	
	public static final String TYPE_NAME = "boolean";
	
	private TypeNameParameter typeName;
	private ValuesParameter values;
	
	public BooleanType() {
		typeName = new TypeNameParameter(TYPE_NAME);
		values = new ValuesParameter();
		values.addValue(ConstantManager.FALSE_VALUE);
		values.addValue(ConstantManager.TRUE_VALUE);
	}
	
	public void editFalseWeight(int weight) {
		values.setWeight(ConstantManager.FALSE_VALUE, weight);
	}
	
	public int getFalseWeight() {
		return values.getWeight(ConstantManager.FALSE_VALUE);
	}
	
	public void editTrueWeight(int weight) {
		values.setWeight(ConstantManager.TRUE_VALUE, weight);
	}

	public int getTrueWeight() {
		return values.getWeight(ConstantManager.TRUE_VALUE);
	}
	
	@Override
	public void addTypeParameter(TypeParameter typeParameter) {
		// Ignore values
		if (typeParameter instanceof WeightsParameter) {
			values.setWeights(((WeightsParameter) typeParameter).getWeights());
		}
	}
	
	@Override
	public Set<String> getMandatoryParametersName() {
		return new HashSet<String>();
	}
	
	@Override
	public Set<String> getOptionalParametersName() {
		return OPTIONAL_TYPE_PARAMETERS;
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
		TypeParameter weights = values.createWeightParameter();
		return PARAMETER_FORMAT.formatted(typeName.toString(), values.toString(), weights.toString());
	}

}
