package com.taf.logic.type;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import com.taf.logic.type.parameter.TypeNameParameter;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.logic.type.parameter.ValuesParameter;
import com.taf.logic.type.parameter.WeightsParameter;
import com.taf.manager.ConstantManager;
import com.taf.util.HashSetBuilder;

public class StringType extends Type {

	public static final String TYPE_NAME = "string";

	private static final HashSet<String> MANDATORY_TYPE_PARAMETERS = new HashSetBuilder<String>()
			.add(ValuesParameter.PARAMETER_NAME).add(WeightsParameter.PARAMETER_NAME).build();

	private TypeParameter typeName;
	private ValuesParameter values;

	public StringType() {
		typeName = new TypeNameParameter(TYPE_NAME);
		values = new ValuesParameter();
	}

	public boolean addValue(String value, int weight) {
		return values.addValue(value, weight);
	}

	public boolean addValue(String value) {
		return values.addValue(value);
	}

	public boolean editValueName(String oldValue, String newValue) {
		return values.editValueName(oldValue, newValue);
	}

	public boolean setWeight(String value, int weight) {
		return values.setWeight(value, weight);
	}

	public boolean removeValue(String value) {
		return values.removeValue(value);
	}

	public Set<Entry<String, Integer>> getValues() {
		return values.getValues();
	}

	@Override
	public void addTypeParameter(TypeParameter typeParameter) {
		// We assume that ValuesParameter comes first and then WeightParameter
		// We also assume that weights and values are ordered the same way
		if (typeParameter instanceof ValuesParameter) {
			for (Entry<String, Integer> value : ((ValuesParameter) typeParameter).getValues()) {
				values.addValue(value.getKey());
			}
		} else if (typeParameter instanceof WeightsParameter) {
			values.setWeights(((WeightsParameter) typeParameter).getWeights());
		}
	}

	@Override
	public Set<String> getMandatoryParametersName() {
		return MANDATORY_TYPE_PARAMETERS;
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
		final String separator = ConstantManager.PARAMETER_SEPARATOR;
		String typeStr = typeName.toString();
		typeStr += separator + values.toString();
		typeStr += separator + values.createWeightParameter().toString();
		return typeStr;
	}

}
