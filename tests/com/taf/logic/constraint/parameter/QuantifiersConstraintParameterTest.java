package com.taf.logic.constraint.parameter;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

class QuantifiersConstraintParameterTest extends ConstraintParameterTest<String> {

	private QuantifiersConstraintParameter quantifiersConstraintParameter;
	
	public QuantifiersConstraintParameterTest() {
		super(new QuantifiersConstraintParameter(), "quantifiers");
		quantifiersConstraintParameter = (QuantifiersConstraintParameter) constraintParameter;
	}
	
	@Override
	public Stream<String> badValueProvider() {
		return Stream.of((String) null);
	}

	@Override
	public Stream<List<String>> listProvider() {
		return Stream.of(
				List.<String>of("a"),
				List.<String>of("b", "v", "c")
			);
	}
	
	@Override
	public Consumer<List<String>> listProviderConsumer() {
		return values -> {
			values.forEach(value -> quantifiersConstraintParameter.addQuantifier(value));
		};
	}
	
	@Override
	public Consumer<List<String>> valuesRemover() {
		return values -> {
			values.forEach(value -> quantifiersConstraintParameter.removeQuantifier(0));
		};
	}

}
