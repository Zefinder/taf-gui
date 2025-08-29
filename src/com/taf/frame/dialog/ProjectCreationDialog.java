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

import com.taf.util.Consts;

/**
 * The ProjectCreationDialog is shown when the user wants to create a new
 * project.
 *
 * @see InputInformationDialog
 *
 * @author Adrien Jakubiak
 */
public class ProjectCreationDialog extends InputInformationDialog {

	private static final long serialVersionUID = -8365643430677886812L;

	/** The dialog title. */
	private static final String DIALOG_TITLE = "Create a new project";

	/** The project label text. */
	private static final String PROJECT_LABEL_TEXT = "Project name";

	/** The project name. */
	private String projectName;

	/**
	 * Instantiates a new project creation dialog.
	 */
	public ProjectCreationDialog() {
		super(PROJECT_LABEL_TEXT);
		this.setTitle(DIALOG_TITLE);
	}

	/**
	 * Returns the project name.
	 *
	 * @return the project name
	 */
	public String getProjectName() {
		return projectName;
	}

	@Override
	public void initDialog() {
		projectName = null;
		super.initDialog();
	}

	@Override
	protected void performAction() {
		String name = getFieldNameText();
		if (!name.isBlank()) {
			// Check if ends with .taf, adds it otherwise
			if (!name.endsWith(Consts.TAF_FILE_EXTENSION)) {
				name += Consts.TAF_FILE_EXTENSION;
			}

			projectName = name;
			dispose();
		}
	}

}
