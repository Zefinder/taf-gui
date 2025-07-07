package com.taf.event.entity;

import com.taf.event.Event;
import com.taf.logic.field.Node;

public class NodeTypeChangedEvent implements Event {

	private Node node;
	
	private String previousValue;
	private boolean hadType;
	private boolean hadRef;
	
	public NodeTypeChangedEvent(Node node) {
		this.node = node;
		
		this.previousValue = node.getTypeName();
		this.hadType = node.hasType();
		this.hadRef = node.hasRef();
	}
	
	public Node getNode() {
		return node;
	}

	public String getPreviousValue() {
		return previousValue;
	}
	
	public boolean hadType() {
		return hadType;
	}
	
	public boolean hadRef() {
		return hadRef;
	}
}
