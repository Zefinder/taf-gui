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

import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import com.taf.annotation.Nullable;
import com.taf.exception.EntityCreationException;
import com.taf.logic.field.Parameter;
import com.taf.logic.type.FieldType;
import com.taf.logic.type.ParameterTypeFactory;
import com.taf.util.Consts;

/**
 * The ParameterCreationDialog is shown when the user asked for a new parameter.
 * It asks for a parameter name and a parameter type.
 *
 * @see InputInformationDialog
 * @see Parameter
 * @see FieldType
 *
 * @author Adrien Jakubiak
 */
public class ParameterCreationDialog extends InputInformationDialog {

	private static final long serialVersionUID = 33492308978388388L;

	/** The dialog title. */
	private static final String DIALOG_TITLE = "Create a new parameter";

	/** The type names combo box. */
	private final JComboBox<String> typeNames;

	/** The created parameter. */
	private Parameter createdParameter;

	/**
	 * Instantiates a new parameter creation dialog.
	 */
	public ParameterCreationDialog() {
		super(Consts.PARAMETER_NAME_LABEL_TEXT);
		this.setTitle(DIALOG_TITLE);

		c.insets = new Insets(5, Consts.LARGE_INSET_GAP, Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP);
		JLabel typeNameLabel = new JLabel(Consts.PARAMETER_TYPE_LABEL_TEXT);
		addComponent(typeNameLabel, c);

		c.insets = new Insets(Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP,
				Consts.LARGE_INSET_GAP);
		c.gridx = 1;
		typeNames = new JComboBox<String>(FieldType.PARAMETER_TYPE_SET.toArray(String[]::new));
		addComponent(typeNames, c);
	}

	/**
	 * Returns the created parameter.
	 *
	 * @return the created parameter
	 */
	@Nullable
	public Parameter getField() {
		return createdParameter;
	}

	@Override
	public void initDialog() {
		createdParameter = null;
		super.initDialog();
	}

	@Override
	protected void performAction() {
		String name = getFieldNameText();
		if (!name.isBlank()) {
			String typeName = (String) typeNames.getSelectedItem();
			try {
				com.taf.logic.type.FieldType type = ParameterTypeFactory.createFieldType(typeName);
				createdParameter = new Parameter(name, type);
			} catch (EntityCreationException e) {
				// This cannot happen, ignore
			}
			dispose();
		}
	}

}
