package com.taf.logic.type;

import java.util.HashSet;
import java.util.Set;

import com.taf.logic.type.parameter.DepthNumberParameter;
import com.taf.logic.type.parameter.InstanceNumberParameter;
import com.taf.logic.type.parameter.MaxDepthParameter;
import com.taf.logic.type.parameter.MaxInstanceParameter;
import com.taf.logic.type.parameter.MinDepthParameter;
import com.taf.logic.type.parameter.MinInstanceParameter;
import com.taf.logic.type.parameter.ReferenceParameter;
import com.taf.logic.type.parameter.TypeNameParameter;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.manager.ConstantManager;
import com.taf.util.HashSetBuilder;

public class NodeType extends FieldType {

	private static final HashSet<String> OPTIONAL_TYPE_PARAMETERS = new HashSetBuilder<String>()
			.add(InstanceNumberParameter.PARAMETER_NAME).add(MinInstanceParameter.PARAMETER_NAME)
			.add(MaxInstanceParameter.PARAMETER_NAME).add(TypeNameParameter.PARAMETER_NAME)
			.add(ReferenceParameter.PARAMETER_NAME).add(DepthNumberParameter.PARAMETER_NAME)
			.add(MinDepthParameter.PARAMETER_NAME).add(MaxDepthParameter.PARAMETER_NAME).build();

	private static final String EMPTY_TYPE_REF_NAME = "";

	private TypeNameParameter typeName;
	private ReferenceParameter refName;
	private InstanceNumberParameter instanceNumber;
	private MinInstanceParameter minInstance;
	private MaxInstanceParameter maxInstance;
	private DepthNumberParameter depthNumber;
	private MinDepthParameter minDepth;
	private MaxDepthParameter maxDepth;

	private boolean minMaxInstance;
	private boolean minMaxDepth;
	private boolean hasType;
	private boolean hasRef;

	public NodeType() {
		typeName = new TypeNameParameter(EMPTY_TYPE_REF_NAME);
		refName = new ReferenceParameter(EMPTY_TYPE_REF_NAME);
		instanceNumber = new InstanceNumberParameter(ConstantManager.DEFAULT_INSTANCE_NUMBER);
		minInstance = new MinInstanceParameter(ConstantManager.DEFAULT_MIN_INSTANCE_NUMBER);
		maxInstance = new MaxInstanceParameter(ConstantManager.DEFAULT_MIN_INSTANCE_NUMBER);
		depthNumber = new DepthNumberParameter(ConstantManager.DEFAULT_DEPTH_NUMBER);
		minDepth = new MinDepthParameter(ConstantManager.DEFAULT_MIN_DEPTH_NUMBER);
		maxDepth = new MaxDepthParameter(ConstantManager.DEFAULT_MAX_DEPTH_NUMBER);
	}

	public void editMinInstanceNumber(int minValue) {
		minInstance.setValue(minValue);
	}

	public int getMinInstanceNumber() {
		return minInstance.getValue().intValue();
	}

	public void editMaxInstanceNumber(int maxValue) {
		maxInstance.setValue(maxValue);
	}

	public int getMaxInstanceNumber() {
		return maxInstance.getValue().intValue();
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

	public void editMinDepth(int minValue) {
		minDepth.setValue(minValue);
	}

	public int getMinDepth() {
		return minDepth.getValue().intValue();
	}

	public void editMaxDepth(int maxValue) {
		maxDepth.setValue(maxValue);
	}

	public int getMaxDepth() {
		return maxDepth.getValue().intValue();
	}

	public void editDepthNumber(int number) {
		depthNumber.setDepthNumber(number);
	}

	public int getDepthNumber() {
		return depthNumber.getDepthNumber();
	}

	public boolean hasMinMaxDepth() {
		return minMaxDepth;
	}

	public void setMinMaxDepth(boolean minMaxDepth) {
		this.minMaxDepth = minMaxDepth;
	}

	public void setType(String typeName) {
		this.typeName.setTypeName(typeName);
		hasType = true;
		hasRef = false;
	}

	public void setReference(String refName) {
		this.refName.setReferenceName(refName);
		hasRef = true;
		hasType = false;
	}

	public void removeType() {
		typeName.setTypeName(EMPTY_TYPE_REF_NAME);
		refName.setReferenceName(EMPTY_TYPE_REF_NAME);
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
			return refName.valueToString();
		}
		return super.getName();
	}

	@Override
	public void addTypeParameter(TypeParameter typeParameter) {
		if (typeParameter instanceof InstanceNumberParameter) {
			instanceNumber.setInstanceNumber(((InstanceNumberParameter) typeParameter).getInstanceNumber());
			setMinMaxInstance(false);
		} else if (typeParameter instanceof MinInstanceParameter) {
			minInstance.setValue(((MinInstanceParameter) typeParameter).getValue());
			setMinMaxInstance(true);
		} else if (typeParameter instanceof MaxInstanceParameter) {
			maxInstance.setValue(((MaxInstanceParameter) typeParameter).getValue());
			setMinMaxInstance(true);
		} else if (typeParameter instanceof TypeNameParameter) {
			setType(typeParameter.valueToString());
		} else if (typeParameter instanceof ReferenceParameter) {
			setReference(typeParameter.valueToString());
		} else if (typeParameter instanceof DepthNumberParameter) {
			depthNumber.setDepthNumber(((DepthNumberParameter) typeParameter).getDepthNumber());
			setMinMaxDepth(false);
		} else if (typeParameter instanceof MinDepthParameter) {
			minDepth.setValue(((MinDepthParameter) typeParameter).getValue());
			setMinMaxDepth(true);
		} else if (typeParameter instanceof MaxDepthParameter) {
			maxDepth.setValue(((MaxDepthParameter) typeParameter).getValue());
			setMinMaxDepth(true);
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
	public String typeToString() {
		final String separator = ConstantManager.PARAMETER_SEPARATOR;
		String typeStr = "";

		if (hasType) {
			typeStr += typeName.toString() + separator;
		} else if (hasRef) {
			typeStr += refName.toString() + separator;
		}

		if (minMaxInstance) {
			typeStr += minInstance.toString() + separator + maxInstance.toString();
		} else {
			typeStr += instanceNumber.toString();
		}
		
		if (isRecursiveNode()) {
			if (minMaxDepth) {
				typeStr += separator + minDepth.toString() + separator + maxDepth.toString();
			} else {
				typeStr += separator + depthNumber.toString();
			}
		}

		return typeStr.stripLeading();
	}

}
