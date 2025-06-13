package com.taf.logic.constraint.parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.taf.manager.ConstantManager;

public class RangesConstraintParameter extends ConstraintParameter {

	private static final String RANGE_PATTERN_STRING = "\\[[\s]*([^\"]+),[\s]*([^\"]+)\\]";
	private static final Pattern RANGE_PATTERN = Pattern.compile(RANGE_PATTERN_STRING); 
	
	static final String CONSTRAINT_PARAMETER_NAME = "ranges";

	private List<Range> ranges;

	public RangesConstraintParameter() {
		super(CONSTRAINT_PARAMETER_NAME);
		ranges = new ArrayList<Range>();
	}

	public void addRange(String left, String right) {
		ranges.add(new Range(left, right));
	}

	public void removeRange(int index) {
		ranges.remove(index);
	}

	public void editLeftRange(int index, String left) {
		ranges.get(index).setLeft(left);
	}

	public void editRightRange(int index, String right) {
		ranges.get(index).setRight(right);
	}
	
	public List<Range> getRanges() {
		return ranges;
	}

	@Override
	public String valueToString() {
		if (ranges.isEmpty()) {
			return "";
		}

		final String separator = ConstantManager.ELEMENT_SEPARATOR;
		String res = ranges.get(0).toString();
		for (int i = 1; i < ranges.size(); i++) {
			res += separator + ranges.get(i).toString();
		}

		return res;
	}

	@Override
	void stringToValue(String stringValue) {
		final String separator = ConstantManager.ELEMENT_SEPARATOR;
		String[] values = stringValue.split(separator);

		for (String value : values) {
			if (!value.isBlank()) {
				Matcher m = RANGE_PATTERN.matcher(value);
				if (m.find()) {
					String left = m.group(1).stripLeading();
					String right = m.group(2).stripLeading();
					addRange(left, right);
				}
			}
		}
	}
	
	public static class Range {

		private static final String RANGE_STRING_FORMAT = "[%s, %s]";

		private String left;
		private String right;

		public Range(String left, String right) {
			this.left = left;
			this.right = right;
		}

		public String getLeft() {
			return left;
		}
		
		public void setLeft(String left) {
			this.left = left;
		}

		public String getRight() {
			return right;
		}
		
		public void setRight(String right) {
			this.right = right;
		}

		@Override
		public String toString() {
			return RANGE_STRING_FORMAT.formatted(left, right);
		}
	}

}
