package com.taf.util;

import java.util.HashSet;
import java.util.LinkedHashSet;

public class HashSetBuilder<T> {

	private HashSet<T> elementSet;

	public HashSetBuilder() {
		elementSet = new LinkedHashSet<T>();
	}

	public HashSetBuilder<T> add(T element) {
		elementSet.add(element);
		return this;
	}

	public HashSet<T> build() {
		return elementSet;
	}

}
