package com.taf.logic.constraint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.taf.logic.constraint.parameter.QuantifierType;
import com.taf.logic.constraint.parameter.RangesConstraintParameter.Range;

class ConstraintTest {

	private static final String name = "test";

	private Constraint constraint;

	public ConstraintTest() {
		constraint = new Constraint(name);
	}

	@Test
	void testEmptyConstraint() {
		assertEquals(name, constraint.getName());
		assertEquals(null, constraint.getParent());
		assertIterableEquals(new ArrayList<String>(), constraint.getExpressions());
		assertIterableEquals(new ArrayList<String>(), constraint.getQuantifiers());
		assertIterableEquals(new ArrayList<Range>(), constraint.getRanges());
		assertIterableEquals(new ArrayList<QuantifierType>(), constraint.getTypes());
	}

	@Test
	void testAddExpression() {
		String expression = "AAAAAAA";
		constraint.addExpression(expression);

		List<String> expectedList = new ArrayList<String>();
		expectedList.add(expression);
		assertIterableEquals(expectedList, constraint.getExpressions());
	}

	@ParameterizedTest
	@EnumSource(value = QuantifierType.class)
	void testAddQuantifier(QuantifierType quantifierType) {
		String quantifierName = "i";
		String leftRange = "0";
		String rightRange = "AAAAAAA";
		constraint.addQuantifier(quantifierName, leftRange, rightRange, quantifierType);

		List<String> expectedQuantifierList = new ArrayList<String>();
		List<Range> expectedRangeList = new ArrayList<Range>();
		List<QuantifierType> expectedQuantifierTypeList = new ArrayList<QuantifierType>();

		expectedQuantifierList.add(quantifierName);
		expectedRangeList.add(new Range(leftRange, rightRange));
		expectedQuantifierTypeList.add(quantifierType);

		assertIterableEquals(expectedQuantifierList, constraint.getQuantifiers());
		assertIterableEquals(expectedRangeList, constraint.getRanges());
		assertIterableEquals(expectedQuantifierTypeList, constraint.getTypes());
	}

	@Test
	void testEditExpression() {
		String oldExpression = "AAAAAAA";
		String newExpression = "BBBBBBB";
		constraint.addExpression(oldExpression);
		constraint.editExpression(0, newExpression);

		List<String> expectedList = new ArrayList<String>();
		expectedList.add(newExpression);
		assertIterableEquals(expectedList, constraint.getExpressions());
	}
	
	@ParameterizedTest
	@EnumSource(value = QuantifierType.class)
	void testEditQuantifierName(QuantifierType quantifierType) {
		String oldQuantifierName = "i";
		String newQuantifierName = "j";
		String leftRange = "0";
		String rightRange = "AAAAAAA";
		constraint.addQuantifier(oldQuantifierName, leftRange, rightRange, quantifierType);
		constraint.editQuantifier(0, newQuantifierName);
		
		List<String> expectedQuantifierList = new ArrayList<String>();
		List<Range> expectedRangeList = new ArrayList<Range>();
		List<QuantifierType> expectedQuantifierTypeList = new ArrayList<QuantifierType>();

		expectedQuantifierList.add(newQuantifierName);
		expectedRangeList.add(new Range(leftRange, rightRange));
		expectedQuantifierTypeList.add(quantifierType);

		assertIterableEquals(expectedQuantifierList, constraint.getQuantifiers());
		assertIterableEquals(expectedRangeList, constraint.getRanges());
		assertIterableEquals(expectedQuantifierTypeList, constraint.getTypes());
	}
	
	@ParameterizedTest
	@EnumSource(value = QuantifierType.class)
	void testEditRange(QuantifierType quantifierType) {
		String quantifierName = "i";
		String oldLeftRange = "0";
		String oldRightRange = "AAAAAAA";
		String newLeftRange = "1";
		String newRightRange = "BBBBBBB";
		constraint.addQuantifier(quantifierName, oldLeftRange, oldRightRange, quantifierType);
		constraint.editLeftRange(0, newLeftRange);
		constraint.editRightRange(0, newRightRange);
		
		List<String> expectedQuantifierList = new ArrayList<String>();
		List<Range> expectedRangeList = new ArrayList<Range>();
		List<QuantifierType> expectedQuantifierTypeList = new ArrayList<QuantifierType>();

		expectedQuantifierList.add(quantifierName);
		expectedRangeList.add(new Range(newLeftRange, newRightRange));
		expectedQuantifierTypeList.add(quantifierType);

		assertIterableEquals(expectedQuantifierList, constraint.getQuantifiers());
		assertIterableEquals(expectedRangeList, constraint.getRanges());
		assertIterableEquals(expectedQuantifierTypeList, constraint.getTypes());
	}
	
	@Test
	void testEditQuantifier() {
		String quantifierName = "i";
		String leftRange = "0";
		String rightRange = "AAAAAAA";
		QuantifierType oldQuantifierType = QuantifierType.EXISTS;
		QuantifierType newQuantifierType = QuantifierType.FORALL;
		constraint.addQuantifier(quantifierName, leftRange, rightRange, oldQuantifierType);
		constraint.editQuantifierType(0, newQuantifierType);

		List<String> expectedQuantifierList = new ArrayList<String>();
		List<Range> expectedRangeList = new ArrayList<Range>();
		List<QuantifierType> expectedQuantifierTypeList = new ArrayList<QuantifierType>();

		expectedQuantifierList.add(quantifierName);
		expectedRangeList.add(new Range(leftRange, rightRange));
		expectedQuantifierTypeList.add(newQuantifierType);

		assertIterableEquals(expectedQuantifierList, constraint.getQuantifiers());
		assertIterableEquals(expectedRangeList, constraint.getRanges());
		assertIterableEquals(expectedQuantifierTypeList, constraint.getTypes());
	}

}
