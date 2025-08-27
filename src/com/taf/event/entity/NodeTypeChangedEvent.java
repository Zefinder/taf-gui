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
import com.taf.logic.field.Node;

/**
 * The NodeTypeChangedEvent is an event fired when an {@link Node} changes type.
 *
 * @see Event
 *
 * @author Adrien Jakubiak
 */
public class NodeTypeChangedEvent implements Event {

	/** The node. */
	private Node node;

	/** The previous type value. */
	private String previousValue;

	/** True if the node had a type. */
	private boolean hadType;

	/** True if the node had a ref. */
	private boolean hadRef;

	/**
	 * Instantiates a new node type changed event.
	 *
	 * @param node the node
	 */
	public NodeTypeChangedEvent(Node node) {
		this.node = node;

		this.previousValue = node.getTypeName();
		this.hadType = node.hasType();
		this.hadRef = node.hasRef();
	}

	/**
	 * Returns the node.
	 *
	 * @return the node
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * Returns the previous type value.
	 *
	 * @return the previous type value
	 */
	public String getPreviousValue() {
		return previousValue;
	}

	/**
	 * Returns true if the node had a ref.
	 *
	 * @return true if the node had a ref
	 */
	public boolean hadRef() {
		return hadRef;
	}

	/**
	 * Returns true if the node had a type.
	 *
	 * @return true if the node had a type
	 */
	public boolean hadType() {
		return hadType;
	}
}
