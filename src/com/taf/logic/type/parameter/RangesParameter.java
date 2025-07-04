package com.taf.logic.type.parameter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import com.taf.exception.ParseException;
import com.taf.manager.ConstantManager;

public class RangesParameter extends TypeParameter {

	public static final String PARAMETER_NAME = "ranges";

	private List<Range> ranges;

	RangesParameter() {
		super(PARAMETER_NAME);
		ranges = new ArrayList<Range>();
	}

	public void addRange(Number lowerBound, Number upperBound) {
		ranges.add(new Range(lowerBound, upperBound));
	}

	public Range getRange(int index) {
		return ranges.get(index);
	}
	
	public List<Range> getRanges() {
		return ranges;
	}

	public int size() {
		return ranges.size();
	}
	
	public void editLowerBound(int index, Number lowerBound) {
		ranges.get(index).lowerBound = lowerBound;
	}

	public void editUpperBound(int index, Number upperBound) {
		ranges.get(index).upperBound = upperBound;
	}
	
	public void removeRange(int index) {
		ranges.remove(index);
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
					double lowerBound = Double.valueOf(m.group(1).stripLeading());
					double upperBound = Double.valueOf(m.group(2).stripLeading());
					addRange(lowerBound, upperBound);
				}
			}
		}
	}

	public static class Range {
		
		private static final String RANGE_STRING_FORMAT = "[%s, %s]";
		private final DecimalFormat realFormatter = ConstantManager.REAL_FORMATTER;

		private Number lowerBound;
		private Number upperBound;

		public Range(Number lowerBound, Number upperBound) {
			this.lowerBound = lowerBound;
			this.upperBound = upperBound;
		}

		public Number getLowerBound() {
			return lowerBound;
		}

		public Number getUpperBound() {
			return upperBound;
		}
		
		@Override
		public String toString() {
			return RANGE_STRING_FORMAT.formatted(realFormatter.format(lowerBound), realFormatter.format(upperBound));
		}
	}

}
