package com.fullwall.maps.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ByIdArray<T> implements Iterable<T> {
	private Object[] elementData;
	private int size;
	private int modCount;
	private int highest;

	public ByIdArray() {
		this(50);
	}

	public ByIdArray(int capacity) {
		if (capacity < 0)
			throw new IllegalArgumentException("illegal capacity");
		elementData = new Object[capacity];
	}

	public void clear() {
		modCount = highest = size = 0;
		elementData = new Object[50];
	}

	public boolean contains(int index) {
		return get(index) != null;
	}

	private void ensureCapacity(int minCapacity) { // from ArrayList
		int oldCapacity = elementData.length;
		if (minCapacity > oldCapacity) {
			int newCapacity = (oldCapacity * 3) / 2 + 1;
			if (newCapacity < minCapacity)
				newCapacity = minCapacity;
			elementData = Arrays.copyOf(elementData, newCapacity);
		}
	}

	private void fastRemove(int index) {
		if (index == highest)
			recalcHighest();
		elementData[index] = null;
		--size;
	}

	@SuppressWarnings("unchecked")
	public T get(int index) {
		return (T) elementData[index];
	}

	/*@SuppressWarnings("unchecked")
	public T[] toArray(T[] a) {
		if (a.length < size)
			// Make a new array of a's runtime type, but my contents:
			return (T[]) Arrays.copyOf(elementData, size, a.getClass());
		System.arraycopy(elementData, 0, a, 0, size);
		if (a.length > size)
			a[size] = null;
		return a;
	}
	*/

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private int idx = 0;

			@Override
			public boolean hasNext() {
				return highest + 1 > idx && size > 0;
			}

			@Override
			@SuppressWarnings("unchecked")
			public T next() {
				T next = (T) elementData[idx];
				if (next == null || idx > highest)
					throw new NoSuchElementException();
				do {
					idx++;
				} while (idx != highest + 1 && elementData[idx] == null);
				return next;
			}

			@Override
			public void remove() {
				ByIdArray.this.fastRemove(idx);
			}
		};
	}

	public void put(int index, T t) {
		if (t == null)
			throw new IllegalArgumentException("t cannot be null");
		++modCount;
		if (index > highest)
			highest = index;

		ensureCapacity(index + 1);

		elementData[index] = t;
		++size;
	}

	private void recalcHighest() {
		while (highest != 0 && elementData[highest--] == null)
			;
	}

	public T remove(int index) {
		++modCount;
		if (index == highest)
			recalcHighest();
		@SuppressWarnings("unchecked")
		T prev = (T) elementData[index];
		elementData[index] = null;
		if (prev != null)
			--size;
		return prev;
	}

	public int size() {
		return size;
	}

	public void trimToSize() {
		if (elementData.length > highest)
			elementData = Arrays.copyOf(elementData, highest + 1);
	}

	public static <T> ByIdArray<T> create() {
		return new ByIdArray<T>();
	}
}
