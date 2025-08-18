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
package com.taf.util;

/**
 * The OSValidator enumeration is an easy way to get the running operating
 * system in the form of an object. It is made so a switch can be used to adapt
 * to the OS.
 *
 * @author Adrien Jakubiak
 */
public enum OSValidator {

	/** Represents the Windows OS. */
	WINDOWS,
	/** Represents the Mac OS. */
	MAC,
	/** Represents the Unix OS. */
	UNIX,
	/** Unknwon OS. */
	UNKNOWN;

	/** Stores the OS name given by the system property. */
	private static final String OS_NAME = System.getProperty("os.name").toLowerCase();

	/** The cached OS value. */
	private static final OSValidator OS = getOSFromName(OS_NAME);

	/** The Windows OS name. */
	private static final String WINDOWS_NAME = "win";

	/** The Mac OS name. */
	private static final String MAC_NAME = "mac";

	/** The first Unix OS name. */
	private static final String UNIX1_NAME = "nix";

	/** The second Unix OS name. */
	private static final String UNIX2_NAME = "nux";

	/** The third Unix OS name. */
	private static final String UNIX3_NAME = "aix";

	/**
	 * Returns the OS.
	 *
	 * @return the OS
	 */
	public static OSValidator getOS() {
		return OS;
	}

	/**
	 * Checks if the OS is mac.
	 *
	 * @return true if the OS is mac
	 */
	public static boolean isMac() {
		return OS_NAME.contains(MAC_NAME);
	}

	/**
	 * Checks if the OS is unix.
	 *
	 * @return true if the OS is unix
	 */
	public static boolean isUnix() {
		return (OS_NAME.contains(UNIX1_NAME) || OS_NAME.contains(UNIX2_NAME) || OS_NAME.contains(UNIX3_NAME));
	}

	/**
	 * Checks if the OS is windows.
	 *
	 * @return true if the OS is windows
	 */
	public static boolean isWindows() {
		return OS_NAME.contains(WINDOWS_NAME);
	}

	/**
	 * Returns the OS from the given name.
	 *
	 * @param osName the OS name
	 * @return the OS from the name
	 */
	private static OSValidator getOSFromName(String osName) {
		if (isWindows()) {
			return WINDOWS;
		}
		if (isMac()) {
			return MAC;
		}
		if (isUnix()) {
			return UNIX;
		}

		return UNKNOWN;
	}

	private OSValidator() {
	}

}