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

import com.taf.annotation.FactoryObject;
import com.taf.annotation.NotNull;
import com.taf.exception.ParseException;
import com.taf.util.Consts;

/**
 * The ExpressionsConstraintParameter parameter represents a list of string
 * expressions that will be inputed in the solver to generate values.
 * 
 * @see ConstraintParameter
 *
 * @author Adrien Jakubiak
 */
@FactoryObject(types = {}, generate = true)
public class ExpressionsConstraintParameter extends ConstraintParameter {

	/** The parameter name. */
	static final String CONSTRAINT_PARAMETER_NAME = "expressions";

	private static final String NULL_ERROR_MESSAGE = "Expressions must not be null!";

	/** The list of expressions. */
	private List<String> expressions;

	/**
	 * Instantiates a new expressions constraint parameter.
	 */
	public ExpressionsConstraintParameter() {
		super(CONSTRAINT_PARAMETER_NAME);
		expressions = new ArrayList<String>();
	}

	/**
	 * Adds an expression.
	 *
	 * @param expression the expression
	 */
	public void addExpression(String expression) {
		expressions.add(expression.strip());
	}

	/**
	 * Edits the expression at the specified index.
	 *
	 * @param index      the index
	 * @param expression the expression
	 */
	public void editExpression(int index, String expression) {
		expressions.set(index, expression.strip());
	}

	/**
	 * Returns the list of expressions.
	 *
	 * @return the expressions
	 */
	public List<String> getExpressions() {
		return expressions;
	}

	/**
	 * Removes the expression at the specified index.
	 *
	 * @param index the index
	 */
	public void removeExpression(int index) {
		expressions.remove(index);
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
		expressions.clear();

		for (String value : values) {
			if (!value.isBlank()) {
				addExpression(value);
			}
		}
	}

	@Override
	@NotNull
	public String valueToString() {
		if (expressions.isEmpty()) {
			return "";
		}

		final String separator = Consts.ELEMENT_SEPARATOR;
		String res = expressions.get(0);
		for (int i = 1; i < expressions.size(); i++) {
			res += separator + expressions.get(i);
		}

		return res;
	}

}
