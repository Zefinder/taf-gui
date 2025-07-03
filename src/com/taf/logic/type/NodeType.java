package com.taf.logic.type;

import java.util.HashSet;
import java.util.Set;

import com.taf.logic.type.parameter.InstanceNumberParameter;
import com.taf.logic.type.parameter.MaxInstanceParameter;
import com.taf.logic.type.parameter.MinInstanceParameter;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.manager.ConstantManager;
import com.taf.util.HashSetBuilder;

public class NodeType extends Type {

	private static final HashSet<Class<? extends TypeParameter>> ALLOWED_TYPE_PARAMETERS = new HashSetBuilder<Class<? extends TypeParameter>>()
			.add(MaxInstanceParameter.class).add(MinInstanceParameter.class).build();

	private InstanceNumberParameter instanceNumber;
	private MinInstanceParameter min;
	private MaxInstanceParameter max;

	public NodeType() {
		instanceNumber = new InstanceNumberParameter(ConstantManager.DEFAULT_INSTANCE_NUMBER);
		min = new MinInstanceParameter(ConstantManager.DEFAULT_MIN_INSTANCE_NUMBER);
		max = new MaxInstanceParameter(ConstantManager.DEFAULT_MIN_INSTANCE_NUMBER);
	}

	public void editMin(int minValue) {
		min.setValue(minValue);
	}

	public int getMin() {
		return min.getValue().intValue();
	}

	public void editMax(int maxValue) {
		max.setValue(maxValue);
	}

	public int getMax() {
		return max.getValue().intValue();
	}

	public void editInstanceNumber(int number) {
		instanceNumber.setInstanceNumber(number);
	}

	public int getInstanceNumber() {
		return instanceNumber.getInstanceNumber();
	}

	@Override
	public void addTypeParameter(TypeParameter typeParameter) {
		return;
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
