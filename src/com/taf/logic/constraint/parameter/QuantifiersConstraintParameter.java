package com.taf.logic.constraint.parameter;

import java.util.ArrayList;
import java.util.List;

import com.taf.util.Consts;

public class QuantifiersConstraintParameter extends ConstraintParameter {

	// We assume that there can only be one quantifier...

	static final String CONSTRAINT_PARAMETER_NAME = "quantifiers";

	private List<String> quantifiers;

	public QuantifiersConstraintParameter() {
		super(CONSTRAINT_PARAMETER_NAME);
		quantifiers = new ArrayList<String>();
	}

	public void addQuantifier(String quantifier) {
		quantifiers.add(quantifier.strip());
	}
	
	public void removeQuantifier(int index) {
		quantifiers.remove(index);
	}
	
	public void editQuantifier(int index, String quantifier) {
		quantifiers.set(index, quantifier.strip());
	}
	
	public List<String> getQuantifiers() {
		return quantifiers;
	}
	
	@Override
	public String valueToString() {
		if (quantifiers.isEmpty()) {
			return "";
		}

		final String separator = Consts.ELEMENT_SEPARATOR;
		String res = quantifiers.get(0);
		for (int i = 1; i < quantifiers.size(); i++) {
			res += separator + quantifiers.get(i);
		}
		
		return res;
	}
	
	@Override
	public void stringToValue(String stringValue) {
		final String separator = Consts.ELEMENT_SEPARATOR;
		String[] values = stringValue.split(separator);

		for (String value : values) {
			if (!value.isBlank()) {
				addQuantifier(value);
			}
		}
	}

}
