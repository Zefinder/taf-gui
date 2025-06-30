package com.taf.util;

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

	private final String identifier;

	private ConsoleStyle(String identifier) {
		this.identifier = identifier;
	}

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
