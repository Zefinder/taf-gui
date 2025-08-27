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
package com.taf.event.entity;

import com.taf.event.Event;
import com.taf.logic.field.Parameter;
import com.taf.logic.type.FieldType;

/**
 * The ParameterTypeChangedEvent is an event fired when an {@link Parameter}
 * changes type.
 *
 * @see Event
 *
 * @author Adrien Jakubiak
 */
public class ParameterTypeChangedEvent implements Event {

	/** The parameter. */
	private Parameter parameter;

	/** The new type. */
	private FieldType type;

	/**
	 * Instantiates a new parameter type changed event.
	 *
	 * @param parameter the field
	 * @param type  the type
	 */
	public ParameterTypeChangedEvent(Parameter parameter, FieldType type) {
		this.parameter = parameter;
		this.type = type;
	}

	/**
	 * Returns the field.
	 *
	 * @return the field
	 */
	public Parameter getParameter() {
		return parameter;
	}

	/**
	 * Returns the type.
	 *
	 * @return the type
	 */
	public FieldType getType() {
		return type;
	}

}
