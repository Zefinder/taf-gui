package com.taf.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.taf.event.EventListener;
import com.taf.event.EventMethod;
import com.taf.event.entity.EntityDeletedEvent;
import com.taf.event.entity.NodeTypeChangedEvent;
import com.taf.event.entity.creation.NodeCreatedEvent;
import com.taf.event.entity.creation.TypeCreatedEvent;
import com.taf.logic.Entity;
import com.taf.logic.field.Node;
import com.taf.logic.field.Type;
import com.taf.logic.type.BooleanType;
import com.taf.logic.type.FieldType;
import com.taf.logic.type.IntegerType;
import com.taf.logic.type.RealType;
import com.taf.logic.type.StringType;
import com.taf.util.HashSetBuilder;

public class TypeManager extends Manager implements EventListener {

	private static final TypeManager instance = new TypeManager();

	private final HashSet<Class<? extends FieldType>> parameterTypeSet = new HashSetBuilder<Class<? extends FieldType>>()
			.add(BooleanType.class).add(IntegerType.class).add(RealType.class).add(StringType.class).build();

	private final Set<String> parameterTypeNameSet;
	private final Set<String> customNodeTypeSet;
	private final Set<String> customNodeRefSet;
	private final Map<String, Set<Node>> typeToNodeMap;
	private final Map<String, Set<Node>> refToNodeMap;

	private TypeManager() {
		parameterTypeNameSet = new LinkedHashSet<String>();
		for (var basicType : parameterTypeSet) {
			parameterTypeNameSet.add(basicType.getSimpleName());
		}
		customNodeTypeSet = new LinkedHashSet<String>();
		customNodeRefSet = new LinkedHashSet<String>();
		typeToNodeMap = new HashMap<String, Set<Node>>();
		refToNodeMap = new HashMap<String, Set<Node>>();
	}

	void addCustomNodeType(Type type) {
		customNodeTypeSet.add(type.getName());
		typeToNodeMap.put(type.getName(), new HashSet<Node>());
	}

	private void removeCustomNodeType(String typeName) {
		// Remove types of associated nodes
		typeToNodeMap.get(typeName).forEach(node -> {
			node.removeType();
			EventManager.getInstance().fireEvent(new NodeTypeChangedEvent(node));
		});
		typeToNodeMap.remove(typeName);

		// Remove from type set
		customNodeTypeSet.remove(typeName);
	}

	void addCustomReference(Node node) {
		customNodeRefSet.add(node.getName());
		refToNodeMap.put(node.getName(), new HashSet<Node>());
	}

	private void removeCustomReference(String refName) {
		// Remove ref of associated nodes
		refToNodeMap.get(refName).forEach(node -> {
			node.removeType();
			EventManager.getInstance().fireEvent(new NodeTypeChangedEvent(node));
		});
		refToNodeMap.remove(refName);

		// Remove from ref set
		customNodeRefSet.remove(refName);
	}

	public void resetCustomNodeTypes() {
		// Remove types
		typeToNodeMap.clear();
		customNodeTypeSet.clear();

		// Remove refs
		refToNodeMap.clear();
		customNodeRefSet.clear();
	}

	public Set<String> getParameterTypeNames() {
		return parameterTypeNameSet;
	}

	public Set<String> getCustomNodeTypeSet() {
		return customNodeTypeSet;
	}

	public Set<String> getCustomNodeRefSet() {
		return customNodeRefSet;
	}

	// TODO Create a type annotation to check at compile time if first constructor
	// has no args.
	public FieldType instanciateTypeFromClassName(String typeClassName) {
		for (Class<? extends FieldType> basicType : parameterTypeSet) {
			if (basicType.getSimpleName().equals(typeClassName)) {
				try {
					FieldType type = (FieldType) basicType.getConstructors()[0].newInstance();
					return type;
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | SecurityException e) {
					e.printStackTrace();
					break;
				}
			}
		}

		return null;
	}

	public FieldType instanciateTypeFromTypeName(String typeName) {
		String typeClassName = typeName.substring(0, 1).toUpperCase() + typeName.substring(1) + "Type";
		return instanciateTypeFromClassName(typeClassName);
	}

	void setNodeType(String typeName, Node node) {
		// Must appear after type creation!
		if (customNodeTypeSet.contains(typeName)) {
			typeToNodeMap.get(typeName).add(node);
		}
	}

	void setNodeRef(String nodeName, Node node) {
		// Can appear BEFORE the ref node exist...
		refToNodeMap.computeIfAbsent(nodeName, t -> new HashSet<Node>()).add(node);
	}

	@EventMethod
	public void onTypeCreated(TypeCreatedEvent event) {
		addCustomNodeType(event.getType());
	}

	@EventMethod
	public void onNodeCreated(NodeCreatedEvent event) {
		addCustomReference(event.getNode());
	}

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

	@Override
	public void unregisterComponents() {
		// Nothing to unregister
	}

	@Override
	public void initManager() {
		EventManager.getInstance().registerEventListener(instance);
	}
	
	@Override
	public void clearManager() {
		// Nothing to do here
	}

	public static TypeManager getInstance() {
		return instance;
	}

}
