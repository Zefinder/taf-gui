package com.taf.logic.type.parameter;

public abstract class TypeParameter {

	private static final String TYPE_PARAMETER_FORMAT = "%s=\"%s\"";
	protected final String name;

	public TypeParameter(String name) {
		this.name = name;
	}

	public abstract String valueToString();

	@Override
	public String toString() {
		return TYPE_PARAMETER_FORMAT.formatted(name, valueToString());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TypeParameter)) {
			return false;
		}

		TypeParameter otherParameter = (TypeParameter) obj;
		return this.name.equals(otherParameter.name) && this.valueToString().equals(otherParameter.valueToString());
	}
	
	@Override
	public int hashCode() {
		return (name + valueToString()).hashCode();
	}

}
