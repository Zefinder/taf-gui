package com.taf.frame.panel.field;

import com.taf.logic.field.Field;
import com.taf.logic.field.Node;
import com.taf.logic.field.Parameter;

public class FieldPanelFactory {

	private FieldPanelFactory() {
	}
	
	public static final FieldPropertyPanel createFieldPropertyPanel(Field field) {
		if (field instanceof Node) {
			return new NodePropertyPanel((Node) field);
		}
		
		return new ParameterPropertyPanel((Parameter) field);
	}

}
