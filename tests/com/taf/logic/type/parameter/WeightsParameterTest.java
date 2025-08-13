package com.taf.logic.type.parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

class WeightsParameterTest extends ListTypeParameterTest<Integer> {

	private WeightsParameter weightsParameter;
	
	public WeightsParameterTest() {
		super(new WeightsParameter(), "weights");
		weightsParameter = (WeightsParameter) typeParameter;
	}

	@Override
	public Stream<String> badValueProvider() {
		return Stream.of("a", "1;4;e", "1,2", "1;;3", null);
	}

	@Override
	public Stream<List<Integer>> listProvider() {
		return Stream.of(
				List.<Integer>of(0),
				List.<Integer>of(2, 3, 4), 
				List.<Integer>of(0, -5)
			);
	}

	@Override
	public Consumer<List<Integer>> listProviderConsumer() {
		return values -> {
			values.forEach(value -> weightsParameter.addWeight(value));
		};
	}
	
	@Override
	public Consumer<List<Integer>> valuesRemover() {
		return values -> {
			values.forEach(value -> weightsParameter.removeWeight(0));
		};
	}
	
	@Test
	void testWeightsParameterVarArgsConstructor() {
		int[] values = {1, 3, 4, 1, 2};
		for (int value : values) {
			weightsParameter.addWeight(value);
		}
		
		WeightsParameter newParameter = new WeightsParameter(values);
		assertEquals(weightsParameter, newParameter);
	}
}
