package com.taf.event.entity.creation;

import com.taf.event.Event;
import com.taf.logic.field.Parameter;

public class ParameterCreatedEvent implements Event {

	private Parameter parameter;

	public ParameterCreatedEvent(Parameter parameter) {
		this.parameter = parameter;
	}

	public Parameter getParameter() {
		return parameter;
	}
	
}
