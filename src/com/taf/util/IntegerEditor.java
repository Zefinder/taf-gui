package com.taf.util;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

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

/*
 * This is a modified version of the IntegerEditor of the Java tutorial of JTables
 */

public class IntegerEditor extends DefaultCellEditor {

	private static final long serialVersionUID = -6290272775924941609L;

	private JFormattedTextField integerField;
	private NumberFormat integerFormat;

	public IntegerEditor() {
		super(new JFormattedTextField());
		integerField = (JFormattedTextField) getComponent();

		// Set up the editor for the integer cells.
		NumberFormat integerFormat = NumberFormat.getIntegerInstance();
		NumberFormatter intFormatter = new NumberFormatter(integerFormat);
		intFormatter.setFormat(integerFormat);
		integerField.setFormatterFactory(new DefaultFormatterFactory(intFormatter));

		// Text at the beginning when editing and set persistent (react to tabs too)
		integerField.setHorizontalAlignment(JTextField.LEADING);
		integerField.setFocusLostBehavior(JFormattedTextField.PERSIST);

		// Set action when enter (and focus lost in general)
		integerField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "check");
		integerField.getActionMap().put("check", new AbstractAction() {
			private static final long serialVersionUID = -7031956174719188660L;

			public void actionPerformed(ActionEvent e) {
				checkFormatedField();
			}
		});
	}

	// Override to invoke setValue on the formatted text field.
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//		JFormattedTextField ftf = (JFormattedTextField) super.getTableCellEditorComponent(table, value, isSelected, row,
//				column);
		integerField.setValue(value);
		return integerField;
	}

	@Override
	public Object getCellEditorValue() {
//		JFormattedTextField ftf = (JFormattedTextField) getComponent();
		Object o = integerField.getValue();
//		System.out.println(o);
		if (o instanceof Integer) {
			return o;
		} else if (o instanceof Number) {
			return ((Number) o).intValue();
		} else {
			try {
				return integerFormat.parseObject(o.toString());
			} catch (ParseException exc) {
				// Should never go here if initialized correctly...
				System.err.println("getCellEditorValue: can't parse o: " + o);
				return null;
			}
		}
	}

	private boolean checkFormatedField() {
		if (!integerField.isEditValid()) {
			// TODO JOptionPane message to say invalid input
			integerField.setValue(integerField.getValue());

		} else {
			try {
				integerField.commitEdit();
			} catch (ParseException exc) {
				// Should never go here
			}
		}

		return super.stopCellEditing();
	}

	@Override
	public boolean stopCellEditing() {
		return checkFormatedField();
	}
}