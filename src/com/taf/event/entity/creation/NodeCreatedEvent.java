package com.taf.event.entity.creation;

import com.taf.event.Event;
import com.taf.logic.field.Node;

public class NodeCreatedEvent implements Event {

	private Node node;
	
	public NodeCreatedEvent(Node node) {
		this.node = node;
	}
	
	public Node getNode() {
		return node;
	}

}
