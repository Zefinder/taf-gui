package com.taf.logic.type;

import java.util.HashSet;
import java.util.Set;

import com.taf.logic.type.parameter.InstanceNumberParameter;
import com.taf.logic.type.parameter.MaxInstanceParameter;
import com.taf.logic.type.parameter.MinInstanceParameter;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.manager.ConstantManager;
import com.taf.util.HashSetBuilder;

public class AnonymousType extends Type {

	private static final HashSet<Class<? extends TypeParameter>> ALLOWED_TYPE_PARAMETERS = new HashSetBuilder<Class<? extends TypeParameter>>()
			.add(MaxInstanceParameter.class).add(MinInstanceParameter.class).build();

	private InstanceNumberParameter instanceNumber;
	private MinInstanceParameter min;
	private MaxInstanceParameter max;

	public AnonymousType() {
		instanceNumber = new InstanceNumberParameter(1);
	}

	public void addMinMaxInstanceParameter(int minValue, int maxValue) {
		// Both are linked, if one is null, both are null
		if (min == null) {
			min = new MinInstanceParameter(minValue);
			max = new MaxInstanceParameter(maxValue);
		} else {
			min.setValue(minValue);
			min.setValue(maxValue);
		}
	}

	public void editMinInstanceParameter(int minValue) {
		if (min != null) {
			min.setValue(minValue);
		}
	}
	
	public int getMinInstanceParameter() {
		if (min == null) {
			return 0;
		}
		
		return min.getValue().intValue();
	}

	public void editMaxInstanceParameter(int maxValue) {
		if (max != null) {
			max.setValue(maxValue);
		}
	}
	
	public int getMaxInstanceParameter() {
		if (max == null) {
			return 0;
		}
		
		return max.getValue().intValue();
	}
	
	public boolean hasMinMaxInstance() {
		return min != null;
	}
	
	public void editInstanceNumberParameter(int number) {
		instanceNumber.setInstanceNumber(number);
	}
	
	public int getInstanceNumberParameter() {
		return instanceNumber.getInstanceNumber();
	}
	
	public void removeMinMaxInstanceParameter() {
		min = null;
		max = null;
	}

	@Override
	public Set<Class<? extends TypeParameter>> getMandatoryParametersName() {
		return new HashSet<Class<? extends TypeParameter>>();
	}
	
	@Override
	public Set<Class<? extends TypeParameter>> getOptionalParametersName() {
		return new HashSet<Class<? extends TypeParameter>>();
	}

	@Override
	public boolean isAllowedTypeParameter(TypeParameter typeParameter) {
		return ALLOWED_TYPE_PARAMETERS.contains(typeParameter.getClass());
	}

	@Override
	public String typeToString() {
		final String separator = ConstantManager.PARAMETER_SEPARATOR;
		String typeStr = "";

		if (min != null) {
			typeStr += min.toString() + separator + max.toString();
		} else {
			typeStr += instanceNumber.toString();
		}

		return typeStr.stripLeading();
	}

}
