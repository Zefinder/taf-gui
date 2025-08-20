/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.taf.util;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;


/**
 * <p>
 * The DoubleEditor is a {@link JTable} cell editor that is used to verify,
 * format and store double values.
 * </p>
 *
 * <p>
 * This is a modified version of the IntegerEditor of the Java tutorial of
 * JTables
 * </p>
 * 
 * @author Adrien Jakubiak
 */
public class DoubleEditor extends DefaultCellEditor {

	private static final long serialVersionUID = -6290272775924941609L;
	private static final String CHECK_ACTION = "check";
	
	private static final String UNKNWON_VALUE_ERROR_MESSAGE = "getCellEditorValue: can't parse o: ";

	/** The double field. */
	private JFormattedTextField doubleField;
	
	/** The double format. */
	private NumberFormat doubleFormat;

	/**
	 * Instantiates a new double editor.
	 */
	public DoubleEditor() {
		super(new JFormattedTextField());
		doubleField = (JFormattedTextField) getComponent();

		// Set up the editor for the integer cells.
		doubleFormat = DecimalFormat.getInstance(Locale.US);
		NumberFormatter doubleFormatter = new NumberFormatter(doubleFormat);
		doubleFormatter.setFormat(doubleFormat);
		doubleField.setFormatterFactory(new DefaultFormatterFactory(doubleFormatter));

		// Text at the beginning when editing and set persistent (react to tabs too)
		doubleField.setHorizontalAlignment(JTextField.LEADING);
		doubleField.setFocusLostBehavior(JFormattedTextField.PERSIST);

		// Set action when enter (and focus lost in general)
		doubleField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), CHECK_ACTION);
		doubleField.getActionMap().put(CHECK_ACTION, new AbstractAction() {
			private static final long serialVersionUID = -7031956174719188660L;

			@Override
			public void actionPerformed(ActionEvent e) {
				checkFormatedField();
			}
		});
	}

	@Override
	public Object getCellEditorValue() {
		Object o = doubleField.getValue();
		if (o instanceof Integer) {
			return o;
		} else if (o instanceof Number) {
			return ((Number) o).doubleValue();
		} else {
			try {
				return doubleFormat.parseObject(o.toString());
			} catch (ParseException exc) {
				// Should never go here if initialized correctly...
				System.err.println(UNKNWON_VALUE_ERROR_MESSAGE + o);
				return null;
			}
		}
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		doubleField.setValue(value);
		return doubleField;
	}

	@Override
	public boolean stopCellEditing() {
		return checkFormatedField();
	}

	/**
	 * Check if the formatted field contains a valid value.
	 *
	 * @return true if the field contains a valid value
	 */
	private boolean checkFormatedField() {
		if (!doubleField.isEditValid()) {
			// TODO JOptionPane message to say invalid input
			doubleField.setValue(doubleField.getValue());

		} else {
			try {
				doubleField.commitEdit();
			} catch (ParseException exc) {
				// Should never go here
			}
		}

		return super.stopCellEditing();
	}
}