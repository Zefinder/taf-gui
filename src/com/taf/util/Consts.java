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

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.taf.logic.type.parameter.DistributionType;

/**
 * This class gather constants that are used by multiple classes and that are
 * available to all.
 *
 * @author Adrien Jakubiak
 */
public class Consts {

	/** A double formatter. */
	public static final DecimalFormat REAL_FORMATTER = new DecimalFormat("0.###",
			DecimalFormatSymbols.getInstance(Locale.US));

	/*
	 * ------------------
	 * --- EXTENSIONS ---
	 * ------------------
	 */
	
	/** The TAF file extension. */
	public static final String TAF_FILE_EXTENSION = ".taf";

	/** The XML file extension. */
	public static final String XML_FILE_EXTENSION = ".xml";

	/*
	 * --------------------
	 * --- ENTITY NAMES ---
	 * --------------------
	 */
	
	/** The parameter type name displayed in the entity tree. */
	public static final String PARAMETER_ENTITY_NAME = "parameter";

	/** The type type name displayed in the entity tree. */
	public static final String TYPE_ENTITY_NAME = "type";

	/** The node type name displayed in the entity tree. */
	public static final String NODE_ENTITY_NAME = "node";

	/** The constraint type name displayed in the entity tree. */
	public static final String CONSTRAINT_ENTITY_NAME = "constraint";

	/*
	 * --------------------
	 * --- DISPLAY TEXT ---
	 * --------------------
	 */
	
	/** The text displayed to ask for parameter name in the parameter creation dialog, and to display the parameter name. */
	public static final String PARAMETER_NAME_LABEL_TEXT = "Parameter name";

	/** The text displayed to ask for parameter type in the parameter creation dialog, and to display the parameter type. */
	public static final String PARAMETER_TYPE_LABEL_TEXT = "Parameter type";

	/** The text displayed to ask for node name in the node creation dialog, and to display the node type. */
	public static final String NODE_NAME_LABEL_TEXT = "Node name";
	
	/** The text displayed to show the node type combo box. */
	public static final String NODE_TYPE_LABEL_TEXT = "Node type";

	/** The text displayed to ask for root name in the root creation dialog, and to display the root name. */
	public static final String ROOT_NAME_LABEL_TEXT = "Root name";

	/** The text displayed to ask for type name in the type creation dialog, and to display the type name. */
	public static final String TYPE_NAME_LABEL_TEXT = "Type name";

	/** The text displayed to ask for constraint name in the constraint creation dialog, and to display the constraint name. */
	public static final String CONSTRAINT_NAME_LABEL_TEXT = "Constraint name";
	
	/** The text displayed to show the minimum value. */
	public static final String MIN_TEXT = "Min";

	/** The text displayed to show the maximum value. */
	public static final String MAX_TEXT = "Max";

	/** The false value text. */
	public static final String FALSE_VALUE = "False";

	/** The true value text. */
	public static final String TRUE_VALUE = "True";
	
	/** The error title for a {@link JOptionPane} dialog. */
	public static final String ERROR_DIALOG_TITLE = "Error!";

	/*
	 * ---------------------
	 * --- ENTITY FORMAT ---
	 * ---------------------
	 */

	/** The parameter format to print its name and value. */
	public static final String PARAMETER_STRING_FORMAT = "%s=\"%s\"";

	/** The field string format to print its name and parameters. */
	public static final String FIELD_STRING_FORMAT = "name=\"%s\" %s";

	/*
	 * ------------------
	 * --- CHARACTERS ---
	 * ------------------
	 */
	
	/** The tabulation constant. */
	public static final String TAB = "\t";

	/** The line jump constant. */
	public static final String LINE_JUMP = "\n";

	/** The character used for hash separation. */
	public static final String HASH_SEPARATOR = ":";

	/** The character used for element separation. */
	public static final String ELEMENT_SEPARATOR = ";";

	/** The character used for parameter separation. */
	public static final String PARAMETER_SEPARATOR = " ";

	/*
	 * ------------------
	 * --- PROPERTIES ---
	 * ------------------
	 */

	/** The default number of columns in a {@link JTextField}. */
	public static final int JTEXT_FIELD_DEFAULT_COLUMN = 20;

	/** The value property name in a formatted text field to associate to a handler. */
	public static final String JFORMATTED_TEXT_FIELD_VALUE_PROPERTY = "value";
	
	/*
	 * ----------------------
	 * --- DEFAULT VALUES ---
	 * ----------------------
	 */

	/** The default name for the root node. */
	public static final String DEFAULT_ROOT_NAME = "test_cases";
	
	/** The default weight value. */
	public static final int DEFAULT_WEIGHT_VALUE = 1;

	/** The default minimum value. */
	public static final int DEFAULT_MIN_VALUE = 0;

	/** The default maximum value. */
	public static final int DEFAULT_MAX_VALUE = 10;

	/** The default distribution. */
	public static final DistributionType DEFAULT_DISTRIBUTION = DistributionType.UNIFORM;

	/** The default mean value. */
	public static final int DEFAULT_MEAN_VALUE = 0;

	/** The default variance value. */
	public static final int DEFAULT_VARIANCE_VALUE = 0;

	/** The default instance number. */
	public static final int DEFAULT_INSTANCE_NUMBER = 1;

	/** The default depth number. */
	public static final int DEFAULT_DEPTH_NUMBER = 1;

	/** The default minimum instance number. */
	public static final int DEFAULT_MIN_INSTANCE_NUMBER = 1;

	/** The default maximum instance number. */
	public static final int DEFAULT_MAX_INSTANCE_NUMBER = 1;

	/** The default minimum depth number. */
	public static final int DEFAULT_MIN_DEPTH_NUMBER = 1;

	/** The default maximum depth number. */
	public static final int DEFAULT_MAX_DEPTH_NUMBER = 1;
	
	/*
	 * --------------
	 * --- INSETS ---
	 * --------------
	 */

	/** A small inset gap for {@link GridBagConstraints}. */
	public static final int SMALL_INSET_GAP = 5;

	/** A medium inset gap for {@link GridBagConstraints}. */
	public static final int MEDIUM_INSET_GAP = 10;

	/** A large inset gap for {@link GridBagConstraints}. */
	public static final int LARGE_INSET_GAP = 15;

	/** A huge inset gap for {@link GridBagConstraints}. */
	public static final int HUGE_INSET_GAP = 20;

	/** A huge huge inset gap for {@link GridBagConstraints}. */
	public static final int XXL_INSET_GAP = 50;
	
	/*
	 * ----------------------
	 * --- STRING PATTERN ---
	 * ----------------------
	 */

	/** The string pattern to represent a range. */
	private static final String RANGE_PATTERN_STRING = "\\[[\s]*([^\"]+),[\s]*([^\"]+)\\]";

	/** The pattern object of a range. */
	public static final Pattern RANGE_PATTERN = Pattern.compile(RANGE_PATTERN_STRING);
	
	/*
	 * ---------------------
	 * --- CONSOLE STYLE ---
	 * ---------------------
	 */

	/** The console background color. */
	public static final Color CONSOLE_BACKGROUND_COLOR = new Color(0xC8C8C8);

	/** The console foreground color. */
	public static final Color CONSOLE_FOREGROUND_COLOR = new Color(0x383838);

	/** The console black color. */
	public static final Color BLACK_COLOR = new Color(0x000000);

	/** The console red color. */
	public static final Color RED_COLOR = new Color(0xC91E1E);

	/** The console green color. */
	public static final Color GREEN_COLOR = new Color(0x32A81B);

	/** The console yellow color. */
	public static final Color YELLOW_COLOR = new Color(0x899114);

	/** The console blue color. */
	public static final Color BLUE_COLOR = new Color(0x244BB5);

	/** The console magenta color. */
	public static final Color MAGENTA_COLOR = new Color(0x8C1B96);

	/** The console cyan color. */
	public static final Color CYAN_COLOR = new Color(0x309DB0);

	/** The console white color. */
	public static final Color WHITE_COLOR = new Color(0xFFFFFF);

	/**
	 * Defines the default {@link GridBagConstraints} used in the UI.
	 *
	 * @return the default constraints
	 */
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

	/**
	 * Show an error to the user using a pop-up message by the {@link JOptionPane}
	 * class.
	 *
	 * @param errorMessage the error message to display
	 */
	public static final void showError(String errorMessage) {
		JOptionPane.showMessageDialog(null, errorMessage, ERROR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Format a parameter using the parameter format.
	 *
	 * @param name  the parameter name
	 * @param value the parameter value
	 * @return the formatted parameter
	 */
	public static final String formatParameter(String name, String value) {
		return PARAMETER_STRING_FORMAT.formatted(name, value);
	}

	private Consts() {
	}

}
