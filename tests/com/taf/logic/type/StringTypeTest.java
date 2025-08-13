package com.taf.logic.type;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.Test;

import com.taf.logic.type.parameter.ValuesParameter;
import com.taf.logic.type.parameter.WeightsParameter;
import com.taf.util.Consts;
import com.taf.util.HashSetBuilder;

class StringTypeTest extends TypeTest {

	private StringType stringType;

	public StringTypeTest() {
		super(new StringType(), "string");
		stringType = (StringType) fieldType;
	}

	@Override
	void testTypeDefaultValuesImpl() {
		assertIterableEquals(new LinkedHashSet<Entry<String, Integer>>(), stringType.getValues());
	}
	
	@Override
	void testTypeMandatoryParametersImpl() {
		assertIterableEquals(new HashSet<String>(), stringType.getMandatoryParametersName());
	}

	@Override
	void testTypeOptionalParametersImpl() {
		HashSet<String> optionalTypeParameters = new HashSetBuilder<String>()
				.add(ValuesParameter.PARAMETER_NAME).add(WeightsParameter.PARAMETER_NAME).build();
		assertIterableEquals(optionalTypeParameters, stringType.getOptionalParametersName());
	}

	@Test
	void testAddValueWithWeight() {
		String text = "value";
		int weight = 10;
		stringType.addValue(text, weight);

		Map<String, Integer> expectedMap = new LinkedHashMap<String, Integer>();
		expectedMap.put(text, weight);
		assertIterableEquals(expectedMap.entrySet(), stringType.getValues());
	}

	@Test
	void testAddValueWithoutWeight() {
		String text = "value";
		int weight = Consts.DEFAULT_WEIGHT_VALUE;
		stringType.addValue(text);

		Map<String, Integer> expectedMap = new LinkedHashMap<String, Integer>();
		expectedMap.put(text, weight);
		assertIterableEquals(expectedMap.entrySet(), stringType.getValues());
	}

	@Test
	void testEditValueName() {
		String oldName = "value";
		String newName = "newValue";
		int weight = Consts.DEFAULT_WEIGHT_VALUE;
		stringType.addValue(oldName);
		stringType.editValueName(oldName, newName);

		Map<String, Integer> expectedMap = new LinkedHashMap<String, Integer>();
		expectedMap.put(newName, weight);
		assertIterableEquals(expectedMap.entrySet(), stringType.getValues());
	}

	@Test
	void testEditValueWeight() {
		String text = "value";
		int newWeight = Consts.DEFAULT_WEIGHT_VALUE + 1;
		stringType.addValue(text);
		stringType.setWeight(text, newWeight);

		Map<String, Integer> expectedMap = new LinkedHashMap<String, Integer>();
		expectedMap.put(text, newWeight);
		assertIterableEquals(expectedMap.entrySet(), stringType.getValues());
	}

	@Test
	void testAddRemoveValue() {
		String text = "value";
		stringType.addValue(text);
		stringType.removeValue(text);

		assertIterableEquals(new LinkedHashSet<Entry<String, Integer>>(), stringType.getValues());
	}

}
