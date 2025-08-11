package com.taf.logic.constraint.parameter;

import static com.taf.logic.constraint.parameter.QuantifierType.EXISTS;
import static com.taf.logic.constraint.parameter.QuantifierType.FORALL;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.taf.exception.ParseException;

class TypesConstraintParameterTest extends ConstraintParameterTest<QuantifierType> {

	private TypesConstraintParameter typesConstraintParameter;
	
	public TypesConstraintParameterTest() {
		super(new TypesConstraintParameter(), "types");
		typesConstraintParameter = (TypesConstraintParameter) constraintParameter;
	}
	
	@Override
	public Stream<String> badValueProvider() {
		return Stream.of((String) null);
	}

	@Override
	public Stream<List<QuantifierType>> listProvider() {
		return Stream.of(
				List.<QuantifierType>of(FORALL),
				List.<QuantifierType>of(EXISTS),
				List.<QuantifierType>of(FORALL, FORALL, EXISTS),
				List.<QuantifierType>of(EXISTS, FORALL)
			);
	}
	
	@Override
	public Consumer<List<QuantifierType>> listProviderConsumer() {
		return values -> {
			values.forEach(value -> typesConstraintParameter.addType(value));
		};
	}
	
	@Override
	public Consumer<List<QuantifierType>> valuesRemover() {
		return values -> {
			values.forEach(value -> typesConstraintParameter.removeType(0));
		};
	}
	
	@Test
	void testTypesConstraintParameterBadTypeString() throws ParseException {
		typesConstraintParameter.stringToValue("a");
		assertEquals(FORALL, typesConstraintParameter.getTypes().get(0));
	}
}
