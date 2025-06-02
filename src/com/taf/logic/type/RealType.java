package com.taf.logic.type;

import java.util.HashSet;

import com.taf.logic.type.parameter.MaxParameter;
import com.taf.logic.type.parameter.MinParameter;
import com.taf.logic.type.parameter.TypeNameParameter;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.manager.ConstantManager;
import com.taf.util.HashSetBuilder;

public class RealType extends Type {

	private static final String TYPE_NAME = "real";
	private static final HashSet<Class<? extends TypeParameter>> ALLOWED_TYPE_PARAMETERS = new HashSetBuilder<Class<? extends TypeParameter>>()
			.add(MaxParameter.class)
			.add(MinParameter.class)
			.build();

	private TypeParameter typeName;
	private MinParameter min;
	private MaxParameter max;

	public RealType() {
		typeName = new TypeNameParameter(TYPE_NAME);
	}

	public void addMinParameter(double minValue) {
		if (min == null) {
			min = new MinParameter(minValue, true);
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
			max = new MaxParameter(maxValue, true);
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
