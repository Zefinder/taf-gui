package com.taf.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.taf.logic.type.BooleanType;
import com.taf.logic.type.IntegerType;
import com.taf.logic.type.RealType;
import com.taf.logic.type.StringType;
import com.taf.logic.type.Type;
import com.taf.util.HashSetBuilder;

public class TypeManager extends Manager {

	private static final TypeManager instance = new TypeManager();

	private final HashSet<Class<? extends Type>> basicTypeSet = new HashSetBuilder<Class<? extends Type>>()
			.add(BooleanType.class).add(IntegerType.class).add(RealType.class).add(StringType.class).build();

	private final List<String> typeNameList;

	private TypeManager() {
		typeNameList = new ArrayList<String>();
		for (var basicType : basicTypeSet) {
			typeNameList.add(basicType.getSimpleName());
		}
	}

	public List<String> getTypeNames() {
		return typeNameList;
	}

	// TODO Create a type annotation to check at compile time if first constructor
	// has no args.
	public Type instanciateTypeFromClassName(String typeClassName) {
		for (var basicType : basicTypeSet) {
			if (basicType.getSimpleName().equals(typeClassName)) {
				try {
					Type type = (Type) basicType.getConstructors()[0].newInstance();
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
	
	public Type instanciateTypeFromTypeName(String typeName) {
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
