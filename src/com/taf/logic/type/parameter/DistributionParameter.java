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

import com.taf.annotation.FactoryObject;
import com.taf.annotation.NotNull;
import com.taf.exception.ParseException;
import com.taf.logic.type.NumericalType;
import com.taf.util.Consts;

/**
 * The DistributionParameter parameter represents the distribution of a number
 * type. This parameter is complex and already embeds the mean, variance, ranges
 * and weights parameters so the coder does not have to deal with them.
 * 
 * @see TypeParameter
 * @see DistributionType
 * @see MeanParameter
 * @see VarianceParameter
 * @see RangesParameter
 * @see WeightsParameter
 * @see NumericalType
 *
 * @author Adrien Jakubiak
 */
@FactoryObject(types = {}, generate = false)
public class DistributionParameter extends TypeParameter {

	/** The parameter name. */
	public static final String PARAMETER_NAME = "distribution";

	/** The distribution type. */
	private DistributionType distributionType;

	/** The mean parameter. */
	private MeanParameter meanParameter;

	/** The variance parameter. */
	private VarianceParameter varianceParameter;

	/** The ranges parameter. */
	private RangesParameter rangesParameter;

	/** The weights parameter. */
	private WeightsParameter weightsParameter;

	/**
	 * Instantiates a new distribution parameter.
	 */
	public DistributionParameter() {
		super(PARAMETER_NAME);
		meanParameter = new MeanParameter(Consts.DEFAULT_MEAN_VALUE);
		varianceParameter = new VarianceParameter(Consts.DEFAULT_VARIANCE_VALUE);
		rangesParameter = new RangesParameter();
		weightsParameter = new WeightsParameter();
	}

	/**
	 * Instantiates a new distribution parameter.
	 *
	 * @param distributionType the distribution type
	 */
	public DistributionParameter(DistributionType distributionType) {
		this();
		this.distributionType = distributionType;
	}

	/**
	 * Adds an interval.
	 *
	 * @param lowerBound the lower bound
	 * @param upperBound the upper bound
	 * @param weight     the weight
	 */
	public void addInterval(Number lowerBound, Number upperBound, int weight) {
		rangesParameter.addRange(lowerBound, upperBound);
		weightsParameter.addWeight(weight);
	}

	/**
	 * Edits the lower bound at the selected index.
	 *
	 * @param index      the index
	 * @param lowerBound the lower bound
	 */
	public void editLowerBound(int index, Number lowerBound) {
		rangesParameter.editLowerBound(index, lowerBound);
	}

	/**
	 * Edits the mean.
	 *
	 * @param mean the mean
	 */
	public void editMean(double mean) {
		meanParameter.setMean(mean);
	}

	/**
	 * Edits the upper bound at the selected index.
	 *
	 * @param index      the index
	 * @param upperBound the upper bound
	 */
	public void editUpperBound(int index, Number upperBound) {
		rangesParameter.editUpperBound(index, upperBound);
	}

	/**
	 * Edits the variance.
	 *
	 * @param variance the variance
	 */
	public void editVariance(double variance) {
		varianceParameter.setVariance(variance);
	}

	/**
	 * Edits the weight at the selected index.
	 *
	 * @param index  the index
	 * @param weight the weight
	 */
	public void editWeight(int index, int weight) {
		weightsParameter.editWeight(index, weight);
	}

	/**
	 * Returns the distribution type.
	 *
	 * @return the distribution type
	 */
	public DistributionType getDistributionType() {
		return distributionType;
	}

	/**
	 * Returns the mean.
	 *
	 * @return the mean
	 */
	public double getMean() {
		return meanParameter.getMean();
	}

	/**
	 * Returns the mean parameter.
	 *
	 * @return the mean parameter
	 */
	public TypeParameter getMeanParameter() {
		return meanParameter;
	}

	/**
	 * Returns the ranges parameter.
	 *
	 * @return the ranges parameter
	 */
	public TypeParameter getRangesParameter() {
		return rangesParameter;
	}

	/**
	 * Returns the variance.
	 *
	 * @return the variance
	 */
	public double getVariance() {
		return varianceParameter.getVariance();
	}

	/**
	 * Returns the variance parameter.
	 *
	 * @return the variance parameter
	 */
	public TypeParameter getVarianceParameter() {
		return varianceParameter;
	}

	/**
	 * Returns the weights parameter.
	 *
	 * @return the weights parameter
	 */
	public TypeParameter getWeightsParameter() {
		return weightsParameter;
	}

	/**
	 * Removes the interval at the selected index.
	 *
	 * @param index the index
	 */
	public void removeInterval(int index) {
		rangesParameter.removeRange(index);
		weightsParameter.removeWeight(index);
	}

	/**
	 * Sets the distribution type.
	 *
	 * @param distributionType the new distribution type
	 */
	public void setDistributionType(DistributionType distributionType) {
		this.distributionType = distributionType;
	}

	@Override
	public void stringToValue(String stringValue) throws ParseException {
		setDistributionType(DistributionType.fromDistributionString(stringValue));
	}

	@Override
	@NotNull
	public String valueToString() {
		return distributionType.getDistributionString();
	}

}
