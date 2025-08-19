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
package com.taf.frame.popup;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.taf.event.ProjectToDeleteEvent;
import com.taf.event.ProjectToImportEvent;
import com.taf.event.ProjectToOpenEvent;
import com.taf.frame.panel.ProjectChooserPanel;
import com.taf.manager.EventManager;

/**
 * The ProjectPopupMenu is displayed when right-clicking on a project name in
 * the {@link ProjectChooserPanel}.
 *
 * @author Adrien Jakubiak
 */
public class ProjectPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 3680615409452198572L;

	/** The import item text. */
	private static final String IMPORT_ITEM_TEXT = "Import project";

	/** The open item text. */
	private static final String OPEN_ITEM_TEXT = "Open project";

	/** The delete item text. */
	private static final String DELETE_ITEM_TEXT = "Delete project";

	/**
	 * Instantiates a new project popup menu.
	 */
	public ProjectPopupMenu() {
		JMenuItem importItem = new JMenuItem(IMPORT_ITEM_TEXT);
		JMenuItem openItem = new JMenuItem(OPEN_ITEM_TEXT);
		JMenuItem deleteItem = new JMenuItem(DELETE_ITEM_TEXT);

		importItem.addActionListener(e -> importProject());
		openItem.addActionListener(e -> open());
		deleteItem.addActionListener(e -> delete());

		this.add(importItem);
		this.add(openItem);
		this.add(deleteItem);
	}

	/**
	 * Fires an event to notify that the user wants to import a project.
	 */
	private void importProject() {
		EventManager.getInstance().fireEvent(new ProjectToImportEvent());
	}

	/**
	 * Fires an event to notify that the user wants to open a project.
	 */
	private void open() {
		EventManager.getInstance().fireEvent(new ProjectToOpenEvent());
	}

	/**
	 * Fires an event to notify that the user wants to delete a project.
	 */
	private void delete() {
		EventManager.getInstance().fireEvent(new ProjectToDeleteEvent());
	}

}
