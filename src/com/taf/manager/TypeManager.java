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
package com.taf.manager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.taf.annotation.EventMethod;
import com.taf.annotation.ManagerImpl;
import com.taf.annotation.Priority;
import com.taf.event.EventListener;
import com.taf.event.entity.EntityDeletedEvent;
import com.taf.event.entity.NodeTypeChangedEvent;
import com.taf.event.entity.creation.NodeCreatedEvent;
import com.taf.event.entity.creation.TypeCreatedEvent;
import com.taf.logic.Entity;
import com.taf.logic.field.Node;
import com.taf.logic.field.Type;

/**
 * <p>
 * The TypeManager manager focuses on registering parameter types and custom
 * types for nodes (including node references).
 * </p>
 *
 * <p>
 * When a type or a node is added, it will be automatically added to this
 * manager. When a type or node is removed, it will be automatically removed
 * from this manager and from all the nodes that were using it.
 * </p>
 *
 * @author Adrien Jakubiak
 */
@ManagerImpl(priority = Priority.MEDIUM)
public class TypeManager implements Manager, EventListener {

	/** The next type id. */
	private static int nextTypeId;

	/** The manager instance. */
	private static final TypeManager instance = new TypeManager();

	/**
	 * Gets the single instance of TypeManager.
	 *
	 * @return single instance of TypeManager
	 */
	public static TypeManager getInstance() {
		return instance;
	}

	/**
	 * Provide a type id
	 *
	 * @return a type id
	 */
	public static int provideTypeId() {
		return nextTypeId++;
	}

	/** The custom node type set. */
	private final Map<String, Type> customNodeTypeMap;
	
	/** The custom node reference set. */
	private final Map<String, Node> customNodeRefMap;

	/** The type to node map. */
	private final Map<String, Set<Node>> typeToNodeMap;

	/** The reference to node map. */
	private final Map<String, Set<Node>> refToNodeMap;

	private TypeManager() {
		customNodeTypeMap = new LinkedHashMap<String, Type>();
		customNodeRefMap = new LinkedHashMap<String, Node>();
		typeToNodeMap = new HashMap<String, Set<Node>>();
		refToNodeMap = new HashMap<String, Set<Node>>();
	}

	@Override
	public void clear() {
		customNodeTypeMap.clear();
		customNodeRefMap.clear();
		typeToNodeMap.clear();
		refToNodeMap.clear();
		EventManager.getInstance().unregisterEventListener(instance);
	}

	/**
	 * Returns the custom node reference set.
	 *
	 * @return the custom node reference set
	 */
	public Set<String> getCustomNodeRefSet() {
		return customNodeRefMap.keySet();
	}

	/**
	 * Returns the custom node type set.
	 *
	 * @return the custom node type set
	 */
	public Set<String> getCustomNodeTypeSet() {
		return customNodeTypeMap.keySet();
	}

	/**
	 * Returns the node from name.
	 *
	 * @param nodeName the node name
	 * @return the node from name
	 */
	public Node getNodeFromName(String nodeName) {
		return customNodeRefMap.get(nodeName);
	}
	
	/**
	 * Returns the nodes implementing this type.
	 *
	 * @param typeName the type name
	 * @return the nodes implementing this type
	 */
	public Set<Node> getNodesImplementingType(String typeName) {
		return typeToNodeMap.get(typeName);
	}
	
	/**
	 * Returns the nodes referring to this node.
	 *
	 * @param nodeName the node name
	 * @return the nodes referring to this node
	 */
	public Set<Node> getNodesReferringNode(String nodeName) {
		return refToNodeMap.get(nodeName);
	}
	
	/**
	 * Returns the type from name.
	 *
	 * @param typeName the type name
	 * @return the type from name
	 */
	public Type getTypeFromName(String typeName) {
		return customNodeTypeMap.get(typeName);
	}

	@Override
	public void init() {
		nextTypeId = 0;
		EventManager.getInstance().registerEventListener(instance);
	}

	/**
	 * Handler for {@link EntityDeletedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onEntityDeleted(EntityDeletedEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Node) {
			Node node = (Node) entity;
			// Remove references
			removeCustomReference(node.getName());

			// Remove from type map iff it has a type or a ref
			String typeName = node.getTypeName();
			if (node.hasType()) {
				typeToNodeMap.get(typeName).remove(node);
				node.removeType();
			} else if (node.hasRef()) {
				refToNodeMap.get(typeName).remove(node);
				node.removeType();
			}

		} else if (entity instanceof Type) {
			removeCustomNodeType(entity.getName());
		}
	}

	/**
	 * Handler for {@link NodeCreatedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onNodeCreated(NodeCreatedEvent event) {
		addCustomReference(event.getNode());
	}

	/**
	 * Handler for {@link NodeTypeChangedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onNodeTypeChanged(NodeTypeChangedEvent event) {
		Node node = event.getNode();
		String previousValue = event.getPreviousValue();

		// Do nothing if the value did not change
		if (node.getTypeName().equals(previousValue)) {
			return;
		}

		// Remove from old maps if it was not empty
		if (event.hadType()) {
			typeToNodeMap.get(previousValue).remove(node);
		} else if (event.hadRef()) {
			refToNodeMap.get(previousValue).remove(node);
		}

		// Add to new maps
		if (node.hasType()) {
			typeToNodeMap.get(node.getTypeName()).add(node);
		} else if (node.hasRef()) {
			refToNodeMap.get(node.getTypeName()).add(node);
		}
	}

	/**
	 * Handler for {@link TypeCreatedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onTypeCreated(TypeCreatedEvent event) {
		addCustomNodeType(event.getType());
	}

	/**
	 * Reset all types and references.
	 */
	public void resetCustomNodeTypes() {
		// Remove types
		typeToNodeMap.clear();
		customNodeTypeMap.clear();

		// Remove refs
		refToNodeMap.clear();
		customNodeRefMap.clear();
	}

	@Override
	public void unregisterComponents() {
		// Nothing to unregister
	}

	/**
	 * Adds a custom node type to the manager.
	 *
	 * @param type the type to add
	 */
	void addCustomNodeType(Type type) {
		customNodeTypeMap.put(type.getName(), type);
		typeToNodeMap.put(type.getName(), new LinkedHashSet<Node>());
	}

	/**
	 * Adds the custom reference to the manager .
	 *
	 * @param node the reference node to add
	 */
	void addCustomReference(Node node) {
		customNodeRefMap.put(node.getName(), node);
		refToNodeMap.put(node.getName(), new LinkedHashSet<Node>());
	}

	/**
	 * Sets the node reference. Because the node order is not guaranteed, the
	 * referenced node can be not yet initialized when setting the node reference.
	 *
	 * @param nodeName the node name
	 * @param node     the node
	 */
	void setNodeRef(String nodeName, Node node) {
		// Can appear BEFORE the ref node exist...
		refToNodeMap.computeIfAbsent(nodeName, t -> new HashSet<Node>()).add(node);
	}

	/**
	 * Sets the node type to a node. Because types are initialized before nodes, the
	 * type must exist when trying to reference it.
	 *
	 * @param typeName the type name
	 * @param node     the node
	 */
	void setNodeType(String typeName, Node node) {
		// Must appear after type creation!
		if (customNodeTypeMap.containsKey(typeName)) {
			typeToNodeMap.get(typeName).add(node);
		}
	}

	/**
	 * Removes the custom node type from the manager and resets all types from the
	 * nodes having it.
	 *
	 * @param typeName the type name
	 */
	private void removeCustomNodeType(String typeName) {
		// Remove types of associated nodes
		typeToNodeMap.get(typeName).forEach(node -> {
			node.removeType();
			EventManager.getInstance().fireEvent(new NodeTypeChangedEvent(node));
		});
		typeToNodeMap.remove(typeName);

		// Remove from type set
		customNodeTypeMap.remove(typeName);
	}

	/**
	 * Removes the custom reference from the manager and all nodes referencing it.
	 *
	 * @param refName the reference name
	 */
	private void removeCustomReference(String refName) {
		// Remove ref of associated nodes
		refToNodeMap.get(refName).forEach(node -> {
			node.removeType();
			EventManager.getInstance().fireEvent(new NodeTypeChangedEvent(node));
		});
		refToNodeMap.remove(refName);

		// Remove from ref set
		customNodeRefMap.remove(refName);
	}

}
