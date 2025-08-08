package com.taf.logic.type.parameter;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

class WeightsParameterTest extends ListTypeParameterTest<Integer> {

	private WeightsParameter weightsParameter;
	
	public WeightsParameterTest() {
		super(new WeightsParameter(), "weights");
		weightsParameter = (WeightsParameter) typeParameter;
	}

	@Override
	Stream<String> badValueProvider() {
		return Stream.of("a", "1;4;e", "1,2", "1;;3", null);
	}

	@Override
	Stream<List<Integer>> listProvider() {
		return Stream.of(
				List.<Integer>of(0),
				List.<Integer>of(2, 3, 4), 
				List.<Integer>of(0, -5)
			);
	}

	@Override
	Consumer<List<Integer>> listProviderConsumer() {
		return values -> {
			values.forEach(value -> weightsParameter.addWeight(value));
		};
	}
	
	@Override
	Consumer<List<Integer>> valuesRemover() {
		return values -> {
			values.forEach(value -> weightsParameter.removeWeight(0));
		};
	}
}
