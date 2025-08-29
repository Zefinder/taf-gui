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

import com.taf.logic.constraint.Constraint;
import com.taf.logic.type.BooleanType;
import com.taf.logic.type.DefaultFieldType;
import com.taf.logic.type.FieldType;
import com.taf.logic.type.IntegerType;
import com.taf.logic.type.NodeType;
import com.taf.logic.type.RealType;
import com.taf.logic.type.StringType;

/**
 * <p>
 * The EntitySecondaryPanelFactory is used to create
 * {@link EntitySecondaryPropertyPanel} objects using their {@link FieldType}.
 * </p>
 * 
 * <p>
 * Classes implementing the {@link EntitySecondaryPropertyPanel} must have a
 * constructor with one argument having the type of {@link FieldType}.
 * </p>
 * 
 * @author Adrien Jakubiak
 */
public class EntitySecondaryPanelFactory {

	/**
	 * Creates a new EntitySecondaryPropertyPanel object for a constraint.
	 *
	 * @param constraint the constraint
	 * @return the entity secondary property panel
	 */
	public static EntitySecondaryPropertyPanel createConstraintPropertyPanel(Constraint constraint) {
		return new ConstraintPropertyPanel(constraint);
	}

	/**
	 * Creates a new EntitySecondaryPropertyPanel object.
	 *
	 * @param type the type
	 * @return the entity secondary property panel
	 */
	public static EntitySecondaryPropertyPanel createFieldPropertyPanel(FieldType type) {
		if (type instanceof DefaultFieldType) {
			return new TypePropertyPanel(type);
		}

		if (type instanceof NodeType) {
			return new NodePropertyPanel((NodeType) type);
		}

		if (type instanceof BooleanType) {
			return new BooleanPropertyPanel((BooleanType) type);
		}

		if (type instanceof IntegerType) {
			return new IntegerPropertyPanel((IntegerType) type);
		}

		if (type instanceof RealType) {
			return new RealPropertyPanel((RealType) type);
		}

		if (type instanceof StringType) {
			return new StringPropertyPanel((StringType) type);
		}

		return null;
	}

	/**
	 * Creates a new EntitySecondaryPropertyPanel object for the root node.
	 *
	 * @return the entity secondary property panel
	 */
	public static EntitySecondaryPropertyPanel createRootPropertyPanel() {
		return new RootPropertyPanel();
	}

	private EntitySecondaryPanelFactory() {
	}

}
