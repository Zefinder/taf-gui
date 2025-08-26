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
package com.taf.frame;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.taf.event.Event;
import com.taf.event.ProjectRunAbortedEvent;
import com.taf.event.ProjectRunClosedEvent;
import com.taf.frame.menubar.RunMenuBar;
import com.taf.frame.panel.RunPanel;
import com.taf.manager.EventManager;

/**
 * The RunFrame is the frame shown when a project is run. It allows TAF
 * generation.
 * 
 * @see TafFrame
 * @see RunPanel
 * @see RunMenuBar
 *
 * @author Adrien Jakubiak
 */
public class RunFrame extends TafFrame {

	private static final long serialVersionUID = -2445180809943024092L;

	/** The frame name. */
	private static final String FRAME_NAME = "Run TAF";

	/** The run panel. */
	private RunPanel runPanel;

	/**
	 * Instantiates a new run frame.
	 */
	public RunFrame() {
		super(FRAME_NAME);
		this.setSize(1000, 800);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		this.setJMenuBar(new RunMenuBar());

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// Send the event to abort the process if it exists
				Event event = new ProjectRunAbortedEvent();
				EventManager.getInstance().fireEvent(event);

				// Send the event to notify that the frame is closed
				event = new ProjectRunClosedEvent();
				EventManager.getInstance().fireEvent(event);
				runPanel.unregisterConsolePanel();
			}
		});

		this.setLayout(new BorderLayout());
		runPanel = new RunPanel();
		this.add(runPanel);

		this.setVisible(false);
	}

}
