package com.taf.manager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ConstantManager {

	public static final DecimalFormat REAL_FORMATTER = new DecimalFormat("0.0##",
			DecimalFormatSymbols.getInstance(Locale.US));

	public static final String PARAMETER_STRING_FORMAT = "%s=\"%s\"";
	public static final String FIELD_STRING_FORMAT = "name=\"%s\" %s";
	
	public static final String TAB = "\t";
	public static final String LINE_JUMP = "\n";

	public static final String HASH_SEPARATOR = ":";
	public static final String ELEMENT_SEPARATOR = ";";
	public static final String PARAMETER_SEPARATOR = " ";
	
	private ConstantManager() {
	}

}
