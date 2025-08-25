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
import java.lang.reflect.Parameter;

import com.taf.annotation.FactoryObject;
import com.taf.frame.panel.secondary.type.TypeAddEntityPanel;
import com.taf.logic.constraint.Constraint;
import com.taf.logic.field.Node;
import com.taf.logic.field.Type;
import com.taf.logic.type.FieldType;
import com.taf.util.Consts;

/**
 * The TypePropertyPanel is a secondary property panel used for the {@link Type}
 * node. It allows the user to add {@link Parameter}s, {@link Node}s, and
 * {@link Constraint}s.
 * 
 * @see EntitySecondaryPropertyPanel
 * 
 * @author Adrien Jakubiak
 */
@FactoryObject(types = FieldType.class, generate = true)
public class TypePropertyPanel extends EntitySecondaryPropertyPanel {

	private static final long serialVersionUID = 3296878396678633310L;

	/**
	 * Instantiates a new type property panel.
	 */
	public TypePropertyPanel() {
		GridBagConstraints c = Consts.getDefaultConstraint();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;
		this.add(new TypeAddEntityPanel(), c);
	}

}
