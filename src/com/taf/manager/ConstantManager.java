package com.taf.manager;

import java.awt.GridBagConstraints;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.swing.JOptionPane;

public class ConstantManager extends Manager {

	private static final ConstantManager instance = new ConstantManager();

	public static final DecimalFormat REAL_FORMATTER = new DecimalFormat("0.0##",
			DecimalFormatSymbols.getInstance(Locale.US));

	public static final String TAF_FILE_EXTENSION = ".taf";
	
	public static final String PARAMETER_ENTITY_NAME = "parameter";
	public static final String NODE_ENTITY_NAME = "node";
	public static final String CONSTRAINT_ENTITY_NAME = "constraint";
	
	public static final String PARAMETER_NAME_LABEL_TEXT = "Parameter name";
	public static final String PARAMETER_TYPE_LABEL_TEXT = "Parameter type";
	public static final String NODE_NAME_LABEL_TEXT = "Node name";
	
	public static final String PARAMETER_STRING_FORMAT = "%s=\"%s\"";
	public static final String FIELD_STRING_FORMAT = "name=\"%s\" %s";

	public static final String TAB = "\t";
	public static final String LINE_JUMP = "\n";

	public static final String HASH_SEPARATOR = ":";
	public static final String ELEMENT_SEPARATOR = ";";
	public static final String PARAMETER_SEPARATOR = " ";
	
	public static final String DEFAULT_ROOT_NAME = "test_cases";
	
	public static final String ERROR_DIALOG_TITLE = "Error!";
	
	public static final int JTEXT_FIELD_DEFAULT_COLUMN = 20;
	public static final String JFORMATTED_TEXT_FIELD_VALUE_PROPERTY = "value";
	
	public static final int DEFAULT_WEIGHT_VALUE = 1;
	
	public static final int DEFAULT_MIN_VALUE = 0;
	public static final int DEFAULT_MAX_VALUE = 10;
	
	public static final int DEFAULT_INSTANCE_NUMBER = 1;
	public static final int DEFAULT_MIN_INSTANCE_NUMBER = 1;
	public static final int DEFAULT_MAX_INSTANCE_NUMBER = 1;
	
	public static final String MIN_TEXT = "min";
	public static final String MAX_TEXT = "max";

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
	
	public static final void showError(String errorMessage) {
		JOptionPane.showMessageDialog(null, errorMessage, ERROR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
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
