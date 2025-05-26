package com.taf.logic.constraint.parameter;

public class RangeConstraintParameter extends ConstraintParameter {

	private static final String CONSTRAINT_PARAMETER_NAME = "ranges";
	private static final String RANGE_STRING_FORMAT = "[%s, %s]";

	private String left;
	private String right;

	public RangeConstraintParameter(String left, String right) {
		super(CONSTRAINT_PARAMETER_NAME);
		this.left = left;
		this.right = right;
	}

	public void setLeft(String left) {
		this.left = left;
	}

	public void setRight(String right) {
		this.right = right;
	}

	@Override
	public String valueToString() {
		return RANGE_STRING_FORMAT.formatted(left, right);
	}

}
