package com.taf.frame.panel.entity;

import com.taf.logic.Entity;
import com.taf.logic.constraint.Constraint;
import com.taf.logic.field.Node;
import com.taf.logic.field.Parameter;

public class EntityPrimaryPanelFactory {

	private EntityPrimaryPanelFactory() {
	}
	
	public static final EntityPrimaryPropertyPanel createEntityPropertyPanel(Entity entity) {
		if (entity instanceof Node) {
			return new NodePropertyPanel((Node) entity);
		}
		
		if (entity instanceof Parameter) {			
			return new ParameterPropertyPanel((Parameter) entity);
		}
		
		if (entity instanceof Constraint) {
			return new ConstraintPrimaryPropertyPanel((Constraint) entity);
		}
		
		return null;
	}

}
