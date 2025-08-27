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

import java.util.HashSet;
import java.util.Set;

import com.taf.annotation.FactoryObject;
import com.taf.logic.type.parameter.DistributionParameter;
import com.taf.logic.type.parameter.MaxRealParameter;
import com.taf.logic.type.parameter.MeanParameter;
import com.taf.logic.type.parameter.MinRealParameter;
import com.taf.logic.type.parameter.RangesParameter;
import com.taf.logic.type.parameter.RangesParameter.Range;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.logic.type.parameter.VarianceParameter;
import com.taf.logic.type.parameter.WeightsParameter;
import com.taf.util.Consts;
import com.taf.util.HashSetBuilder;

/**
 * <p>
 * The RealType class defines a parameter having an real value. This class is a
 * subclass of {@link NumericalType} that works with reals only. All methods are
 * real versions of methods present in the superclass.
 * </p>
 * 
 * <p>
 * Note that the type is called real in TAF, but actually works with double
 * values.
 * </p>
 * 
 * @see NumericalType
 *
 * @author Adrien Jakubiak
 */
@FactoryObject(types = {}, generate = true)
public class RealType extends NumericalType {

	/** The parameter string format. */
	public static final String TYPE_NAME = "real";

	/** The mandatory type parameters. */
	private static final HashSet<String> MANDATORY_TYPE_PARAMETERS = new HashSetBuilder<String>()
			.add(MaxRealParameter.PARAMETER_NAME).add(MinRealParameter.PARAMETER_NAME).build();

	/** The optional type parameters. */
	private static final HashSet<String> OPTIONAL_TYPE_PARAMETERS = new HashSetBuilder<String>()
			.add(DistributionParameter.PARAMETER_NAME).add(MeanParameter.PARAMETER_NAME)
			.add(VarianceParameter.PARAMETER_NAME).add(RangesParameter.PARAMETER_NAME)
			.add(WeightsParameter.PARAMETER_NAME).build();

	/**
	 * Instantiates a new real type.
	 */
	public RealType() {
		super(TYPE_NAME, new MinRealParameter(Consts.DEFAULT_MIN_VALUE),
				new MaxRealParameter(Consts.DEFAULT_MAX_VALUE));
	}

	/**
	 * Adds an interval.
	 *
	 * @param lowerBound the lower bound
	 * @param upperBound the upper bound
	 * @param weight     the weight
	 */
	public void addInterval(double lowerBound, double upperBound, int weight) {
		super.addInterval(lowerBound, upperBound, weight);
	}

	@Override
	public void addTypeParameter(TypeParameter typeParameter) {
		if (typeParameter instanceof MinRealParameter) {
			editMinNumber(((MinRealParameter) typeParameter).getValue().doubleValue());
		} else if (typeParameter instanceof MaxRealParameter) {
			editMaxNumber(((MaxRealParameter) typeParameter).getValue().doubleValue());
		} else if (typeParameter instanceof DistributionParameter) {
			setDistribution(((DistributionParameter) typeParameter).getDistributionType());
		} else if (typeParameter instanceof MeanParameter) {
			editMean(((MeanParameter) typeParameter).getMean());
		} else if (typeParameter instanceof VarianceParameter) {
			editVariance(((VarianceParameter) typeParameter).getVariance());
		} else if (typeParameter instanceof RangesParameter) {
			for (Range range : ((RangesParameter) typeParameter).getRanges()) {
				addInterval(range.getLowerBound(), range.getUpperBound(), Consts.DEFAULT_WEIGHT_VALUE);
			}
		} else if (typeParameter instanceof WeightsParameter) {
			int[] weights = ((WeightsParameter) typeParameter).getWeights();
			int rangeSize = super.getRangeNumber();
			for (int i = 0; i < Math.min(rangeSize, weights.length); i++) {
				editWeight(i, weights[i]);
			}
		}
	}

	/**
	 * Edits the lower bound at the selected index.
	 *
	 * @param index      the index
	 * @param lowerBound the lower bound
	 */
	public void editLowerBound(int index, double lowerBound) {
		super.editLowerBound(index, lowerBound);
	}

	/**
	 * Edits the maximum value as a real (double value in Java)..
	 *
	 * @param maxValue the maximum value
	 */
	public void editMax(long maxValue) {
		super.editMaxNumber(maxValue);
	}

	/**
	 * Edits the minimum value as a real (double value in Java).
	 *
	 * @param minValue the min value
	 */
	public void editMin(long minValue) {
		super.editMinNumber(minValue);
	}

	/**
	 * Edits the upper bound at the selected index.
	 *
	 * @param index      the index
	 * @param upperBound the upper bound
	 */
	public void editUpperBound(int index, double upperBound) {
		super.editUpperBound(index, upperBound);
	}

	@Override
	public Set<String> getMandatoryParametersName() {
		return MANDATORY_TYPE_PARAMETERS;
	}

	/**
	 * Returns the maximum value.
	 *
	 * @return the maximum value
	 */
	public double getMax() {
		return super.getMaxNumber().doubleValue();
	}

	/**
	 * Returns the minimum value.
	 *
	 * @return the minimum value
	 */
	public double getMin() {
		return super.getMinNumber().doubleValue();
	}

	@Override
	public String getName() {
		return TYPE_NAME;
	}

	@Override
	public Set<String> getOptionalParametersName() {
		return OPTIONAL_TYPE_PARAMETERS;
	}

}
