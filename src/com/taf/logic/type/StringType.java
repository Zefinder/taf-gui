package com.taf.logic.type;

import com.taf.logic.type.parameter.TypeNameParameter;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.logic.type.parameter.ValuesParameter;
import com.taf.manager.ConstantManager;

public class StringType extends Type {

	private static final String TYPE_NAME = "string";

	private TypeParameter typeName;
	private ValuesParameter values;

	public StringType() {
		typeName = new TypeNameParameter(TYPE_NAME);
		values = new ValuesParameter();
	}

	public void addValue(String value, int weight) {
		values.addValue(value, weight);
	}

	public void addValue(String value) {
		values.addValue(value);
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
