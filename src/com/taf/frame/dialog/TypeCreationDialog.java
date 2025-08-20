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
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.taf.util.Consts;

/**
 * The TypeCreationDialog is shown when the user asked for a new type. It asks
 * for a type name only.
 *
 * @see InputInformationDialog
 * @see Type
 *
 * @author Adrien Jakubiak
 */
public class TypeCreationDialog extends InputInformationDialog {

	private static final long serialVersionUID = 8094717940445682259L;

	/** The dialog title. */
	private static final String DIALOG_TITLE = "Create a new type";

	/** The field name. */
	private final JTextField fieldName;

	/** The created type. */
	private com.taf.logic.field.Type createdType;

	/**
	 * Instantiates a new type creation dialog.
	 */
	public TypeCreationDialog() {
		this.setTitle(DIALOG_TITLE);

		// TODO Put in InputInformationDialog as they all use it?
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(Consts.HUGE_INSET_GAP, Consts.LARGE_INSET_GAP, Consts.SMALL_INSET_GAP,
				Consts.SMALL_INSET_GAP);
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		JLabel fieldLabel = new JLabel(Consts.TYPE_NAME_LABEL_TEXT);
		addComponent(fieldLabel, c);

		c.insets = new Insets(Consts.HUGE_INSET_GAP, Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP,
				Consts.LARGE_INSET_GAP);
		c.gridx = 1;
		fieldName = new JTextField(Consts.JTEXT_FIELD_DEFAULT_COLUMN);
		addComponent(fieldName, c);
	}

	/**
	 * Returns the created type.
	 *
	 * @return the created type
	 */
	// TODO Replace by optional
	public com.taf.logic.field.Type getField() {
		return createdType;
	}

	@Override
	public void initDialog() {
		createdType = null;
		super.initDialog();
	}

	@Override
	protected void performAction() {
		String name = fieldName.getText();
		if (!name.isBlank()) {
			createdType = new com.taf.logic.field.Type(name);
			dispose();
		}
	}

}
