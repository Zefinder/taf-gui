/*
 * Copyright or © or Copr.
 * 
 * This software is a computer program whose purpose is to generate random test
 * case from a template file describing the data model.
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software. You can use, modify
 * and/or redistribute the software under the terms of the CeCILL-B license as
 * circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * 
 * As a counterpart to the access to the source code and rights to copy, modify
 * and redistribute granted by the license, users are provided only with a
 * limited warranty and the software's author, the holder of the economic
 * rights, and the successive licensors have only limited liability.
 * 
 * In this respect, the user's attention is drawn to the risks associated with
 * loading, using, modifying and/or developing or reproducing the software by
 * the user in light of its specific status of free software, that may mean that
 * it is complicated to manipulate, and that also therefore means that it is
 * reserved for developers and experienced professionals having in-depth
 * computer knowledge. Users are therefore encouraged to load and test the
 * software's suitability as regards their requirements in conditions enabling
 * the security of their systems and/or data to be ensured and, more generally,
 * to use and operate it in the same conditions as regards security.
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package com.taf.logic.type;

import java.util.HashSet;
import java.util.Set;

import com.taf.logic.field.Node;
import com.taf.logic.type.parameter.DepthNumberParameter;
import com.taf.logic.type.parameter.InstanceNumberParameter;
import com.taf.logic.type.parameter.MaxDepthParameter;
import com.taf.logic.type.parameter.MaxInstanceParameter;
import com.taf.logic.type.parameter.MinDepthParameter;
import com.taf.logic.type.parameter.MinInstanceParameter;
import com.taf.logic.type.parameter.ReferenceParameter;
import com.taf.logic.type.parameter.TypeNameParameter;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.manager.TypeManager;
import com.taf.util.Consts;
import com.taf.util.HashSetBuilder;

/**
 * <p>
 * A NodeType is the type used by {@link Node}s to store their informations.
 * Nodes in TAF are represented by their number of instances (or a range of
 * number of instances), and can be recursive if they have either a type or make
 * reference to another node. In this case, they will also be represented by
 * their generation depth (or a range of depth).
 * </p>
 * 
 * <p>
 * All these informations are stored in the form of parameters. The NodeType has
 * eight optional parameters:
 * <ul>
 * <li>{@link InstanceNumberParameter} the number of instances of a node
 * <li>{@link MinInstanceParameter} the minimum number of instances of a node
 * <li>{@link MaxInstanceParameter} the maximum number of instances of a node
 * <li>{@link TypeNameParameter} the type of a node (makes it recursive)
 * <li>{@link ReferenceParameter} the reference of a node (makes it recursive)
 * <li>{@link DepthNumberParameter} the depth of a node (relevant if recursive)
 * <li>{@link MinDepthParameter} the minimum depth of a node (relevant if
 * recursive)
 * <li>{@link MaxDepthParameter} the maximum depth of a node (relevant if
 * recursive)
 * </ul>
 * </p>
 * 
 * <p>
 * The string representation of a node does not contain a parameter specifying
 * that a node is recursive. This is determined by the presence of a type or a
 * reference.
 * </p>
 * 
 * <p>
 * The string representation of a node does not contain a parameter specifying
 * that a node has a minimum and a maximum number of instances (resp. depth).
 * This implementation makes the assumption that if a minimum or maximum value
 * is specified, then the node will have a minimum and maximum number of
 * instances (resp. depth).
 * </p>
 * 
 * <p>
 * TAF requires that a node has either a number of instance or a minimum and a
 * maximum number of instances. This is not the case here since the node is
 * created with a default value of a single node instance. Same behavior, when
 * there is a minimum (or maximum) instance value, TAF will force the user to
 * input a maximum (or minimum) instance value. This is not the case here since
 * a default value is also set.
 * </p>
 * 
 * @see FieldType
 * @see InstanceNumberParameter
 * @see MinInstanceParameter
 * @see MaxInstanceParameter
 * @see TypeNameParameter
 * @see ReferenceParameter
 * @see DepthNumberParameter
 * @see MinDepthParameter
 * @see MaxDepthParameter
 *
 * @author Adrien Jakubiak
 */
public class NodeType extends FieldType {

	/** The optional type parameters. */
	private static final HashSet<String> OPTIONAL_TYPE_PARAMETERS = new HashSetBuilder<String>()
			.add(InstanceNumberParameter.PARAMETER_NAME).add(MinInstanceParameter.PARAMETER_NAME)
			.add(MaxInstanceParameter.PARAMETER_NAME).add(TypeNameParameter.PARAMETER_NAME)
			.add(ReferenceParameter.PARAMETER_NAME).add(DepthNumberParameter.PARAMETER_NAME)
			.add(MinDepthParameter.PARAMETER_NAME).add(MaxDepthParameter.PARAMETER_NAME).build();

	/** String representation of the type of a node when it has no type. */
	private static final String EMPTY_TYPE_REF_NAME = "";

	/** The field type name. */
	private TypeNameParameter typeName;

	/** The reference name of a node. */
	private ReferenceParameter refName;

	/** The instance number of a node. */
	private InstanceNumberParameter instanceNumber;

	/** The minimum instance number of a node. */
	private MinInstanceParameter minInstance;

	/** The maximum instance number of a node. */
	private MaxInstanceParameter maxInstance;

	/** The depth number of a node. */
	private DepthNumberParameter depthNumber;

	/** The minimum depth of a node. */
	private MinDepthParameter minDepth;

	/** The maximum depth of a node. */
	private MaxDepthParameter maxDepth;

	/** Represents a node that has a minimum and maximum number of instances. */
	private boolean minMaxInstance;

	/** Represents a node that has a minimum and maximum depth. */
	private boolean minMaxDepth;

	/** Represents a node that has a type. */
	private boolean hasType;

	/** Represents a node that makes reference to another node. */
	private boolean hasRef;

	/**
	 * Instantiates a new node type.
	 */
	public NodeType() {
		typeName = new TypeNameParameter(EMPTY_TYPE_REF_NAME);
		refName = new ReferenceParameter(EMPTY_TYPE_REF_NAME);
		instanceNumber = new InstanceNumberParameter(Consts.DEFAULT_INSTANCE_NUMBER);
		minInstance = new MinInstanceParameter(Consts.DEFAULT_MIN_INSTANCE_NUMBER);
		maxInstance = new MaxInstanceParameter(Consts.DEFAULT_MAX_INSTANCE_NUMBER);
		depthNumber = new DepthNumberParameter(Consts.DEFAULT_DEPTH_NUMBER);
		minDepth = new MinDepthParameter(Consts.DEFAULT_MIN_DEPTH_NUMBER);
		maxDepth = new MaxDepthParameter(Consts.DEFAULT_MAX_DEPTH_NUMBER);
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

	/**
	 * Edits the depth number.
	 *
	 * @param number the number
	 */
	public void editDepthNumber(int number) {
		depthNumber.setDepthNumber(number);
	}

	/**
	 * Edits the instance number.
	 *
	 * @param number the number
	 */
	public void editInstanceNumber(int number) {
		instanceNumber.setInstanceNumber(number);
	}

	/**
	 * Edits the maximum depth.
	 *
	 * @param maxValue the maximum value
	 */
	public void editMaxDepth(int maxValue) {
		maxDepth.setValue(maxValue);
	}

	/**
	 * Edits the maximum instance number.
	 *
	 * @param maxValue the maximum value
	 */
	public void editMaxInstanceNumber(int maxValue) {
		maxInstance.setValue(maxValue);
	}

	/**
	 * Edits the minimum depth.
	 *
	 * @param minValue the minimum value
	 */
	public void editMinDepth(int minValue) {
		minDepth.setValue(minValue);
	}

	/**
	 * Edits the minimum instance number.
	 *
	 * @param minValue the minimum value
	 */
	public void editMinInstanceNumber(int minValue) {
		minInstance.setValue(minValue);
	}

	/**
	 * Returns the depth number.
	 *
	 * @return the depth number
	 */
	public int getDepthNumber() {
		return depthNumber.getDepthNumber();
	}

	/**
	 * Returns the instance number.
	 *
	 * @return the instance number
	 */
	public int getInstanceNumber() {
		return instanceNumber.getInstanceNumber();
	}

	@Override
	public Set<String> getMandatoryParametersName() {
		return new HashSet<String>();
	}

	/**
	 * Returns the maximum depth.
	 *
	 * @return the maximum depth
	 */
	public int getMaxDepth() {
		return maxDepth.getValue().intValue();
	}

	/**
	 * Returns the maximum instance number.
	 *
	 * @return the maximum instance number
	 */
	public int getMaxInstanceNumber() {
		return maxInstance.getValue().intValue();
	}

	/**
	 * Returns the minimum depth.
	 *
	 * @return the minimum depth
	 */
	public int getMinDepth() {
		return minDepth.getValue().intValue();
	}

	/**
	 * Returns the minimum instance number.
	 *
	 * @return the minimum instance number
	 */
	public int getMinInstanceNumber() {
		return minInstance.getValue().intValue();
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
	public Set<String> getOptionalParametersName() {
		return OPTIONAL_TYPE_PARAMETERS;
	}

	/**
	 * Checks for minimum and maximum depth.
	 *
	 * @return true if the node has a minimum and maximum depth
	 */
	public boolean hasMinMaxDepth() {
		return minMaxDepth;
	}

	/**
	 * Checks for minimum and maximum instance.
	 *
	 * @return true if the node has a minimum and maximum instance
	 */
	public boolean hasMinMaxInstance() {
		return minMaxInstance;
	}

	/**
	 * Checks for reference.
	 *
	 * @return true if the node makes a reference to another node
	 */
	public boolean hasRef() {
		return hasRef;
	}

	/**
	 * Checks for type.
	 *
	 * @return true if the node has a type
	 */
	public boolean hasType() {
		return hasType;
	}

	/**
	 * Checks if is recursive node.
	 *
	 * @return true if the node is recursive
	 */
	public boolean isRecursiveNode() {
		return hasType || hasRef;
	}

	/**
	 * Removes the node recursiveness.
	 */
	public void removeType() {
		typeName.setTypeName(EMPTY_TYPE_REF_NAME);
		refName.setReferenceName(EMPTY_TYPE_REF_NAME);
		hasType = false;
		hasRef = false;
	}

	/**
	 * Sets the minimum and maximum depth.
	 *
	 * @param minMaxDepth the node's minimum and maximum depth state
	 */
	public void setMinMaxDepth(boolean minMaxDepth) {
		this.minMaxDepth = minMaxDepth;
	}

	/**
	 * Sets the minimum and maximum instance.
	 *
	 * @param minMaxInstance the node's minimum and maximum instance state
	 */
	public void setMinMaxInstance(boolean minMaxInstance) {
		this.minMaxInstance = minMaxInstance;
	}

	/**
	 * Sets the node reference as specified in the {@link TypeManager}. Makes the
	 * node recursive.
	 *
	 * @param refName the new reference
	 */
	public void setReference(String refName) {
		this.refName.setReferenceName(refName);
		hasRef = true;
		hasType = false;
	}

	/**
	 * Sets the node type as specified in the {@link TypeManager}. Makes the node
	 * recursive.
	 *
	 * @param typeName the new type
	 */
	public void setType(String typeName) {
		this.typeName.setTypeName(typeName);
		hasType = true;
		hasRef = false;
	}

	@Override
	public String typeToString() {
		final String separator = Consts.PARAMETER_SEPARATOR;
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
