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

import com.taf.annotation.Nullable;
import com.taf.exception.EntityCreationException;
import com.taf.logic.constraint.Constraint;
import com.taf.util.Consts;

/**
 * The ConstraintCreationDialog is shown when the user asked for a new
 * constraint. It asks for a constraint name only.
 *
 * @see InputInformationDialog
 * @see Constraint
 *
 * @author Adrien Jakubiak
 */
public class ConstraintCreationDialog extends InputInformationDialog {

	private static final long serialVersionUID = 1054772104893408576L;

	/** The dialog title. */
	private static final String DIALOG_TITLE = "Create a new constraint";

	/** The created constraint. */
	private Constraint createdConstraint;

	/**
	 * Instantiates a new constraint creation dialog.
	 */
	public ConstraintCreationDialog() {
		super(Consts.CONSTRAINT_NAME_LABEL_TEXT);
		this.setTitle(DIALOG_TITLE);
	}

	/**
	 * Returns the constraint.
	 *
	 * @return the constraint
	 */
	@Nullable
	public Constraint getConstraint() {
		return createdConstraint;
	}

	@Override
	public void initDialog() {
		createdConstraint = null;
		super.initDialog();
	}

	@Override
	protected void performAction() {
		String name = getFieldNameText();
		if (name != null && !name.isBlank()) {
			try {
				createdConstraint = new Constraint(name.strip());
			} catch (EntityCreationException e) {
				// This cannot happen, ignore
			}
			dispose();
		}
	}

}
