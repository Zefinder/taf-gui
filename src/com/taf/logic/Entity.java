package com.taf.logic;

import com.taf.logic.field.Type;

public interface Entity {

	String getName();
	
	void setName(String name);
	
	String getEntityTypeName();
	
	Type getParent();
	
	void setParent(Type parent);
	
}
