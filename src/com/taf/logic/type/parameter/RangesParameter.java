/*
 * Copyright or © or Copr.
 * 
 * This software is a computer program whose purpose is to generate random test
 * case from a template file describing the data model.
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software. You can use, modify
 * and/or redistribute the software under the terms of the CeCILL-B license as
 * circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * 
 * As a counterpart to the access to the source code and rights to copy, modify
 * and redistribute granted by the license, users are provided only with a
 * limited warranty and the software's author, the holder of the economic
 * rights, and the successive licensors have only limited liability.
 * 
 * In this respect, the user's attention is drawn to the risks associated with
 * loading, using, modifying and/or developing or reproducing the software by
 * the user in light of its specific status of free software, that may mean that
 * it is complicated to manipulate, and that also therefore means that it is
 * reserved for developers and experienced professionals having in-depth
 * computer knowledge. Users are therefore encouraged to load and test the
 * software's suitability as regards their requirements in conditions enabling
 * the security of their systems and/or data to be ensured and, more generally,
 * to use and operate it in the same conditions as regards security.
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package com.taf.logic.type.parameter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import com.taf.annotation.FactoryObject;
import com.taf.annotation.NotNull;
import com.taf.exception.ParseException;
import com.taf.util.Consts;

/**
 * The RangesParameter parameter represents a list of ranges in an interval
 * distribution. This is used in {@link DistributionParameter} and can either
 * store integer or real values.
 * 
 * @see TypeParameter
 * @see DistributionParameter
 *
 * @author Adrien Jakubiak
 */
@FactoryObject(types = {}, generate = false)
public class RangesParameter extends TypeParameter {

	/** The parameter name. */
	public static final String PARAMETER_NAME = "ranges";

	private static final String NULL_ERROR_MESSAGE = "Ranges must not be null!";

	private static final String DOUBLE_ERROR_MESSAGE = "The value in a range must be a valid real";

	private static final String ERROR_MESSAGE = "A range must have a valid representation: [a,b]";

	/** The list of ranges. */
	private List<Range> ranges;

	/**
	 * Instantiates a new ranges parameter.
	 */
	RangesParameter() {
		super(PARAMETER_NAME);
		ranges = new ArrayList<Range>();
	}

	/**
	 * Adds a new range.
	 *
	 * @param lowerBound the lower bound
	 * @param upperBound the upper bound
	 */
	public void addRange(Number lowerBound, Number upperBound) {
		ranges.add(new Range(lowerBound, upperBound));
	}

	/**
	 * Edits the lower bound at the specified index.
	 *
	 * @param index      the index
	 * @param lowerBound the lower bound
	 */
	public void editLowerBound(int index, Number lowerBound) {
		ranges.get(index).lowerBound = lowerBound;
	}

	/**
	 * Edits the upper bound at the specified index.
	 *
	 * @param index      the index
	 * @param upperBound the upper bound
	 */
	public void editUpperBound(int index, Number upperBound) {
		ranges.get(index).upperBound = upperBound;
	}

	/**
	 * Returns the range at the specified index.
	 *
	 * @param index the index
	 * @return the range
	 */
	public Range getRange(int index) {
		return ranges.get(index);
	}

	/**
	 * Returns all ranges.
	 *
	 * @return the ranges
	 */
	public List<Range> getRanges() {
		return ranges;
	}

	/**
	 * Removes the range at the specified index.
	 *
	 * @param index the index
	 */
	public void removeRange(int index) {
		ranges.remove(index);
	}

	/**
	 * Returns the number of ranges.
	 *
	 * @return the number of ranges
	 */
	public int size() {
		return ranges.size();
	}

	@Override
	public void stringToValue(String stringValue) throws ParseException {
		if (stringValue == null) {
			throw new ParseException(this.getClass(), NULL_ERROR_MESSAGE);
		}
		if (stringValue.isBlank()) {
			// No weight to put
			return;
		}

		final String separator = Consts.ELEMENT_SEPARATOR;
		String[] values = stringValue.split(separator);
		ranges.clear();

		for (String value : values) {
			if (!value.isBlank()) {
				Matcher m = Consts.RANGE_PATTERN.matcher(value);
				if (m.find()) {
					try {
						double lowerBound = Double.valueOf(m.group(1).stripLeading());
						double upperBound = Double.valueOf(m.group(2).stripLeading());
						addRange(lowerBound, upperBound);
					} catch (NumberFormatException e) {
						throw new ParseException(this.getClass(), DOUBLE_ERROR_MESSAGE);
					}
				} else {
					throw new ParseException(this.getClass(), ERROR_MESSAGE);
				}
			}
		}
	}

	@Override
	@NotNull
	public String valueToString() {
		if (ranges.isEmpty()) {
			return "";
		}

		final String separator = Consts.ELEMENT_SEPARATOR;
		String res = ranges.get(0).toString();
		for (int i = 1; i < ranges.size(); i++) {
			res += separator + ranges.get(i).toString();
		}

		return res;
	}

	/**
	 * The Range class is an internal class that describes a closed interval of
	 * numbers. The numbers can be either integers or reals. Depending on their
	 * value, they will be displayed differently.
	 *
	 * @author Adrien Jakubiak
	 */
	public static class Range {

		/** The display format. */
		private static final String RANGE_STRING_FORMAT = "[%s, %s]";

		/** The real formatter. */
		private final DecimalFormat realFormatter = Consts.REAL_FORMATTER;

		/** The lower bound. */
		private Number lowerBound;

		/** The upper bound. */
		private Number upperBound;

		/**
		 * Instantiates a new range.
		 *
		 * @param lowerBound the lower bound
		 * @param upperBound the upper bound
		 */
		public Range(Number lowerBound, Number upperBound) {
			this.lowerBound = lowerBound;
			this.upperBound = upperBound;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Range)) {
				return false;
			}

			Range other = (Range) obj;
			return other.lowerBound.equals(lowerBound) && other.upperBound.equals(upperBound);
		}

		/**
		 * Returns the lower bound.
		 *
		 * @return the lower bound
		 */
		public Number getLowerBound() {
			return lowerBound;
		}

		/**
		 * Returns the upper bound.
		 *
		 * @return the upper bound
		 */
		public Number getUpperBound() {
			return upperBound;
		}

		@Override
		public String toString() {
			return RANGE_STRING_FORMAT.formatted(realFormatter.format(lowerBound), realFormatter.format(upperBound));
		}
	}

}
