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

	private StringType type;

	public StringTypeTest() {
		type = new StringType();
	}

	@Override
	void testTypeDefaultValuesImpl() {
		assertIterableEquals(new LinkedHashSet<Entry<String, Integer>>(), type.getValues());
	}
	
	@Override
	void testTypeMandatoryParametersImpl() {
		assertIterableEquals(new HashSet<String>(), type.getMandatoryParametersName());
	}

	@Override
	void testTypeOptionalParametersImpl() {
		HashSet<String> optionalTypeParameters = new HashSetBuilder<String>()
				.add(ValuesParameter.PARAMETER_NAME).add(WeightsParameter.PARAMETER_NAME).build();
		assertIterableEquals(optionalTypeParameters, type.getOptionalParametersName());
	}

	@Test
	void testAddValueWithWeight() {
		String text = "value";
		int weight = 10;
		type.addValue(text, weight);

		Map<String, Integer> expectedMap = new LinkedHashMap<String, Integer>();
		expectedMap.put(text, weight);
		assertIterableEquals(expectedMap.entrySet(), type.getValues());
	}

	@Test
	void testAddValueWithoutWeight() {
		String text = "value";
		int weight = Consts.DEFAULT_WEIGHT_VALUE;
		type.addValue(text);

		Map<String, Integer> expectedMap = new LinkedHashMap<String, Integer>();
		expectedMap.put(text, weight);
		assertIterableEquals(expectedMap.entrySet(), type.getValues());
	}

	@Test
	void testEditValueName() {
		String oldName = "value";
		String newName = "newValue";
		int weight = Consts.DEFAULT_WEIGHT_VALUE;
		type.addValue(oldName);
		type.editValueName(oldName, newName);

		Map<String, Integer> expectedMap = new LinkedHashMap<String, Integer>();
		expectedMap.put(newName, weight);
		assertIterableEquals(expectedMap.entrySet(), type.getValues());
	}

	@Test
	void testEditValueWeight() {
		String text = "value";
		int newWeight = Consts.DEFAULT_WEIGHT_VALUE + 1;
		type.addValue(text);
		type.setWeight(text, newWeight);

		Map<String, Integer> expectedMap = new LinkedHashMap<String, Integer>();
		expectedMap.put(text, newWeight);
		assertIterableEquals(expectedMap.entrySet(), type.getValues());
	}

	@Test
	void testAddRemoveValue() {
		String text = "value";
		type.addValue(text);
		type.removeValue(text);

		assertIterableEquals(new LinkedHashSet<Entry<String, Integer>>(), type.getValues());
	}

}
