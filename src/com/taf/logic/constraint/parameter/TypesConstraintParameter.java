package com.taf.logic.constraint.parameter;

import java.util.ArrayList;
import java.util.List;

import com.taf.exception.ParseException;
import com.taf.util.Consts;

public class TypesConstraintParameter extends ConstraintParameter {

	static final String CONSTRAINT_PARAMETER_NAME = "types";

	private static final String NULL_ERROR_MESSAGE = "Quantifier types must not be null!";
	
	private List<QuantifierType> types;

	public TypesConstraintParameter() {
		super(CONSTRAINT_PARAMETER_NAME);
		types = new ArrayList<QuantifierType>();
	}

	public void addType(QuantifierType type) {
		types.add(type);
	}

	public void removeType(int index) {
		types.remove(index);
	}

	public void editType(int index, QuantifierType type) {
		types.set(index, type);
	}

	public List<QuantifierType> getTypes() {
		return types;
	}

	@Override
	public void stringToValue(String stringValue) throws ParseException {
		if (stringValue == null) {
			throw new ParseException(this.getClass(), NULL_ERROR_MESSAGE);
		} if (stringValue.isBlank()) {
			return;
		}
		
		final String separator = Consts.ELEMENT_SEPARATOR;
		String[] values = stringValue.split(separator);
		types.clear();
		
		for (String value : values) {
			if (!value.isBlank()) {
				QuantifierType type = QuantifierType.fromString(value);
				addType(type);
			}
		}
	}

	@Override
	public String valueToString() {
		if (types.isEmpty()) {
			return "";
		}

		final String separator = Consts.ELEMENT_SEPARATOR;
		String res = types.get(0).getValue();
		for (int i = 1; i < types.size(); i++) {
			res += separator + types.get(i).getValue();
		}

		return res;
	}

}
