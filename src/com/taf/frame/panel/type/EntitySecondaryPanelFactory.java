package com.taf.frame.panel.type;

import com.taf.logic.constraint.Constraint;
import com.taf.logic.type.AnonymousType;
import com.taf.logic.type.BooleanType;
import com.taf.logic.type.IntegerType;
import com.taf.logic.type.RealType;
import com.taf.logic.type.StringType;
import com.taf.logic.type.Type;

public class EntitySecondaryPanelFactory {

	private EntitySecondaryPanelFactory() {
	}

	public static EntitySecondaryPropertyPanel createRootPropertyPanel() {
		// TODO
		return null;
	}
	
	public static EntitySecondaryPropertyPanel createFieldPropertyPanel(Type type) {
		if (type instanceof AnonymousType) {
			return new AnonymousPropertyPanel((AnonymousType) type);
		}
		
		if (type instanceof BooleanType) {
			return new BooleanPropertyPanel((BooleanType) type);
		}

		if (type instanceof IntegerType) {
			return new IntegerPropertyPanel((IntegerType) type);
		}

		if (type instanceof RealType) {
			return new RealPropertyPanel((RealType) type);
		}

		if (type instanceof StringType) {
			return new StringPropertyPanel((StringType) type);
		}

		return null;
	}
	
	public static EntitySecondaryPropertyPanel createConstraintPropertyPanel(Constraint constraint) {
		return new ConstraintSecondaryPropertyPanel(constraint);
	}

}
