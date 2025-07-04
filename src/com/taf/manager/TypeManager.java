package com.taf.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.taf.logic.field.Type;
import com.taf.logic.type.BooleanType;
import com.taf.logic.type.FieldType;
import com.taf.logic.type.IntegerType;
import com.taf.logic.type.RealType;
import com.taf.logic.type.StringType;
import com.taf.util.HashSetBuilder;

public class TypeManager extends Manager {

	private static final TypeManager instance = new TypeManager();

	private final HashSet<Class<? extends FieldType>> parameterTypeSet = new HashSetBuilder<Class<? extends FieldType>>()
			.add(BooleanType.class).add(IntegerType.class).add(RealType.class).add(StringType.class).build();

	private final Set<String> parameterTypeNameSet;
	private final Set<String> customNodeTypeSet;

	private TypeManager() {
		parameterTypeNameSet = new LinkedHashSet<String>();
		for (var basicType : parameterTypeSet) {
			parameterTypeNameSet.add(basicType.getSimpleName());
		}
		customNodeTypeSet = new LinkedHashSet<String>();
	}

	public void addCustomNodeType(Type type) {
		customNodeTypeSet.add(type.getName());
	}
	
	public void removeCustomNodeType(String typeName) {
		customNodeTypeSet.remove(typeName);
	}
	
	public void resetCustomNodeTypes() {
		customNodeTypeSet.clear();
	}
	
	public Set<String> getParameterTypeNames() {
		return parameterTypeNameSet;
	}
	
	public Set<String> getCustomNodeTypeSet() {
		return customNodeTypeSet;
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

	@Override
	public void initManager() {
		// Nothing to do here
	}
	
	public static TypeManager getInstance() {
		return instance;
	}

}
