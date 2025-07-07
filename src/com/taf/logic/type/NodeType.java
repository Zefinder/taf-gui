package com.taf.logic.type;

import java.util.HashSet;
import java.util.Set;

import com.taf.logic.type.parameter.InstanceNumberParameter;
import com.taf.logic.type.parameter.MaxInstanceParameter;
import com.taf.logic.type.parameter.MinInstanceParameter;
import com.taf.logic.type.parameter.TypeNameParameter;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.manager.ConstantManager;
import com.taf.util.HashSetBuilder;

public class NodeType extends FieldType {

	private static final HashSet<Class<? extends TypeParameter>> ALLOWED_TYPE_PARAMETERS = new HashSetBuilder<Class<? extends TypeParameter>>()
			.add(MaxInstanceParameter.class).add(MinInstanceParameter.class).build();

	private static final HashSet<String> OPTIONAL_TYPE_PARAMETERS = new HashSetBuilder<String>()
			.add(InstanceNumberParameter.PARAMETER_NAME).add(MinInstanceParameter.PARAMETER_NAME)
			.add(MaxInstanceParameter.PARAMETER_NAME).add(TypeNameParameter.PARAMETER_NAME).build();

	private static final String EMPTY_TYPE_REF_NAME = "";

	private TypeNameParameter typeName;
	private InstanceNumberParameter instanceNumber;
	private MinInstanceParameter min;
	private MaxInstanceParameter max;

	private boolean minMaxInstance;
	private boolean hasType;
	private boolean hasRef;

	public NodeType() {
		typeName = new TypeNameParameter(EMPTY_TYPE_REF_NAME);
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

	public boolean hasMinMaxInstance() {
		return minMaxInstance;
	}

	public void setMinMaxInstance(boolean minMaxInstance) {
		this.minMaxInstance = minMaxInstance;
	}

	public void setType(String typeName) {
		this.typeName.setTypeName(typeName);
		hasType = true;
		hasRef = false;
	}

	public void setReference(String referenceName) {
		// TODO
//		this.referenceName.setReferenceName(referenceName);
//		hasRef = true;
//		hasType = false;
	}

	public void removeType() {
		typeName.setTypeName(EMPTY_TYPE_REF_NAME);
//		referenceName.setReferenceName(EMPTY_TYPE_REF_NAME);
		hasType = false;
		hasRef = false;
	}

	public boolean hasType() {
		return hasType;
	}
	
	public boolean hasRef() {
		return hasRef;
	}
	
	public boolean isRecursiveNode() {
		return hasType || hasRef;
	}

	@Override
	public String getName() {
		if (hasType) {
			return typeName.valueToString();
		} else if (hasRef) {
			// TODO
		}
		return super.getName();
	}

	@Override
	public void addTypeParameter(TypeParameter typeParameter) {
		// TODO
		return;
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
	public String typeToString() {
		final String separator = ConstantManager.PARAMETER_SEPARATOR;
		String typeStr = "";

		if (hasType) {
			typeStr += typeName.toString() + separator;
		} else if (hasRef) {
			// TODO
		}

		if (minMaxInstance) {
			typeStr += min.toString() + separator + max.toString();
		} else {
			typeStr += instanceNumber.toString();
		}

		return typeStr.stripLeading();
	}

}
