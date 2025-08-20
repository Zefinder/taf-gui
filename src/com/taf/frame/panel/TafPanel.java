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
package com.taf.frame.panel;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.taf.event.EventListener;
import com.taf.frame.TafFrame;
import com.taf.logic.field.Root;
import com.taf.manager.EventManager;

/**
 * The TafPanel is used in the {@link TafFrame} and contains the
 * {@link FieldTreePanel} and the {@link PropertyPanel}, which are important
 * elements to edit a TAF project.
 * 
 * @see JPanel
 * @see TafFrame
 * @see FieldTreePanel
 * @see PropertyPanel
 * 
 * @author Adrien Jakubiak
 */
public class TafPanel extends JPanel implements EventListener {

	private static final long serialVersionUID = -7098545217788512796L;

	/** The fields panel. */
	private FieldTreePanel fieldsPanel;

	/** The property panel. */
	private PropertyPanel propertyPanel;

	/**
	 * Instantiates a new taf panel.
	 *
	 * @param root the root
	 */
	public TafPanel(Root root) {
		this.setLayout(new BorderLayout());
		fieldsPanel = new FieldTreePanel(root);
		propertyPanel = new PropertyPanel();
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, fieldsPanel, propertyPanel);
		splitPane.setResizeWeight(.15);

		this.add(splitPane);
	}

	@Override
	public void unregisterComponents() {
		EventManager.getInstance().unregisterEventListener(fieldsPanel);
		EventManager.getInstance().unregisterEventListener(propertyPanel);
	}
}
