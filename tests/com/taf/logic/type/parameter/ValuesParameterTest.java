package com.taf.logic.type.parameter;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

class ValuesParameterTest extends ListTypeParameterTest<String> {

	private ValuesParameter valuesParameter;
	
	public ValuesParameterTest() {
		super(new ValuesParameter(), "values");
		valuesParameter = (ValuesParameter) typeParameter;
	}
	
	@Override
	Stream<String> badValueProvider() {
		return Stream.of((String[]) null);
	}
	
	@Override
	Stream<List<String>> listProvider() {
		return Stream.of(
				List.<String>of("a"),
				List.<String>of("b", "v", "c"), 
				List.<String>of("1, 3"),
				List.<String>of("erhg", "jezrgeze")
			);
	}
	
	@Override
	Consumer<List<String>> listProviderConsumer() {
		return values -> {
			values.forEach(value -> valuesParameter.addValue(value));
		};
	}
	
	@Override
	Consumer<List<String>> valuesRemover() {
		return values -> {
			values.forEach(value -> valuesParameter.removeValue(value));
			System.out.println("aaada");
		};
	}

}
