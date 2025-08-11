package com.taf.logic.type.parameter;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.taf.util.Consts;

class ValuesParameterTest extends ListTypeParameterTest<String> {

	private ValuesParameter valuesParameter;

	public ValuesParameterTest() {
		super(new ValuesParameter(), "values");
		valuesParameter = (ValuesParameter) typeParameter;
	}

	@Override
	Stream<String> badValueProvider() {
		return Stream.of((String) null);
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
		};
	}

	@Test
	void testValuesParameterSetValueWeight() {
		valuesParameter.addValue("a");
		valuesParameter.setWeight("a", Consts.DEFAULT_WEIGHT_VALUE + 1);
		assertEquals(Consts.DEFAULT_WEIGHT_VALUE + 1, valuesParameter.getWeight("a"));
	}

	@Test
	void testValuesParameterAddWeightedValue() {
		valuesParameter.addValue("a", Consts.DEFAULT_WEIGHT_VALUE + 1);
		assertEquals(Consts.DEFAULT_WEIGHT_VALUE + 1, valuesParameter.getWeight("a"));
	}
	
	@Test
	void testValuesParameterEditValueName() {
		valuesParameter.addValue("a");
		boolean success = valuesParameter.editValueName("a", "b");

		Map<String, Integer> expectedValues = Map.of("b", Consts.DEFAULT_WEIGHT_VALUE);
		assertIterableEquals(expectedValues.entrySet(), valuesParameter.getValues());
		assertEquals(true, success);
	}

	@Test
	void testValuesParameterSetAllValuesWeights() {
		String[] values = new String[] { "a", "b", "aa", "c" };
		for (String value : values) {
			valuesParameter.addValue(value);
		}

		int[] tooFewWeights = { 1, 2 };
		int[] tooFewExpected = { 1, 2, 1, 1 };
		valuesParameter.setWeights(tooFewWeights);
		assertArrayEquals(tooFewExpected, ((WeightsParameter) valuesParameter.createWeightParameter()).getWeights());

		int[] weights = { 3, 4, 5, 6 };
		valuesParameter.setWeights(weights);
		assertArrayEquals(weights, ((WeightsParameter) valuesParameter.createWeightParameter()).getWeights());

		int[] tooMuchWeights = { 7, 8, 9, 10, 11 };
		int[] tooMuchExpected = { 7, 8, 9, 10 };
		valuesParameter.setWeights(tooMuchWeights);
		assertArrayEquals(tooMuchExpected, ((WeightsParameter) valuesParameter.createWeightParameter()).getWeights());
	}

	@Test
	void testValuesParameterVarArgsConstructor() {
		String[] values = new String[] { "a", "b", "aa", "c" };
		for (String value : values) {
			valuesParameter.addValue(value);
		}

		ValuesParameter newParameter = new ValuesParameter(values);
		assertEquals(valuesParameter, newParameter);
	}

}
