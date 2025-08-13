package com.taf.frame.panel.primary;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import com.taf.event.Event;
import com.taf.event.entity.NodeTypeChangedEvent;
import com.taf.logic.field.Node;
import com.taf.manager.EventManager;
import com.taf.manager.TypeManager;
import com.taf.util.Consts;

public class NodePropertyPanel extends EntityPrimaryPropertyPanel {

	private static final long serialVersionUID = 8423915116760040223L;

	private static final String NO_TYPE = "No type";

	private final JComboBox<String> typeNames;

	private String cachedType;
	
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
		
		System.out.println(selectedType);
	}

}
