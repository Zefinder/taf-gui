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
package com.taf.logic.type;

import java.util.List;

import com.taf.logic.type.parameter.DistributionParameter;
import com.taf.logic.type.parameter.DistributionType;
import com.taf.logic.type.parameter.MaxParameter;
import com.taf.logic.type.parameter.MeanParameter;
import com.taf.logic.type.parameter.MinParameter;
import com.taf.logic.type.parameter.RangesParameter;
import com.taf.logic.type.parameter.RangesParameter.Range;
import com.taf.logic.type.parameter.TypeNameParameter;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.logic.type.parameter.VarianceParameter;
import com.taf.logic.type.parameter.WeightsParameter;
import com.taf.util.Consts;

/**
 * <p>
 * The NumericalType class defines a parameter having a numerical value. Numbers
 * in TAF are represented by close ranges with a minimum and a maximum value. A
 * distribution can be set to force the solver to choose a value with more or
 * less probability.
 * </p>
 * 
 * <p>
 * This field type takes two mandatory type parameters:
 * <ul>
 * <li>{@link MinParameter} to represent the lower bound.
 * <li>{@link MaxParameter} to represent the upper bound.
 * </ul>
 * 
 * This field type takes five optional type parameters:
 * <ul>
 * <li>{@link DistributionParameter} to set the distribution type
 * <li>{@link MeanParameter} to set the mean when working with the normal
 * distribution
 * <li>{@link VarianceParameter} to set the variance when working with the
 * normal distribution
 * <li>{@link RangesParameter} to set the ranges when working with the interval
 * distribution
 * <li>{@link WeightsParameter} to set the weights when working with the
 * interval distribution
 * </ul>
 * </p>
 * 
 * @see FieldType
 * @see MinParameter
 * @see MaxParameter
 * @see DistributionParameter
 * @see MeanParameter
 * @see VarianceParameter
 * @see RangesParameter
 * @see WeightsParameter
 * 
 * @author Adrien Jakubiak
 */
public abstract class NumericalType extends FieldType {

	/** The parameter string format. */
	private final String typeName;

	/** The field type name. */
	private TypeParameter typeNameParameter;

	/** The minimum possible value. */
	private MinParameter min;

	/** The maximum possible value. */
	private MaxParameter max;

	/** The probability distribution. */
	private DistributionParameter distribution;

	/**
	 * Instantiates a new numerical type with a defined range.
	 *
	 * @param typeName     the field type name
	 * @param minParameter the min parameter
	 * @param maxParameter the max parameter
	 */
	public NumericalType(String typeName, MinParameter minParameter, MaxParameter maxParameter) {
		this.typeName = typeName;
		this.typeNameParameter = new TypeNameParameter(typeName);
		this.min = minParameter;
		this.max = maxParameter;
		this.distribution = new DistributionParameter(Consts.DEFAULT_DISTRIBUTION);
	}

	/**
	 * Adds an interval.
	 *
	 * @param lowerBound the lower bound
	 * @param upperBound the upper bound
	 * @param weight     the probability weight
	 */
	public void addInterval(Number lowerBound, Number upperBound, int weight) {
		distribution.addInterval(lowerBound, upperBound, weight);
	}

	/**
	 * Edits the lower bound.
	 *
	 * @param index      the index
	 * @param lowerBound the lower bound
	 */
	public void editLowerBound(int index, Number lowerBound) {
		distribution.editLowerBound(index, lowerBound);
	}

	/**
	 * Edits the maximum value.
	 *
	 * @param maxValue the maximum value
	 */
	public void editMaxNumber(Number maxValue) {
		max.setValue(maxValue);
	}

	/**
	 * Edits the mean.
	 *
	 * @param mean the mean
	 */
	public void editMean(double mean) {
		distribution.editMean(mean);
	}

	/**
	 * Edits the minimum value.
	 *
	 * @param minValue the minimum value
	 */
	public void editMinNumber(Number minValue) {
		min.setValue(minValue);
	}

	/**
	 * Edits the upper bound at the selected index.
	 *
	 * @param index      the index
	 * @param upperBound the upper bound
	 */
	public void editUpperBound(int index, Number upperBound) {
		distribution.editUpperBound(index, upperBound);
	}

	/**
	 * Edits the variance.
	 *
	 * @param variance the variance
	 */
	public void editVariance(double variance) {
		distribution.editVariance(variance);
	}

	/**
	 * Edits the weight at the selected index.
	 *
	 * @param index  the index
	 * @param weight the weight
	 */
	public void editWeight(int index, int weight) {
		distribution.editWeight(index, weight);
	}

	/**
	 * Returns the distribution.
	 *
	 * @return the distribution
	 */
	public DistributionType getDistribution() {
		return distribution.getDistributionType();
	}

	/**
	 * Returns the maximum value.
	 *
	 * @return the maximum value
	 */
	public Number getMaxNumber() {
		return max.getValue();
	}

	/**
	 * Returns the mean.
	 *
	 * @return the mean
	 */
	public double getMean() {
		return distribution.getMean();
	}

	/**
	 * Returns the minimum value.
	 *
	 * @return the minimum value
	 */
	public Number getMinNumber() {
		return min.getValue();
	}

	@Override
	public String getName() {
		return typeName;
	}

	/**
	 * Returns the ranges stored in the field type.
	 *
	 * @return the ranges
	 */
	public List<Range> getRanges() {
		return ((RangesParameter) distribution.getRangesParameter()).getRanges();
	}

	/**
	 * Returns the variance.
	 *
	 * @return the variance
	 */
	public double getVariance() {
		return distribution.getVariance();
	}

	/**
	 * Returns the weights associated to the ranges.
	 *
	 * @return the weights
	 */
	public int[] getWeights() {
		return ((WeightsParameter) distribution.getWeightsParameter()).getWeights();
	}

	/**
	 * Removes the interval at the selected index from the field type.
	 *
	 * @param index the index
	 */
	public void removeInterval(int index) {
		distribution.removeInterval(index);
	}

	/**
	 * Sets the distribution.
	 *
	 * @param distributionType the new distribution
	 */
	public void setDistribution(DistributionType distributionType) {
		distribution.setDistributionType(distributionType);
	}

	@Override
	public String typeToString() {
		final String separator = Consts.PARAMETER_SEPARATOR;
		String typeStr = typeNameParameter.toString();

		if (min != null) {
			typeStr += separator + min.toString();
		}

		if (max != null) {
			typeStr += separator + max.toString();
		}

		typeStr += separator + distribution.toString();
		switch (distribution.getDistributionType()) {
		case NORMAL:
			typeStr += separator + distribution.getMeanParameter().toString();
			typeStr += separator + distribution.getVarianceParameter().toString();
			break;

		case INTERVAL:
			typeStr += separator + distribution.getRangesParameter().toString();
			typeStr += separator + distribution.getWeightsParameter().toString();
			break;

		default:
			break;
		}

		return typeStr;
	}

	/**
	 * Returns the number of ranges in the field type.
	 *
	 * @return the number of ranges
	 */
	protected int getRangeNumber() {
		return ((RangesParameter) distribution.getRangesParameter()).size();
	}
}
