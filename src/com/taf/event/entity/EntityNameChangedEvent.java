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
import com.taf.logic.Entity;

/**
 * The EntityNameChangedEvent is an event fired when an {@link Entity} changes name.
 *
 * @see Event
 *
 * @author Adrien Jakubiak
 */
public class EntityNameChangedEvent implements Event {

	/** The entity. */
	private Entity entity;
	
	/** The old name. */
	private String oldName;
	
	/** The new name. */
	private String newName;
	
	/**
	 * Instantiates a new entity name changed event.
	 *
	 * @param entity the entity
	 * @param oldName the old name
	 * @param newName the new name
	 */
	public EntityNameChangedEvent(Entity entity, String oldName, String newName) {
		this.entity = entity;
		this.oldName = oldName;
		this.newName = newName;
	}
	
	/**
	 * Returns the entity.
	 *
	 * @return the entity
	 */
	public Entity getEntity() {
		return entity;
	}
	
	/**
	 * Returns the new name.
	 *
	 * @return the new name
	 */
	public String getNewName() {
		return newName;
	}
	
	/**
	 * Returns the old name.
	 *
	 * @return the old name
	 */
	public String getOldName() {
		return oldName;
	}

}
