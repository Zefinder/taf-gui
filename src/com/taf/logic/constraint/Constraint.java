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
package com.taf.logic.constraint;

import java.util.List;

import com.taf.annotation.NotEmpty;
import com.taf.annotation.NotNull;
import com.taf.exception.EntityCreationException;
import com.taf.logic.Entity;
import com.taf.logic.constraint.parameter.ConstraintParameter;
import com.taf.logic.constraint.parameter.ExpressionsConstraintParameter;
import com.taf.logic.constraint.parameter.QuantifierType;
import com.taf.logic.constraint.parameter.QuantifiersConstraintParameter;
import com.taf.logic.constraint.parameter.RangesConstraintParameter;
import com.taf.logic.constraint.parameter.RangesConstraintParameter.Range;
import com.taf.logic.constraint.parameter.TypesConstraintParameter;
import com.taf.logic.field.Type;
import com.taf.util.Consts;

/**
 * <p>
 * The Constraint class represents a constraint in TAF. Each constraint can add
 * different parameters:
 * <ul>
 * <li>{@link ExpressionsConstraintParameter} to add constraint expressions.
 * <li>{@link QuantifiersConstraintParameter} to add constraint quantifiers.
 * <li>{@link RangesConstraintParameter} to add a ranges to the quantifiers.
 * <li>{@link TypesConstraintParameter} to add a type to the quantifiers.
 * </ul>
 * </p>
 * 
 * @see Entity
 * @see ConstraintParameter
 * 
 * @author Adrien Jakubiak
 */
public class Constraint implements Entity {

	private static final String NULL_NAME_ERROR_MESSAGE = "The constraint name cannot be null";
	private static final String EMPTY_NAME_ERROR_MESSAGE = "The constraint name cannot be empty";
	
	/** Used to format a constraint to a XML string representation. */
	private static final String CONSTRAINT_STRING_FORMAT = """
			<constraint %s/>""";

	/** The constraint name. */
	private String name;

	/** The constraint parent. */
	private Type parent;

	/** The expressions constraint parameter. */
	private ExpressionsConstraintParameter expressionsConstraintParameter;

	/** The quantifiers constraint parameter. */
	private QuantifiersConstraintParameter quantifiersConstraintParameter;

	/** The ranges constraint parameter. */
	private RangesConstraintParameter rangesConstraintParameter;

	/** The types constraint parameter. */
	private TypesConstraintParameter typesConstraintParameter;

	/**
	 * Instantiates a new constraint.
	 *
	 * @param name the name
	 * @throws EntityCreationException if the name is null or empty
	 */
	public Constraint(String name) throws EntityCreationException {
		if (name == null) {
			throw new EntityCreationException(getClass(), NULL_NAME_ERROR_MESSAGE);
		}
		if (name.isBlank()) {
			throw new EntityCreationException(getClass(), EMPTY_NAME_ERROR_MESSAGE);
		}
		this.name = name;
		expressionsConstraintParameter = new ExpressionsConstraintParameter();
		quantifiersConstraintParameter = new QuantifiersConstraintParameter();
		rangesConstraintParameter = new RangesConstraintParameter();
		typesConstraintParameter = new TypesConstraintParameter();
	}

	/**
	 * Adds a constraint parameter to the constraint.
	 *
	 * @param parameter the parameter
	 */
	public void addConstraintParameter(ConstraintParameter parameter) {
		// We assume that the parameters arrive in that order
		if (parameter instanceof ExpressionsConstraintParameter) {
			for (String expression : ((ExpressionsConstraintParameter) parameter).getExpressions()) {
				expressionsConstraintParameter.addExpression(expression);
			}

		} else if (parameter instanceof QuantifiersConstraintParameter) {
			for (String expression : ((QuantifiersConstraintParameter) parameter).getQuantifiers()) {
				quantifiersConstraintParameter.addQuantifier(expression);
			}

		} else if (parameter instanceof RangesConstraintParameter) {
			List<Range> ranges = ((RangesConstraintParameter) parameter).getRanges();
			int quantifierNumber = quantifiersConstraintParameter.getQuantifiers().size();
			int rangesNumber = ranges.size();
			for (int i = 0; i < Math.min(quantifierNumber, rangesNumber); i++) {
				Range range = ranges.get(i);
				rangesConstraintParameter.addRange(range.getLowerBound(), range.getUpperBound());
			}

		} else if (parameter instanceof TypesConstraintParameter) {
			List<QuantifierType> types = ((TypesConstraintParameter) parameter).getTypes();
			int quantifierNumber = quantifiersConstraintParameter.getQuantifiers().size();
			int rangesNumber = types.size();
			for (int i = 0; i < Math.min(quantifierNumber, rangesNumber); i++) {
				QuantifierType type = types.get(i);
				typesConstraintParameter.addType(type);
			}
		}
	}

	/**
	 * Adds an expression.
	 *
	 * @param expression the expression
	 */
	public void addExpression(String expression) {
		expressionsConstraintParameter.addExpression(expression);
	}

	/**
	 * Adds a quantifier.
	 *
	 * @param quantifier the quantifier
	 * @param leftRange  the left range
	 * @param rightRange the right range
	 * @param type       the type
	 */
	public void addQuantifier(String quantifier, String leftRange, String rightRange, QuantifierType type) {
		quantifiersConstraintParameter.addQuantifier(quantifier);
		rangesConstraintParameter.addRange(leftRange, rightRange);
		typesConstraintParameter.addType(type);
	}

	/**
	 * Edits the expression at the specified index.
	 *
	 * @param index      the index
	 * @param expression the expression
	 */
	public void editExpression(int index, String expression) {
		expressionsConstraintParameter.editExpression(index, expression);
	}

	/**
	 * Edits the left range at the specified index.
	 *
	 * @param index     the index
	 * @param leftRange the left range
	 */
	public void editLeftRange(int index, String leftRange) {
		rangesConstraintParameter.editLeftRange(index, leftRange);
	}

	/**
	 * Edits the quantifier at the specified index.
	 *
	 * @param index      the index
	 * @param quantifier the quantifier
	 */
	public void editQuantifier(int index, String quantifier) {
		quantifiersConstraintParameter.editQuantifier(index, quantifier);
	}

	/**
	 * Edits the quantifier type at the specified index.
	 *
	 * @param index the index
	 * @param type  the type
	 */
	public void editQuantifierType(int index, QuantifierType type) {
		typesConstraintParameter.editType(index, type);
	}

	/**
	 * Edits the right range at the specified index.
	 *
	 * @param index      the index
	 * @param rightRange the right range
	 */
	public void editRightRange(int index, String rightRange) {
		rangesConstraintParameter.editRightRange(index, rightRange);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Constraint)) {
			return false;
		}

		Constraint other = (Constraint) obj;
		return other.parent.equals(parent) && other.name.equals(name);
	}

	@Override
	public String getEntityTypeName() {
		return Consts.CONSTRAINT_ENTITY_NAME;
	}

	/**
	 * Returns the list of expressions.
	 *
	 * @return the expressions
	 */
	public List<String> getExpressions() {
		return expressionsConstraintParameter.getExpressions();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	@NotNull
	public Type getParent() {
		return parent;
	}

	/**
	 * Returns the list of quantifiers.
	 *
	 * @return the quantifiers
	 */
	public List<String> getQuantifiers() {
		return quantifiersConstraintParameter.getQuantifiers();
	}

	/**
	 * Returns the list of ranges.
	 *
	 * @return the ranges
	 */
	public List<RangesConstraintParameter.Range> getRanges() {
		return rangesConstraintParameter.getRanges();
	}

	/**
	 * Returns the list of types.
	 *
	 * @return the types
	 */
	public List<QuantifierType> getTypes() {
		return typesConstraintParameter.getTypes();
	}

	@Override
	public int hashCode() {
		return (this.getClass().toString() + Consts.HASH_SEPARATOR + getName() + parent.getId()).hashCode();
	}

	/**
	 * Returns a nice string representation of the parameters inside the constraint.
	 *
	 * @return the parameters string representation
	 */
	public String parametersToString() {
		final String separator = Consts.PARAMETER_SEPARATOR;
		String paramStr = "";
		if (!expressionsConstraintParameter.getExpressions().isEmpty()) {
			paramStr += separator + expressionsConstraintParameter.toString();
		}

		if (!quantifiersConstraintParameter.getQuantifiers().isEmpty()) {
			paramStr += separator + quantifiersConstraintParameter.toString();
		}

		if (!rangesConstraintParameter.getRanges().isEmpty()) {
			paramStr += separator + rangesConstraintParameter.toString();
		}

		if (!typesConstraintParameter.getTypes().isEmpty()) {
			paramStr += separator + typesConstraintParameter.toString();
		}

		return paramStr;
	}

	/**
	 * Removes the expression at the specified index.
	 *
	 * @param index the index
	 */
	public void removeExpression(int index) {
		expressionsConstraintParameter.removeExpression(index);
	}

	/**
	 * Removes the quantifier at the specified index.
	 *
	 * @param index the index
	 */
	public void removeQuantifier(int index) {
		quantifiersConstraintParameter.removeQuantifier(index);
		rangesConstraintParameter.removeRange(index);
		typesConstraintParameter.removeType(index);
	}

	@Override
	public void setName(@NotEmpty String name) {
		// Because hashcode is not updated in a set, you need to remove and add again to
		// the parent.
		parent.removeEntity(this);
		this.name = name;
		parent.addEntity(this);
	}

	@Override
	public void setParent(Type parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		String paramStr = parametersToString();
		return CONSTRAINT_STRING_FORMAT
				.formatted(Consts.FIELD_STRING_FORMAT.formatted(name, paramStr.strip()).stripTrailing());
	}

}
