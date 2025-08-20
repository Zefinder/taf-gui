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
package com.taf.frame.panel.secondary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.lang.reflect.Parameter;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.taf.event.EventListener;
import com.taf.frame.panel.PropertyPanel;
import com.taf.logic.type.FieldType;
import com.taf.logic.type.IntegerType;
import com.taf.manager.EventManager;

/**
 * <p>
 * The EntitySecondaryPropertyPanel (called secondary property panel) is used in
 * the {@link PropertyPanel} and manages the informations stored by the entity
 * type. For example, for a {@link Parameter} with an {@link IntegerType}, the
 * secondary panel will store the minimum and maximum value of the parameter and
 * its distribution (and the distribution parameters).
 * </p>
 * 
 * <p>
 * Implementation-wise, the secondary property panel uses a
 * {@link GridBagLayout} to store its components.
 * </p>
 * 
 * <p>
 * Secondary property panels are created using the
 * {@link EntitySecondaryPanelFactory} and must have a constructor with one
 * argument, which must be a {@link FieldType}.
 * </p>
 *
 * @author Adrien Jakubiak
 */
public abstract class EntitySecondaryPropertyPanel extends JPanel implements EventListener {

	private static final long serialVersionUID = 3797269772034697720L;

	/**
	 * Instantiates a new entity secondary property panel.
	 */
	public EntitySecondaryPropertyPanel() {
		this.setLayout(new GridBagLayout());
		EventManager.getInstance().registerEventListener(this);
	}

	/**
	 * Adds the component to the panel.
	 *
	 * @param component the component
	 * @param c         the c
	 */
	protected void addComponent(JComponent component, GridBagConstraints c) {
		this.add(component, c);
	}

	@Override
	public void unregisterComponents() {
		// Nothing to unregister
	}

}
