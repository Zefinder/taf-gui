package com.taf.logic;

import com.taf.logic.field.Node;

public interface Entity {

	String getName();
	
	void setName(String name);
	
	String getEntityTypeName();
	
	Node getParent();
	
	void setParent(Node parent);
	
}
