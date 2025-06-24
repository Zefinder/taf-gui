package com.taf.logic.type.parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import com.taf.exception.ParseException;
import com.taf.logic.type.IntegerType;
import com.taf.manager.ConstantManager;

public class RangesParameter extends TypeParameter {

	public static final String PARAMETER_NAME = "ranges";

	private List<Range> ranges;

	RangesParameter() {
		super(PARAMETER_NAME);
		ranges = new ArrayList<Range>();
	}

	public void addRange(int lowerBound, int upperBound) {
		ranges.add(new Range(lowerBound, upperBound));
	}

	public Range getRange(int index) {
		return ranges.get(index);
	}

	public void editLowerBound(int index, int lowerBound) {
		ranges.get(index).lowerBound = lowerBound;
	}

	public void editUpperBound(int index, int upperBound) {
		ranges.get(index).upperBound = upperBound;
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
	void stringToValue(String stringValue) throws ParseException {
		final String separator = ConstantManager.ELEMENT_SEPARATOR;
		String[] values = stringValue.split(separator);

		for (String value : values) {
			if (!value.isBlank()) {
				Matcher m = ConstantManager.RANGE_PATTERN.matcher(value);
				if (m.find()) {
					int lowerBound = Integer.valueOf(m.group(1).stripLeading());
					int upperBound = Integer.valueOf(m.group(2).stripLeading());
					addRange(lowerBound, upperBound);
				}
			}
		}
	}

	public static class Range {
		
		private static final String RANGE_STRING_FORMAT = "[%d, %d]";

		private int lowerBound;
		private int upperBound;

		public Range(int lowerBound, int upperBound) {
			this.lowerBound = lowerBound;
			this.upperBound = upperBound;
		}

		public int getLowerBound() {
			return lowerBound;
		}

		public int getUpperBound() {
			return upperBound;
		}
		
		@Override
		public String toString() {
			return RANGE_STRING_FORMAT.formatted(lowerBound, upperBound);
		}
	}

}
