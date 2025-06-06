package com.taf.manager;

import java.awt.GridBagConstraints;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ConstantManager extends Manager {

	private static final ConstantManager instance = new ConstantManager();

	public static final DecimalFormat REAL_FORMATTER = new DecimalFormat("0.0##",
			DecimalFormatSymbols.getInstance(Locale.US));

	public static final String PARAMETER_STRING_FORMAT = "%s=\"%s\"";
	public static final String FIELD_STRING_FORMAT = "name=\"%s\" %s";

	public static final String TAB = "\t";
	public static final String LINE_JUMP = "\n";

	public static final String HASH_SEPARATOR = ":";
	public static final String ELEMENT_SEPARATOR = ";";
	public static final String PARAMETER_SEPARATOR = " ";
	
	public static final String DEFAULT_ROOT_NAME = "test_cases";

	public static final GridBagConstraints getDefaultConstraint() {
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;

		return c;
	}

	private ConstantManager() {
	}

	@Override
	public void initManager() {
		// Nothing to do here...
	}

	public static ConstantManager getInstance() {
		return instance;
	}

}
