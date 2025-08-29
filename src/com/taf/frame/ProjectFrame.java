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

import javax.swing.JFrame;

import com.taf.annotation.EventMethod;
import com.taf.event.EventListener;
import com.taf.event.ProjectClosedEvent;
import com.taf.event.ProjectRunClosedEvent;
import com.taf.event.ProjectRunOpenedEvent;
import com.taf.frame.menubar.TafProjectMenuBar;
import com.taf.frame.panel.TafPanel;
import com.taf.logic.field.Root;
import com.taf.manager.EventManager;

/**
 * The ProjectFrame is the frame shown when opening a project from the
 * {@link MainMenuFrame}. It allows project edition, save and entity
 * modification.
 * 
 * @see TafFrame
 * @see TafPanel
 * @see TafProjectMenuBar
 *
 * @author Adrien Jakubiak
 */
public class ProjectFrame extends TafFrame implements EventListener {

	private static final long serialVersionUID = 1724446330461662942L;

	/** The frame name. */
	private static final String FRAME_NAME = "TAF GUI";

	/** The TAF panel. */
	private final TafPanel tafPanel;
	// TODO Add verification to check if saved before quit !

	/**
	 * Instantiates a new project frame.
	 *
	 * @param root the root
	 */
	public ProjectFrame(Root root) {
		super(FRAME_NAME);
		this.setSize(650, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		this.setJMenuBar(new TafProjectMenuBar());

		tafPanel = new TafPanel(root);
		this.setLayout(new BorderLayout());
		this.add(tafPanel);

		EventManager.getInstance().registerEventListener(this);

		this.setVisible(false);
	}

	/**
	 * Handler for {@link ProjectClosedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onProjectClosed(ProjectClosedEvent event) {
		EventManager.getInstance().unregisterEventListener(this);
		this.dispose();
	}

	/**
	 * Handler for {@link ProjectRunOpenedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onProjectStartRun(ProjectRunOpenedEvent event) {
		this.setVisible(false);
	}

	/**
	 * Handler for {@link ProjectRunClosedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onProjectStopRun(ProjectRunClosedEvent event) {
		this.setVisible(true);
		this.repaint();
	}

	@Override
	public void unregisterComponents() {
		EventManager.getInstance().unregisterEventListener(tafPanel);
	}

}
