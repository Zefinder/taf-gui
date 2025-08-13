package com.taf.util;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.taf.exception.RangeException;

class RangeTreeTest {

	private RangeTree rangeTree;

	public RangeTreeTest() {
		this.rangeTree = new RangeTree();
	}

	@Test
	void testRangeTreeAddConsecutiveIntervals() {
		addRanges(List.of(new Pair<Integer, Integer>(0, 2), new Pair<Integer, Integer>(3, 4),
				new Pair<Integer, Integer>(5, 7)));
		assertParentRangeId(1, 0);
		assertParentRangeId(2, 0);
		assertParentRangeId(3, 0);
		assertRangeId(0, 2, 1);
		assertRangeId(3, 4, 2);
		assertRangeId(5, 7, 3);
	}

	@Test
	void testRangeTreeAddEnclosedInterval() {
		addRanges(List.of(new Pair<Integer, Integer>(0, 10), new Pair<Integer, Integer>(1, 5),
				new Pair<Integer, Integer>(6, 9)));
		assertParentRangeId(1, 0);
		assertParentRangeId(2, 1);
		assertParentRangeId(3, 1);
		assertRangeId(0, 0, 1);
		assertRangeId(1, 5, 2);
		assertRangeId(6, 9, 3);
		assertRangeId(10, 10, 1);
	}

	@Test
	void testRangeTreeAddIntersectingEnclosedInterval() {
		assertRangesError(List.of(new Pair<Integer, Integer>(0, 10), new Pair<Integer, Integer>(1, 5),
				new Pair<Integer, Integer>(6, 10)));
	}

	@Test
	void testRangeTreeAddSingletonInterval() {
		addRanges(List.of(new Pair<Integer, Integer>(0, 2), new Pair<Integer, Integer>(1, 1)));
		assertParentRangeId(1, 0);
		assertParentRangeId(2, 1);
		assertRangeId(0, 0, 1);
		assertRangeId(1, 1, 2);
		assertRangeId(2, 2, 1);
	}

	@Test
	void testRangeTreeAddIntersectingIntervals() {
		assertRangesError(List.of(new Pair<Integer, Integer>(0, 2), new Pair<Integer, Integer>(1, 3)));
	}

	@Test
	void testRangeTreeAddLargerInterval() {
		addRanges(List.of(new Pair<Integer, Integer>(1, 2), new Pair<Integer, Integer>(0, 3)));
		assertParentRangeId(1, 0);
		assertParentRangeId(2, 1);
		assertRangeId(0, 0, 1);
		assertRangeId(1, 2, 2);
		assertRangeId(3, 3, 1);
	}

	@Test
	void testRangeTreeAddLargerIntervalIntersecting() {
		assertRangesError(List.of(new Pair<Integer, Integer>(0, 2), new Pair<Integer, Integer>(3, 5),
				new Pair<Integer, Integer>(-1, 4)));
	}

	@Test
	void testRangeTreeAddSameInterval() {
		assertRangesError(List.of(new Pair<Integer, Integer>(0, 2), new Pair<Integer, Integer>(0, 2)));
	}

	@Test
	void testRangeTreeAddBadRange() {
		assertRangesError(List.of(new Pair<Integer, Integer>(2, 0)));
	}

	@Test
	void testRangeTreeParentIdLastChild() {
		addRanges(List.of(new Pair<Integer, Integer>(-1, 2), new Pair<Integer, Integer>(0, 0),
				new Pair<Integer, Integer>(3, 10), new Pair<Integer, Integer>(6, 9)));
		assertParentRangeId(2, 1);
		assertParentRangeId(4, 3);
	}

	@Test
	void testRangeTreeInvalidRange() {
		addRanges(List.of(new Pair<Integer, Integer>(1, 2), new Pair<Integer, Integer>(0, 3)));
		assertRangeId(4, 6, 0);
		assertParentRangeId(8, -1);
	}
	
	@Test
	void testRangeTreeParentIdDeepTree() {
		List<Pair<Integer, Integer>> ranges = new ArrayList<Pair<Integer, Integer>>();
		ranges.addAll(generateCompleteTree(0, 9));
		ranges.addAll(generateCompleteTree(10, 19));
		ranges.addAll(generateCompleteTree(20, 29));
		addRanges(ranges);

		for (int n = 0; n <= 29; n++) {
			int mod = n % 10;
			int delta = 3 * Math.floorDiv(n, 10);
			int id = n - delta;
			switch (mod) {
			case 0:
			case 1:
			case 2:
			case 3:
				id += 1;
				break;

			case 4:
				id -= 2;
				break;

			case 5:
			case 6:
			case 7:
				id += 0;
				break;

			case 8:
				id -= 3;
				break;

			case 9:
				id -= 8;
				break;
			}

			assertRangeId(n, n, id);
		}
	}

	private void addRanges(List<Pair<Integer, Integer>> ranges) {
		try {
			for (Pair<Integer, Integer> range : ranges) {
				rangeTree.addRange(range.getKey(), range.getValue());
			}
			rangeTree.numberTree();
		} catch (RangeException e) {
			fail("The ranges could not be added: " + e.getMessage());
		}
	}

	private void assertRangesError(List<Pair<Integer, Integer>> ranges) {
		assertThrows(RangeException.class, () -> {
			for (Pair<Integer, Integer> range : ranges) {
				rangeTree.addRange(range.getKey(), range.getValue());
			}
		});
	}

	private void assertRangeId(int start, int end, int expectedId) {
		for (int i = start; i <= end; i++) {
			assertEquals(expectedId, rangeTree.getRangeId(i));
		}
	}

	private void assertParentRangeId(int rangeId, int expectedId) {
		assertEquals(expectedId, rangeTree.getParentId(rangeId));
	}

	private static List<Pair<Integer, Integer>> generateCompleteTree(int start, int end) {
		if (start > end) {
			return null;
		}

		List<Pair<Integer, Integer>> results = new ArrayList<Pair<Integer, Integer>>();
		results.add(new Pair<Integer, Integer>(start, end));

		int size = end - start;
		if (size == 2) {
			results.add(new Pair<Integer, Integer>(start + 1, start + 1));
		} else if (size > 2) {
			int start1 = start + 1;
			int end1 = start + Math.floorDiv(size, 2);
			int start2 = end1 + 1;
			int end2 = end - 1;

			results.addAll(generateCompleteTree(start1, end1));
			results.addAll(generateCompleteTree(start2, end2));
		}

		return results;
	}

}
