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

import com.taf.util.Consts;

/**
 * The InputInformationDialog is the base class for TAF dialogs. It contains a
 * text field and a confirm button to usually input a name.
 *
 * @see JDialog
 *
 * @author Adrien Jakubiak
 */
public abstract class InputInformationDialog extends JDialog implements KeyListener, ActionListener {

	private static final long serialVersionUID = -2718172025186345523L;

	/** The ok button text. */
	private static final String OK_BUTTON_TEXT = "Add";

	/** The ok button. */
	private JButton okButton;

	/** The last row if the added component. */
	private int lastRow;

	/** The last column of the added component. */
	private int lastColumn;

	/**
	 * Instantiates a new input information dialog.
	 */
	public InputInformationDialog() {
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setLayout(new GridBagLayout());
		this.addKeyListener(this);
		okButton = new JButton(OK_BUTTON_TEXT);
		okButton.addActionListener(this);
		lastRow = 0;
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
