package com.taf.logic.type;

import java.util.HashSet;
import java.util.Set;

import com.taf.logic.type.parameter.MaxRealParameter;
import com.taf.logic.type.parameter.MinRealParameter;
import com.taf.logic.type.parameter.TypeNameParameter;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.manager.ConstantManager;
import com.taf.util.HashSetBuilder;

public class RealType extends Type {

	public static final String TYPE_NAME = "real";
	private static final HashSet<Class<? extends TypeParameter>> ALLOWED_TYPE_PARAMETERS = new HashSetBuilder<Class<? extends TypeParameter>>()
			.add(MaxRealParameter.class)
			.add(MinRealParameter.class)
			.build();
	
	private static final HashSet<String> OPTIONAL_TYPE_PARAMETERS = new HashSetBuilder<String>()
			.add(MaxRealParameter.PARAMETER_NAME)
			.add(MinRealParameter.PARAMETER_NAME)
			.build();

	private TypeParameter typeName;
	private MinRealParameter min;
	private MaxRealParameter max;

	public RealType() {
		typeName = new TypeNameParameter(TYPE_NAME);
	}

	public void addMinParameter(double minValue) {
		if (min == null) {
			min = new MinRealParameter(minValue);
		} else {
			min.setValue(minValue);
		}
	}

	public void editMinParameter(double minValue) {
		if (min != null) {
			min.setValue(minValue);
		}
	}
	
	public double getMinParameter() {
		if (min == null) {
			return 0;
		}
		
		return min.getValue().doubleValue();
	}
	
	public void removeMinParameter() {
		min = null;
	}

	public boolean hasMinParameter() {
		return min != null;
	}

	public void addMaxParameter(double maxValue) {
		if (max == null) {
			max = new MaxRealParameter(maxValue);
		} else {
			max.setValue(maxValue);
		}
	}

	public void editMaxParameter(double maxValue) {
		if (max != null) {
			max.setValue(maxValue);
		}
	}
	
	public double getMaxParameter() {
		if (max == null) {
			return 0;
		}
		
		return max.getValue().doubleValue();
	}
	
	public void removeMaxParameter() {
		max = null;
	}
	
	public boolean hasMaxParameter() {
		return max != null;
	}

	@Override
	public void addTypeParameter(TypeParameter typeParameter) {
		if (typeParameter instanceof MinRealParameter) {
			addMinParameter(((MinRealParameter) typeParameter).getValue().doubleValue());
		} else if (typeParameter instanceof MaxRealParameter) {
			addMaxParameter(((MaxRealParameter) typeParameter).getValue().doubleValue());
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
