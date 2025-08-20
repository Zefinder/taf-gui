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
package com.taf.frame.panel.primary;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;

import com.taf.logic.constraint.Constraint;
import com.taf.util.Consts;

/**
 * The TypePropertyPanel is a primary property panel that represent a
 * constraint. It allows the user to edit the constraint name.
 * 
 * @see EntityPrimaryPropertyPanel
 * 
 * @author Adrien Jakubiak
 */
public class ConstraintPrimaryPropertyPanel extends EntityPrimaryPropertyPanel {

	private static final long serialVersionUID = 1744588782255512565L;

	/**
	 * Instantiates a new constraint primary property panel.
	 *
	 * @param constraint the constraint
	 */
	public ConstraintPrimaryPropertyPanel(Constraint constraint) {
		super(constraint);

		GridBagConstraints c = Consts.getDefaultConstraint();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP);
		c.weightx = 0;
		c.weighty = 0;
		JLabel constraintLabel = new JLabel(Consts.CONSTRAINT_NAME_LABEL_TEXT);
		this.add(constraintLabel, c);

		c.insets = new Insets(0, Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		this.add(entityName, c);
	}

}
