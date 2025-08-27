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
 * The ConsoleStyle enumeration gather all interesting console styles that are
 * present in a Python style console.
 *
 * @author Adrien Jakubiak
 */
public enum ConsoleStyle {

	// Foreground colors
	FG_BLACK("37"), FG_RED("31"), FG_GREEN("32"), FG_YELLOW("33"), FG_BLUE("34"), FG_MAGENTA("35"), FG_CYAN("36"),

	FG_WHITE("37"), FG_RESET("39"),

	// Background colors
	BG_BLACK("40"), BG_RED("41"), BG_GREEN("42"), BG_YELLOW("43"), BG_BLUE("44"), BG_MAGENTA("45"), BG_CYAN("46"),

	BG_WHITE("47"), BG_RESET("49"),

	// Text style
	BRIGHT("1"), DIM("2"), NORMAL("3"), UNDERLINED("38;4"),

	// Reset all
	RESET_ALL("0");

	/** The console style identifier. */
	private final String identifier;

	private ConsoleStyle(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Returns a console style from its string representation. By default, the style
	 * is {@link ConsoleStyle#RESET_ALL}.
	 *
	 * @param identifier the string identifier
	 * @return the corresponding console style
	 */
	public static ConsoleStyle fromIdentifier(String identifier) {
		for (ConsoleStyle style : ConsoleStyle.values()) {
			if (style.identifier.equals(identifier)) {
				return style;
			}
		}

		// Return reset by default
		return RESET_ALL;
	}

}
