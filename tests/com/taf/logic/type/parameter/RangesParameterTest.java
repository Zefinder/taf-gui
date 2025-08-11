package com.taf.logic.type.parameter;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.taf.logic.type.parameter.RangesParameter.Range;

class RangesParameterTest extends ListTypeParameterTest<Range> {

	private RangesParameter rangesParameter;

	public RangesParameterTest() {
		super(new RangesParameter(), "ranges");
		rangesParameter = (RangesParameter) typeParameter;
	}

	@Override
	public Stream<String> badValueProvider() {
		return Stream.of("a", "1;4;e", "1,2", "1;;3", "[1,5", "1,5]", "1,5;[0,2]", "[0;4]", "[a, 2]", null);
	}

	@Override
	public Stream<List<Range>> listProvider() {
		return Stream.of(
				List.of(new Range(0, 2)),
				List.of(new Range(0, 2), new Range(5, 42)),
				List.of(new Range(0, 2), new Range(-50, 1), new Range(-5, 7))
			);
	}

	@Override
	public Consumer<List<Range>> listProviderConsumer() {
		return values -> {
			values.forEach(value -> rangesParameter.addRange(value.getLowerBound(), value.getUpperBound()));
		};
	}

	@Override
	public Consumer<List<Range>> valuesRemover() {
		return values -> {
			values.forEach(value -> rangesParameter.removeRange(0));
		};
	}

}
