package com.taf.logic.type;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.Test;

import com.taf.manager.ConstantManager;

class StringTypeTest extends TypeTest {

	private StringType type;

	public StringTypeTest() {
		type = new StringType();
	}

	@Override
	void testTypeDefaultValuesImpl() {
		assertIterableEquals(new LinkedHashSet<Entry<String, Integer>>(), type.getValues());
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
		int weight = ConstantManager.DEFAULT_WEIGHT_VALUE;
		type.addValue(text);

		Map<String, Integer> expectedMap = new LinkedHashMap<String, Integer>();
		expectedMap.put(text, weight);
		assertIterableEquals(expectedMap.entrySet(), type.getValues());
	}

	@Test
	void testEditValueName() {
		String oldName = "value";
		String newName = "newValue";
		int weight = ConstantManager.DEFAULT_WEIGHT_VALUE;
		type.addValue(oldName);
		type.editValueName(oldName, newName);

		Map<String, Integer> expectedMap = new LinkedHashMap<String, Integer>();
		expectedMap.put(newName, weight);
		assertIterableEquals(expectedMap.entrySet(), type.getValues());
	}

	@Test
	void testEditValueWeight() {
		String text = "value";
		int newWeight = ConstantManager.DEFAULT_WEIGHT_VALUE + 1;
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
