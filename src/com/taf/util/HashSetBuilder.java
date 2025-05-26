package com.taf.util;

import java.util.HashSet;

public class HashSetBuilder<T> {

	private HashSet<T> elementSet;

	public HashSetBuilder() {
		elementSet = new HashSet<T>();
	}

	public HashSetBuilder<T> add(T element) {
		elementSet.add(element);
		return this;
	}

	public HashSet<T> build() {
		return elementSet;
	}

}
