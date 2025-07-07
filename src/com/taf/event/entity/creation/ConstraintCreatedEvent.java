package com.taf.event.entity.creation;

import com.taf.event.Event;
import com.taf.logic.constraint.Constraint;

public class ConstraintCreatedEvent implements Event {

	private Constraint constraint;
	
	public ConstraintCreatedEvent(Constraint constraint) {
		this.constraint = constraint;
	}
	
	public Constraint getConstraint() {
		return constraint;
	}

}
