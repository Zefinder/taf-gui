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

import com.taf.frame.RunFrame;
import com.taf.frame.panel.run.TafConsolePanel;
import com.taf.frame.panel.run.TafRunComponentPanel;
import com.taf.manager.EventManager;

/**
 * The RunPanel is used in the {@link RunFrame}, and contains the
 * {@link TafRunComponentPanel} and the {@link TafConsolePanel}, which are
 * important elements to configure the TAF generation.
 * 
 * @see JPanel
 * @see RunFrame
 * @see TafRunComponentPanel
 * @see TafConsolePanel
 * 
 * @author Adrien Jakubiak
 */
public class RunPanel extends JPanel {

	private static final long serialVersionUID = -1299815982147468944L;

	/** The console panel. */
	private TafConsolePanel consolePanel;

	/**
	 * Instantiates a new run panel.
	 */
	public RunPanel() {
		this.setLayout(new BorderLayout());
		TafRunComponentPanel componentPanel = new TafRunComponentPanel();
		consolePanel = new TafConsolePanel();
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, componentPanel, consolePanel);

		this.add(splitPane);
	}

	/**
	 * Unregisters the console panel.
	 */
	public void unregisterConsolePanel() {
		EventManager.getInstance().unregisterEventListener(consolePanel);
	}

}
