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

import java.util.Set;

import com.taf.logic.field.Field;
import com.taf.logic.field.Node;
import com.taf.logic.field.Parameter;
import com.taf.logic.field.Type;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.util.Consts;

/**
 * <p>
 * The FieldType class represents the type a Field can take. Depending on the
 * the nature of the Field, different FieldTypes can be used. For example, a
 * {@link Parameter} will use types that represent coding types whereas a
 * {@link Node} will take a {@link Type} or another {@link Node} as type, stored
 * in a special node type.
 * </p>
 * 
 * <p>
 * This class sets up basic bricks to create a FieldType. It expects the coder
 * to implement four methods:
 * <ul>
 * <li>{@link #addTypeParameter(TypeParameter)} which adds a type parameter to
 * the field type.
 * <li>{@link #getMandatoryParametersName()} which returns a set of mandatory
 * parameters names, as specified in the TAF documentation.
 * <li>{@link #getOptionalParametersName()} which returns a set of optional
 * parameters names, as specified in the TAF documentation.
 * <li>{@link #typeToString()} which forces the FieldType to implement the
 * {@link #toString()} method.
 * </ul>
 * </p>
 * 
 * <p>
 * Field types are created using the FieldTypeFactory (TODO) class. It requires
 * at least one constructor with no argument.
 * </p>
 * 
 * @see Field
 * @see TypeParameter
 * 
 * @author Adrien Jakubiak
 */
public abstract class FieldType {

	/**
	 * Instantiates a new field type.
	 */
	public FieldType() {
	}

	/**
	 * Adds a type parameter to the field type. This type parameter must be
	 * specified in the sets given by {@link #getMandatoryParametersName()} or
	 * {@link #getOptionalParametersName()} to be processed. Else it will simply be
	 * ignored.
	 *
	 * @param typeParameter the type parameter to add
	 */
	public abstract void addTypeParameter(TypeParameter typeParameter);

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FieldType)) {
			return false;
		}

		FieldType other = (FieldType) obj;
		return this.getName().equals(other.getName()) && this.toString().equals(other.toString());
	}

	/**
	 * Returns a set with the mandatory parameters name that can be added to the
	 * field type. These parameters are mandatory to generate a valid TAF parameter.
	 * They will be checked when reading an XML file or a TAF save file. An
	 * exception will be thrown if they are not present. When they are created by
	 * the user, these parameters are automatically generated with default values.
	 *
	 * @return the set of mandatory parameters name
	 */
	public abstract Set<String> getMandatoryParametersName();

	/**
	 * Returns the field type name.
	 *
	 * @return the field type name
	 */
	public String getName() {
		return "";
	}

	/**
	 * Returns a set with the optional parameters name that can be added to the
	 * field type. These parameters are optional to generate a valid TAF parameter.
	 * They will be checked when reading an XML file or a TAF save file but no
	 * exception will be thrown if they are not present. When they are created by
	 * the user, these parameters are automatically generated with default values.
	 *
	 * @return the set of optional parameters name
	 */
	public abstract Set<String> getOptionalParametersName();

	@Override
	public int hashCode() {
		return (this.getClass().toString() + Consts.HASH_SEPARATOR + getName() + toString()).hashCode();
	}

	@Override
	public String toString() {
		return typeToString();
	}

	/**
	 * Returns a string representation of the field type. This method is a way to
	 * force the coder to create a {@link #toString()} method.
	 *
	 * @return the string representation of the field type
	 */
	public abstract String typeToString();

}
