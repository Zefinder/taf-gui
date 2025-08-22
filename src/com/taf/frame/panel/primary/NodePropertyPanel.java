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
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import com.taf.annotation.FactoryObject;
import com.taf.event.Event;
import com.taf.event.entity.NodeTypeChangedEvent;
import com.taf.logic.Entity;
import com.taf.logic.field.Node;
import com.taf.manager.EventManager;
import com.taf.manager.TypeManager;
import com.taf.util.Consts;

/**
 * The NodePropertyPanel is a primary property panel that represent a node. It
 * allows the user to edit the node name and its type or reference.
 * 
 * @see EntityPrimaryPropertyPanel
 * 
 * @author Adrien Jakubiak
 */
@FactoryObject(types = Entity.class, generate = true)
public class NodePropertyPanel extends EntityPrimaryPropertyPanel {

	private static final long serialVersionUID = 8423915116760040223L;

	/** The default no type option. */
	private static final String NO_TYPE = "No type";

	/** The available type names. */
	private final JComboBox<String> typeNames;

	/** The cached type. */
	private String cachedType;

	/**
	 * Instantiates a new node property panel.
	 *
	 * @param node the node
	 */
	public NodePropertyPanel(Node node) {
		super(node);

		GridBagConstraints c = Consts.getDefaultConstraint();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP);
		c.weightx = 0;
		c.weighty = 0;
		JLabel nodeLabel = new JLabel(Consts.NODE_NAME_LABEL_TEXT);
		this.add(nodeLabel, c);

		c.insets = new Insets(0, Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		this.add(entityName, c);

		c.insets = new Insets(Consts.SMALL_INSET_GAP, 0, 0, Consts.SMALL_INSET_GAP);
		c.weighty = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		JLabel typeNameLabel = new JLabel(Consts.NODE_TYPE_LABEL_TEXT);
		this.add(typeNameLabel, c);

		c.insets = new Insets(Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP, 0, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		model.addElement(NO_TYPE);
		TypeManager.getInstance().getCustomNodeTypeSet().forEach(typeName -> model.addElement(typeName));
		TypeManager.getInstance().getCustomNodeRefSet().forEach(typeName -> model.addElement(typeName));
		typeNames = new JComboBox<String>(model);
		String typeName = node.getType().getName();
		if (typeName.isBlank()) {
			typeName = NO_TYPE;
		}

		cachedType = typeName;

		typeNames.setSelectedItem(typeName);
		typeNames.addActionListener(e -> updateNodeType(node));
		this.add(typeNames, c);
	}

	/**
	 * Update the node type.
	 *
	 * @param node the node
	 */
	private void updateNodeType(Node node) {
		String selectedType = (String) typeNames.getSelectedItem();

		// Only continue if the selected type is different
		if (cachedType.equals(selectedType)) {
			return;
		}

		Event event = new NodeTypeChangedEvent(node);

		// If no type selected then remove recursivity
		if (selectedType.equals(NO_TYPE)) {
			cachedType = selectedType;
			node.removeType();
			EventManager.getInstance().fireEvent(event);
			return;
		}

		// Get custom types list and check if present
		Set<String> customNodeTypeSet = TypeManager.getInstance().getCustomNodeTypeSet();
		if (customNodeTypeSet.contains(selectedType)) {
			cachedType = selectedType;
			node.setType(selectedType);
			EventManager.getInstance().fireEvent(event);
			return;
		}

		// Get node names list and check if present
		Set<String> customRefTypeSet = TypeManager.getInstance().getCustomNodeRefSet();
		if (customRefTypeSet.contains(selectedType)) {
			cachedType = selectedType;
			node.setReference(selectedType);
			EventManager.getInstance().fireEvent(event);
			return;
		}

		// Send a warning to say that the type does not exist
		// TODO
	}

}
