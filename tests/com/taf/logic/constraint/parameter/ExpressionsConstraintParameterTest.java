package com.taf.logic.constraint.parameter;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

class ExpressionsConstraintParameterTest extends ConstraintParameterTest<String> {

	private ExpressionsConstraintParameter expressionsConstraintParameter;
	
	public ExpressionsConstraintParameterTest() {
		super(new ExpressionsConstraintParameter(), "expressions");
		expressionsConstraintParameter = (ExpressionsConstraintParameter) constraintParameter;
	}
	
	@Override
	public Stream<String> badValueProvider() {
		return Stream.of((String) null);
	}

	@Override
	public Stream<List<String>> listProvider() {
		return Stream.of(
				List.<String>of("a"),
				List.<String>of("b", "v", "c"), 
				List.<String>of("1, 3"),
				List.<String>of("erhg", "jezrgeze")
			);
	}
	
	@Override
	public Consumer<List<String>> listProviderConsumer() {
		return values -> {
			values.forEach(value -> expressionsConstraintParameter.addExpression(value));
		};
	}
	
	@Override
	public Consumer<List<String>> valuesRemover() {
		return values -> {
			values.forEach(value -> expressionsConstraintParameter.removeExpression(0));
		};
	}
	
}
