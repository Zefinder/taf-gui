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
package com.taf.logic.type.parameter;

import com.taf.annotation.NotNull;
import com.taf.exception.ParseException;
import com.taf.logic.field.Node;

/**
 * The InstanceNumberParameter parameter represents the instance number of a
 * node. This value is a positive integer.
 * 
 * @see TypeParameter
 * @see Node
 *
 * @author Adrien Jakubiak
 */
public class InstanceNumberParameter extends TypeParameter {

	private static final String ERROR_MESSAGE = "Instance number must be an integer!";

	/** The parameter name. */
	public static final String PARAMETER_NAME = "nb_instances";

	/** The instance number. */
	private int instanceNumber;

	/**
	 * Instantiates a new instance number parameter with an instance value.
	 *
	 * @param instanceNumber the instance number
	 */
	public InstanceNumberParameter(int instanceNumber) {
		this();
		setInstanceNumber(instanceNumber);
	}

	/**
	 * Instantiates a new instance number parameter.
	 */
	InstanceNumberParameter() {
		super(PARAMETER_NAME);
	}

	/**
	 * Returns the instance number.
	 *
	 * @return the instance number
	 */
	public int getInstanceNumber() {
		return instanceNumber;
	}

	/**
	 * Sets the instance number.
	 *
	 * @param instanceNumber the new instance number
	 */
	public void setInstanceNumber(int instanceNumber) {
		this.instanceNumber = instanceNumber < 0 ? 0 : instanceNumber;
	}

	@Override
	public void stringToValue(String stringValue) throws ParseException {
		try {
			setInstanceNumber(Integer.valueOf(stringValue));
		} catch (NumberFormatException e) {
			throw new ParseException(this.getClass(), ERROR_MESSAGE);
		}
	}

	@Override
	@NotNull
	public String valueToString() {
		return String.valueOf(instanceNumber);
	}

}
