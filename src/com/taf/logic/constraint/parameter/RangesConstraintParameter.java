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
package com.taf.logic.constraint.parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import com.taf.annotation.FactoryObject;
import com.taf.annotation.NotNull;
import com.taf.exception.ParseException;
import com.taf.util.Consts;

/**
 * <p>
 * The RangesConstraintParameter parameter represents a list of ranges
 * associated to a quantifier to bound its value.
 * </p>
 * 
 * <p>
 * Because some values are not not known at runtime, ranges are represented by
 * strings.
 * </p>
 * 
 * @see ConstraintParameter
 *
 * @author Adrien Jakubiak
 */
@FactoryObject(types = {}, generate = true)
public class RangesConstraintParameter extends ConstraintParameter {

	/** The parameter name. */
	static final String CONSTRAINT_PARAMETER_NAME = "ranges";

	private static final String NULL_ERROR_MESSAGE = "Ranges must not be null!";

	private static final String ERROR_MESSAGE = "A range must have a valid representation: [a,b]";

	/** The list of ranges. */
	private List<Range> ranges;

	/**
	 * Instantiates a new ranges constraint parameter.
	 */
	public RangesConstraintParameter() {
		super(CONSTRAINT_PARAMETER_NAME);
		ranges = new ArrayList<Range>();
	}

	/**
	 * Adds a range.
	 *
	 * @param left  the left
	 * @param right the right
	 */
	public void addRange(String left, String right) {
		ranges.add(new Range(left.strip(), right.strip()));
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
	 * Edits the left range at the specified index.
	 *
	 * @param index the index
	 * @param left  the left
	 */
	public void editLeftRange(int index, String left) {
		ranges.get(index).setLowerBound(left.strip());
	}

	/**
	 * Edits the right range at the specified index.
	 *
	 * @param index the index
	 * @param right the right
	 */
	public void editRightRange(int index, String right) {
		ranges.get(index).setUpperBound(right.strip());
	}

	/**
	 * Returns the list of ranges.
	 *
	 * @return the ranges
	 */
	public List<Range> getRanges() {
		return ranges;
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

	@Override
	public void stringToValue(String stringValue) throws ParseException {
		if (stringValue == null) {
			throw new ParseException(this.getClass(), NULL_ERROR_MESSAGE);
		}
		if (stringValue.isBlank()) {
			return;
		}

		final String separator = Consts.ELEMENT_SEPARATOR;
		String[] values = stringValue.split(separator);
		ranges.clear();

		for (String value : values) {
			if (!value.isBlank()) {
				Matcher m = Consts.RANGE_PATTERN.matcher(value);
				if (m.find()) {
					String left = m.group(1).stripLeading();
					String right = m.group(2).stripLeading();
					addRange(left, right);
				} else {
					throw new ParseException(this.getClass(), ERROR_MESSAGE);
				}
			}
		}
	}

	/**
	 * The Range class is an internal class that describes a closed interval where
	 * bounds are strings. For example, a range can be
	 * <code>[0, node.nb_instances - 1]</code>.
	 *
	 * @author Adrien Jakubiak
	 */
	public static class Range {

		/** The Constant RANGE_STRING_FORMAT. */
		private static final String RANGE_STRING_FORMAT = "[%s, %s]";

		/** The lower bound. */
		private String lowerBound;

		/** The upper bound. */
		private String upperBound;

		/**
		 * Instantiates a new range.
		 *
		 * @param lowerBound the lower bound
		 * @param upperBound the upper bound
		 */
		public Range(String lowerBound, String upperBound) {
			this.lowerBound = lowerBound;
			this.upperBound = upperBound;
		}

		/**
		 * Returns the lower bound.
		 *
		 * @return the lower bound
		 */
		public String getLowerBound() {
			return lowerBound;
		}

		/**
		 * Sets the lower bound.
		 *
		 * @param lowerBound the new lower bound
		 */
		public void setLowerBound(String lowerBound) {
			this.lowerBound = lowerBound;
		}

		/**
		 * Returns the upper bound.
		 *
		 * @return the upper bound
		 */
		public String getUpperBound() {
			return upperBound;
		}

		/**
		 * Sets the upper bound.
		 *
		 * @param upperBound the new upper bound
		 */
		public void setUpperBound(String upperBound) {
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

		@Override
		public String toString() {
			return RANGE_STRING_FORMAT.formatted(lowerBound, upperBound);
		}
	}

}
