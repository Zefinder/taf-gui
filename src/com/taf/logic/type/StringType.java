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
import java.util.Map.Entry;
import java.util.Set;

import com.taf.annotation.FactoryObject;
import com.taf.logic.type.parameter.TypeNameParameter;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.logic.type.parameter.ValuesParameter;
import com.taf.logic.type.parameter.WeightsParameter;
import com.taf.util.Consts;
import com.taf.util.HashSetBuilder;

/**
 * <p>
 * The StringType class defines a parameter having a string value. String in TAF
 * are represented by the equivalent of a Map&lt;String, Integer&gt;,
 * representing a value and its random selection weight.
 * </p>
 * 
 * <p>
 * This field type takes two optional type parameters:
 * <ul>
 * <li>{@link ValuesParameter} to store the values.
 * <li>{@link WeightsParameter} to store the weights.
 * </ul>
 * </p>
 * 
 * @see FieldType
 * @see ValuesParameter
 * @see WeightsParameter
 * 
 * @author Adrien Jakubiak
 */
@FactoryObject(types = {}, generate = true)
public class StringType extends FieldType {

	/** The parameter string format. */
	public static final String TYPE_NAME = "string";

	/** The optional type parameters. */
	private static final HashSet<String> OPTIONAL_TYPE_PARAMETERS = new HashSetBuilder<String>()
			.add(ValuesParameter.PARAMETER_NAME).add(WeightsParameter.PARAMETER_NAME).build();

	/** The field type name. */
	private TypeParameter typeName;

	/** The stored values. */
	private ValuesParameter values;

	/**
	 * Instantiates a new string type.
	 */
	public StringType() {
		typeName = new TypeNameParameter(TYPE_NAME);
		values = new ValuesParameter();
	}

	@Override
	public void addTypeParameter(TypeParameter typeParameter) {
		// We assume that ValuesParameter comes first and then WeightParameter
		// We also assume that weights and values are ordered the same way
		if (typeParameter instanceof ValuesParameter) {
			for (Entry<String, Integer> value : ((ValuesParameter) typeParameter).getValues()) {
				values.addValue(value.getKey());
			}
		} else if (typeParameter instanceof WeightsParameter) {
			values.setWeights(((WeightsParameter) typeParameter).getWeights());
		}
	}

	/**
	 * Adds a value with a default weight.
	 *
	 * @param value the value to add
	 * @return the value of {@link ValuesParameter#addValue(String)}
	 */
	public boolean addValue(String value) {
		return values.addValue(value);
	}

	/**
	 * Adds a value with a special weight.
	 *
	 * @param value  the value to add
	 * @param weight the weight associated to the value
	 * @return the value of {@link ValuesParameter#addValue(String, int)}
	 */
	public boolean addValue(String value, int weight) {
		return values.addValue(value, weight);
	}

	/**
	 * Edits the value name.
	 *
	 * @param oldValue the old value
	 * @param newValue the new value
	 * @return the value of {@link ValuesParameter#editValueName(String, String)}
	 */
	public boolean editValueName(String oldValue, String newValue) {
		return values.editValueName(oldValue, newValue);
	}

	@Override
	public Set<String> getMandatoryParametersName() {
		return new HashSet<String>();
	}

	@Override
	public String getName() {
		return TYPE_NAME;
	}

	@Override
	public Set<String> getOptionalParametersName() {
		return OPTIONAL_TYPE_PARAMETERS;
	}

	/**
	 * Returns the set of values.
	 *
	 * @return the set of values
	 */
	public Set<Entry<String, Integer>> getValues() {
		return values.getValues();
	}

	/**
	 * Removes the value from the value set.
	 *
	 * @param value the value to remove
	 * @return the value of {@link ValuesParameter#removeValue(String)}
	 */
	public boolean removeValue(String value) {
		return values.removeValue(value);
	}

	/**
	 * Sets a new weight for the corresponding value.
	 *
	 * @param value  the value
	 * @param weight the new weight
	 * @return the value of {@link ValuesParameter#setWeight(String, int)}
	 */
	public boolean setWeight(String value, int weight) {
		return values.setWeight(value, weight);
	}

	@Override
	public String typeToString() {
		final String separator = Consts.PARAMETER_SEPARATOR;
		String typeStr = typeName.toString();
		typeStr += separator + values.toString();
		typeStr += separator + values.createWeightParameter().toString();
		return typeStr;
	}

}
