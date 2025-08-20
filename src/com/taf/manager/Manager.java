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
package com.taf.manager;

import java.util.Set;

import com.taf.util.HashSetBuilder;

/**
 * <p>
 * A Manager is a class representing a singleton that manages a single aspect of
 * the code.
 * </p>
 * 
 * <p>
 * It must be implemented as a singleton with a private constructor and a static
 * method <code>getInstance()</code> returning itself. TODO
 * </p>
 * 
 * @author Adrien Jakubiak
 */
public abstract class Manager {

	/** The set of managers. */
	static final Set<Manager> MANAGERS = new HashSetBuilder<Manager>()
			.add(TypeManager.getInstance())
			.add(EventManager.getInstance())
			.add(SaveManager.getInstance())
			.add(RunManager.getInstance())
			.add(SettingsManager.getInstance())
			.build();
	
	/** The init state. */
	private static boolean initDone = false;
	
	protected Manager() {
	}
	
	/**
	 * Initializes the manager.
	 */
	public abstract void initManager();
	
	/**
	 * Clears the manager.
	 */
	public abstract void clearManager();
	
	/**
	 * Initializes all managers.
	 */
	public static final void initAllManagers() {
		if (!initDone) {			
			for (Manager manager : MANAGERS) {
				manager.initManager();
			}
			initDone = true;
		}
	}
	
	/**
	 * Clears all managers.
	 */
	public static final void clearAllManagers() {
		if (initDone) {
			for (Manager manager : MANAGERS) {
				manager.clearManager();
			}
			initDone = false;
		}
	}
}
