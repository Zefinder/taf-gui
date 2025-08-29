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
package com.taf.frame.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.taf.util.Consts;

/**
 * <p>
 * The InputInformationDialog is the base class for TAF dialogs. It contains a
 * text field and a confirm button to usually input a name.
 * </p>
 *
 * <p>
 * This class gives methods to add other things than just a field and a button.
 * The dialog layout is a {@link GridBagLayout} and the constraint
 * <code>c</code> is accessible as a protected field. To access the field text,
 * use {@link #getFieldNameText()}. To add a component, use
 * {@link #addComponent(JComponent, GridBagConstraints)}. It will remember the
 * position of the last <code>gridy</code> and will position the button at the
 * right place. Note that <code>c</code> is already ready for the next row, you
 * should not have to modify it.
 * </p>
 *
 * @see JDialog
 *
 * @author Adrien Jakubiak
 */
public abstract class InputInformationDialog extends JDialog implements KeyListener, ActionListener {

	private static final long serialVersionUID = -2718172025186345523L;

	/** The default GridBagConstraints. */
	protected GridBagConstraints c;

	/** The field name. */
	private JTextField fieldName;

	/** The ok button text. */
	private static final String OK_BUTTON_TEXT = "Add";

	/** The ok button. */
	private JButton okButton;

	/** The last row if the added component. */
	private int lastRow;

	/** The last column of the added component. */
	private int lastColumn;

	/**
	 * Instantiates a new input information dialog.code
	 */
	public InputInformationDialog(String fieldLabelText) {
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setLayout(new GridBagLayout());
		this.addKeyListener(this);
		c = Consts.getDefaultConstraint();
		c.insets = new Insets(Consts.HUGE_INSET_GAP, Consts.LARGE_INSET_GAP, Consts.SMALL_INSET_GAP,
				Consts.SMALL_INSET_GAP);
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		JLabel fieldLabel = new JLabel(fieldLabelText);
		addComponent(fieldLabel, c);

		c.insets = new Insets(Consts.HUGE_INSET_GAP, Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP,
				Consts.LARGE_INSET_GAP);
		c.gridx = 1;
		fieldName = new JTextField(Consts.JTEXT_FIELD_DEFAULT_COLUMN);
		addComponent(fieldName, c);
		
		c.gridx = 0;
		c.gridy = 1;

		okButton = new JButton(OK_BUTTON_TEXT);
		okButton.addActionListener(this);
		lastRow = 1;
		lastColumn = 0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		performAction();
	}

	/**
	 * Initializes the dialog.
	 */
	public void initDialog() {
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(Consts.MEDIUM_INSET_GAP, 0, Consts.HUGE_INSET_GAP, 0);
		c.fill = GridBagConstraints.NONE;
		c.gridheight = 1;
		c.gridwidth = lastColumn;
		c.gridx = 0;
		c.gridy = lastRow;
		this.add(okButton, c);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	/**
	 * Returns the field name text.
	 *
	 * @return the field name text
	 */
	protected String getFieldNameText() {
		return fieldName.getText();
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		switch (e.getKeyChar()) {
		case KeyEvent.VK_ENTER:
			performAction();
			break;

		default:
			break;
		}
	}

	/**
	 * Adds a component to the dialog.
	 *
	 * @param component the component
	 * @param c         the c
	 */
	protected void addComponent(JComponent component, GridBagConstraints c) {
		this.add(component, c);
		component.addKeyListener(this);
		if (c.gridy + c.gridheight > lastRow) {
			lastRow = c.gridy + c.gridheight;
		}

		if (c.gridx + c.gridwidth > lastColumn) {
			lastColumn = c.gridx + c.gridwidth;
		}
	}

	/**
	 * Performs an action when clicking the ok button.
	 */
	protected abstract void performAction();
}
