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
import com.taf.logic.type.parameter.TypeNameParameter;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.logic.type.parameter.ValuesParameter;
import com.taf.logic.type.parameter.WeightsParameter;
import com.taf.util.Consts;
import com.taf.util.HashSetBuilder;

/**
 * <p>
 * The BooleanType class defines a parameter having a boolean value. Booleans in
 * TAF are represented by the strings <code>true</code> and <code>false</code>
 * and their respective weights.
 * </p>
 * 
 * <p>
 * This field type takes only one optional type parameter: {@link WeightsParameter}. The
 * {@link ValuesParameter} is not mandatory since there are only 2 values: true
 * and false.
 * </p>
 * 
 * @see FieldType
 * @see WeightsParameter
 * 
 * @author Adrien Jakubiak
 */
@FactoryObject(types = {}, generate = true)
public class BooleanType extends FieldType {

	/** The parameter string format. */
	private static final String PARAMETER_FORMAT = "%s %s %s";

	/** The optional type parameters. */
	private static final HashSet<String> OPTIONAL_TYPE_PARAMETERS = new HashSetBuilder<String>()
			.add(WeightsParameter.PARAMETER_NAME).build();

	/** The field type name. */
	public static final String TYPE_NAME = "boolean";

	/** The type parameter name. */
	private TypeNameParameter typeName;

	/** The values parameter. */
	private ValuesParameter values;

	/**
	 * Instantiates a new boolean type.
	 */
	public BooleanType() {
		typeName = new TypeNameParameter(TYPE_NAME);
		values = new ValuesParameter();
		values.addValue(Consts.FALSE_VALUE);
		values.addValue(Consts.TRUE_VALUE);
	}

	@Override
	public void addTypeParameter(TypeParameter typeParameter) {
		// Ignore values
		if (typeParameter instanceof WeightsParameter) {
			values.setWeights(((WeightsParameter) typeParameter).getWeights());
		}
	}

	/**
	 * Edits the false weight.
	 *
	 * @param weight the weight
	 */
	public void editFalseWeight(int weight) {
		values.setWeight(Consts.FALSE_VALUE, weight);
	}

	/**
	 * Edits the true weight.
	 *
	 * @param weight the weight
	 */
	public void editTrueWeight(int weight) {
		values.setWeight(Consts.TRUE_VALUE, weight);
	}

	/**
	 * Returns the false weight.
	 *
	 * @return the false weight
	 */
	public int getFalseWeight() {
		return values.getWeight(Consts.FALSE_VALUE);
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
	 * Returns the true weight.
	 *
	 * @return the true weight
	 */
	public int getTrueWeight() {
		return values.getWeight(Consts.TRUE_VALUE);
	}

	@Override
	public String typeToString() {
		TypeParameter weights = values.createWeightParameter();
		return PARAMETER_FORMAT.formatted(typeName.toString(), values.toString(), weights.toString());
	}

}
