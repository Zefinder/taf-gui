package com.taf.manager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ConstantManager {

	public static final DecimalFormat REAL_FORMATTER = new DecimalFormat("0.0##",
			DecimalFormatSymbols.getInstance(Locale.US));

	private ConstantManager() {
	}

}
