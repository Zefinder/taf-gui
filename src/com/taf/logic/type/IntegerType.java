package com.taf.logic.type;

import java.util.HashSet;

import com.taf.logic.type.parameter.MaxParameter;
import com.taf.logic.type.parameter.MinParameter;
import com.taf.logic.type.parameter.TypeNameParameter;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.manager.ConstantManager;
import com.taf.util.HashSetBuilder;

public class IntegerType extends Type {

	private static final String TYPE_NAME = "integer";
	private static final HashSet<Class<? extends TypeParameter>> ALLOWED_TYPE_PARAMETERS = new HashSetBuilder<Class<? extends TypeParameter>>()
			.add(MaxParameter.class)
			.add(MinParameter.class)
			.build();

	private TypeParameter typeName;
	private MinParameter min;
	private MaxParameter max;

	public IntegerType() {
		typeName = new TypeNameParameter(TYPE_NAME);
	}

	public void addMinParameter(long minValue) {
		if (min == null) {
			min = new MinParameter(minValue, false);
		} else {
			min.setValue(minValue);
		}
	}

	public void editMinParameter(long minValue) {
		if (min != null) {
			min.setValue(minValue);
		}
	}

	public void addMaxParameter(long maxValue) {
		if (max == null) {
			max = new MaxParameter(maxValue, false);
		} else {
			max.setValue(maxValue);
		}
	}

	public void editMaxParameter(long maxValue) {
		if (max != null) {
			max.setValue(maxValue);
		}
	}

	@Override
	public boolean isAllowedTypeParameter(TypeParameter typeParameter) {
		return ALLOWED_TYPE_PARAMETERS.contains(typeParameter.getClass());
	}

	@Override
	public String typeToString() {
		final String separator = ConstantManager.PARAMETER_SEPARATOR;
		String typeStr = typeName.toString();

		if (min != null) {
			typeStr += separator + min.toString();
		}

		if (max != null) {
			typeStr += separator + max.toString();
		}

		return typeStr;
	}

}
