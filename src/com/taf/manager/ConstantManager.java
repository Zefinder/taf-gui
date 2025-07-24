package com.taf.manager;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class ConstantManager extends Manager {

	private static final ConstantManager instance = new ConstantManager();

	public static final DecimalFormat REAL_FORMATTER = new DecimalFormat("0.###",
			DecimalFormatSymbols.getInstance(Locale.US));

	public static final String TAF_FILE_EXTENSION = ".taf";
	public static final String XML_FILE_EXTENSION = ".xml";
	
	public static final String PARAMETER_ENTITY_NAME = "parameter";
	public static final String TYPE_ENTITY_NAME = "type";
	public static final String NODE_ENTITY_NAME = "node";
	public static final String CONSTRAINT_ENTITY_NAME = "constraint";
	
	public static final String PARAMETER_NAME_LABEL_TEXT = "Parameter name";
	public static final String PARAMETER_TYPE_LABEL_TEXT = "Parameter type";
	public static final String NODE_TYPE_LABEL_TEXT = "Node type";
	public static final String ROOT_NAME_LABEL_TEXT = "Root name";
	public static final String TYPE_NAME_LABEL_TEXT = "Type name";
	public static final String NODE_NAME_LABEL_TEXT = "Node name";
	public static final String CONSTRAINT_NAME_LABEL_TEXT = "Constraint name";
	
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
	public static final int DEFAULT_DEPTH_NUMBER = 1;
	public static final int DEFAULT_MIN_INSTANCE_NUMBER = 1;
	public static final int DEFAULT_MAX_INSTANCE_NUMBER = 1;
	
	public static final int SMALL_INSET_GAP = 5;
	public static final int MEDIUM_INSET_GAP = 10;
	public static final int LARGE_INSET_GAP = 15;
	public static final int HUGE_INSET_GAP = 20;
	public static final int XXL_INSET_GAP = 50;
	
	public static final String MIN_TEXT = "Min";
	public static final String MAX_TEXT = "Max";
	
	public static final String FALSE_VALUE = "False";
	public static final String TRUE_VALUE = "True";
	
	private static final String RANGE_PATTERN_STRING = "\\[[\s]*([^\"]+),[\s]*([^\"]+)\\]";
	public static final Pattern RANGE_PATTERN = Pattern.compile(RANGE_PATTERN_STRING); 
	
	public static final Color CONSOLE_BACKGROUND_COLOR = new Color(0xC8C8C8);
	public static final Color CONSOLE_FOREGROUND_COLOR = new Color(0x383838);
	public static final Color BLACK_COLOR = new Color(0x000000);
	public static final Color RED_COLOR = new Color(0xC91E1E);
	public static final Color GREEN_COLOR = new Color(0x32A81B);
	public static final Color YELLOW_COLOR = new Color(0x899114);
	public static final Color BLUE_COLOR = new Color(0x244BB5);
	public static final Color MAGENTA_COLOR = new Color(0x8C1B96);
	public static final Color CYAN_COLOR = new Color(0x309DB0);
	public static final Color WHITE_COLOR = new Color(0xFFFFFF);	
	

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
