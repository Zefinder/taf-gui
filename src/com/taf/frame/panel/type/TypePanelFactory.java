package com.taf.frame.panel.type;

import com.taf.logic.type.IntegerType;
import com.taf.logic.type.RealType;
import com.taf.logic.type.StringType;
import com.taf.logic.type.Type;

public class TypePanelFactory {

	private TypePanelFactory() {
	}

	public static TypePropertyPanel createTypePropertyPanel(Type type) {
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

}
