package com.taf.logic.constraint.parameter;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.taf.logic.constraint.parameter.RangesConstraintParameter.Range;

class RangesConstraintParameterTest extends ConstraintParameterTest<Range> {

	private RangesConstraintParameter rangesConstraintParameter;
	
	public RangesConstraintParameterTest() {
		super(new RangesConstraintParameter(), "ranges");
		rangesConstraintParameter = (RangesConstraintParameter) constraintParameter;
	}
	
	@Override
	public Stream<String> badValueProvider() {
		return Stream.of("a", "1;4;e", "1,2", "1;;3", "[1,5", "1,5]", "1,5;[0,2]", "[0;4]", null);
	}

	@Override
	public Stream<List<Range>> listProvider() {
		return Stream.of(
				List.<Range>of(new Range("0", "1")),
				List.<Range>of(new Range("0", "1"), new Range("0", "aaaaaa"))
			);
	}
	
	@Override
	public Consumer<List<Range>> listProviderConsumer() {
		return values -> {
			values.forEach(value -> rangesConstraintParameter.addRange(value.getLowerBound(), value.getUpperBound()));
		};
	}
	
	@Override
	public Consumer<List<Range>> valuesRemover() {
		return values -> {
			values.forEach(value -> rangesConstraintParameter.removeRange(0));
		};
	}

}
