package com.taf.logic.type;

import java.util.HashSet;
import java.util.Set;

import com.taf.logic.type.parameter.MaxIntegerParameter;
import com.taf.logic.type.parameter.MinIntegerParameter;
import com.taf.logic.type.parameter.TypeNameParameter;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.manager.ConstantManager;
import com.taf.util.HashSetBuilder;

public class IntegerType extends Type {

	public static final String TYPE_NAME = "integer";
	private static final HashSet<Class<? extends TypeParameter>> ALLOWED_TYPE_PARAMETERS = new HashSetBuilder<Class<? extends TypeParameter>>()
			.add(MaxIntegerParameter.class)
			.add(MinIntegerParameter.class)
			.build();
	
	private static final HashSet<Class<? extends TypeParameter>> OPTIONAL_TYPE_PARAMETERS = new HashSetBuilder<Class<? extends TypeParameter>>()
			.add(MaxIntegerParameter.class)
			.add(MinIntegerParameter.class)
			.build();

	private TypeParameter typeName;
	private MinIntegerParameter min;
	private MaxIntegerParameter max;

	public IntegerType() {
		typeName = new TypeNameParameter(TYPE_NAME);
	}

	public void addMinParameter(long minValue) {
		if (min == null) {
			min = new MinIntegerParameter(minValue);
		} else {
			min.setValue(minValue);
		}
	}

	public void editMinParameter(long minValue) {
		if (min != null) {
			min.setValue(minValue);
		}
	}
	
	public long getMinParameter() {
		if (min == null) {
			return 0;
		}
		
		return min.getValue().longValue();
	}
	
	public void removeMinParameter() {
		min = null;
	}
	
	public boolean hasMinParameter() {
		return min != null;
	}

	public void addMaxParameter(long maxValue) {
		if (max == null) {
			max = new MaxIntegerParameter(maxValue);
		} else {
			max.setValue(maxValue);
		}
	}

	public void editMaxParameter(long maxValue) {
		if (max != null) {
			max.setValue(maxValue);
		}
	}
	
	public long getMaxParameter() {
		if (max == null) {
			return 0;
		}
		
		return max.getValue().longValue();
	}
	
	public void removeMaxParameter() {
		max = null;
	}
	
	public boolean hasMaxParameter() {
		return max != null;
	}

	@Override
	public void addTypeParameter(TypeParameter typeParameter) {
		if (typeParameter instanceof MinIntegerParameter) {
			addMinParameter(((MinIntegerParameter) typeParameter).getValue().longValue());
		} else if (typeParameter instanceof MaxIntegerParameter) {
			addMaxParameter(((MaxIntegerParameter) typeParameter).getValue().longValue());
		}
	}
	
	@Override
	public Set<Class<? extends TypeParameter>> getMandatoryParametersName() {
		return new HashSet<Class<? extends TypeParameter>>();
	}
	
	@Override
	public Set<Class<? extends TypeParameter>> getOptionalParametersName() {
		return OPTIONAL_TYPE_PARAMETERS;
	}
	
	@Override
	public boolean isAllowedTypeParameter(TypeParameter typeParameter) {
		return ALLOWED_TYPE_PARAMETERS.contains(typeParameter.getClass());
	}

	@Override
	public String getName() {
		return TYPE_NAME;
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
