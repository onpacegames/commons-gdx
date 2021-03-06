/*
 * Copyright 2002-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.gemserk.componentsengine.utils;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A hash map using primitive ints as keys rather than objects.
 * @author Justin Couch
 * @author Alex Chaffee (alex@apache.org)
 * @author Stephen Colebourne
 * @author Nathan Sweet
 */
public class CachingFastMapIntValue<K> implements Iterable<CachingFastMapIntValue.Entry<K>> {
	public static final int NOT_PRESENT_VALUE = Integer.MIN_VALUE;
	@SuppressWarnings("rawtypes") Entry[] table;
	@SuppressWarnings("unused") private final float loadFactor;
	private int size, mask, capacity, threshold;
	@SuppressWarnings("rawtypes") private ArrayList<Entry> freeEntries;

	/**
	 * Same as: FastMap(16, 0.75f);
	 */
	public CachingFastMapIntValue () {
		this(16, 0.75f);
	}

	/**
	 * Same as: FastMap(initialCapacity, 0.75f);
	 */
	public CachingFastMapIntValue (int initialCapacity) {
		this(initialCapacity, 0.75f);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public CachingFastMapIntValue (int initialCapacity, float loadFactor) {
		if (initialCapacity > 1 << 30) {
			throw new IllegalArgumentException("initialCapacity is too large.");
		}
		if (initialCapacity < 0) {
			throw new IllegalArgumentException("initialCapacity must be greater than zero.");
		}
		if (loadFactor <= 0) {
			throw new IllegalArgumentException("initialCapacity must be greater than zero.");
		}
		capacity = 1;
		while (capacity < initialCapacity) {
			capacity <<= 1;
		}
		this.loadFactor = loadFactor;
		this.threshold = (int)(capacity * loadFactor);
		this.table = new Entry[capacity];
		this.mask = capacity - 1;
		freeEntries = new ArrayList(capacity);
	}

	public void clearCache () {
		freeEntries.clear();
	}

	@SuppressWarnings("rawtypes")
	public void cache (int count) {
		for (int i = freeEntries.size(); i < count; i++) {
			freeEntries.add(new Entry());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int put (K key, int value) {
		int hash = key.hashCode();
		Entry[] table = this.table;
		int index = hash & mask;
		// Check if key already exists.
		for (Entry e = table[index]; e != null; e = e.next) {
			if (!e.key.equals(key)) {
				continue;
			}
			int oldValue = e.value;
			e.value = value;
			return oldValue;
		}
		int freeEntriesSize = freeEntries.size();
		Entry entry = freeEntriesSize > 0 ? freeEntries.remove(freeEntriesSize - 1) : new Entry();
		entry.hash = hash;
		entry.key = key;
		entry.value = value;
		entry.next = table[index];
		table[index] = entry;
		if (size++ >= threshold) {
			// Rehash.
			int newCapacity = 2 * capacity;
			Entry[] newTable = new Entry[newCapacity];
			int newMask = newCapacity - 1;
			for (Entry element : table) {
				Entry e = element;
				if (e == null) {
					continue;
				}
				do {
					Entry next = e.next;
					index = e.hash & newMask;
					e.next = newTable[index];
					newTable[index] = e;
					e = next;
				} while (e != null);
			}
			this.table = newTable;
			capacity = newCapacity;
			threshold *= 2;
			mask = capacity - 1;
		}
		return NOT_PRESENT_VALUE;
	}

	@SuppressWarnings("rawtypes")
	public int get (Object key) {
		int index = key.hashCode() & mask;
		for (Entry e = table[index]; e != null; e = e.next) {
			if (e.key.equals(key)) {
				return e.value;
			}
		}
		return NOT_PRESENT_VALUE;
	}

	
	@SuppressWarnings("rawtypes")
	public boolean containsValue (int value) {
		Entry[] table = this.table;
		for (int i = table.length - 1; i >= 0; i--) {
			for (Entry e = table[i]; e != null; e = e.next) {
				if (e.value== value) {
					return true;
				}
			}
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public boolean containsKey (Object key) {
		int index = key.hashCode() & mask;
		for (Entry e = table[index]; e != null; e = e.next) {
			if (e.key.equals(key)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int remove (Object key) {
		int index = key.hashCode() & mask;
		Entry prev = table[index];
		Entry e = prev;
		while (e != null) {
			Entry next = e.next;
			if (e.key.equals(key)) {
				size--;
				if (prev == e) {
					table[index] = next;
				} else {
					prev.next = next;
				}
				freeEntries.add(e);
				return e.value;
			}
			prev = e;
			e = next;
		}
		return NOT_PRESENT_VALUE;
	}

	public int size () {
		return size;
	}

	public boolean isEmpty () {
		return size == 0;
	}

	
	@SuppressWarnings("rawtypes")
	public void clear () {
		Entry[] table = this.table;
		for (int index = table.length - 1; index >= 0; index--) {
			for (Entry e = table[index]; e != null; e = e.next) {
				freeEntries.add(e);
			}
			table[index] = null;
		}
		size = 0;
	}

	@Override
	public EntryIterator iterator () {
		return new EntryIterator();
	}

	public class EntryIterator implements Iterator<Entry<K>> {
		private int nextIndex;
		private Entry<K> current;

		EntryIterator () {
			reset();
		}

		@SuppressWarnings("rawtypes")
		public void reset () {
			current = null;
			// Find first bucket.
			Entry[] table = CachingFastMapIntValue.this.table;
			int i;
			for (i = table.length - 1; i >= 0; i--) {
				if (table[i] != null) {
					break;
				}
			}
			nextIndex = i;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public boolean hasNext () {
			if (nextIndex >= 0) {
				return true;
			}
			Entry e = current;
			return e != null && e.next != null;
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public Entry<K> next () {
			// Next entry in current bucket.
			Entry e = current;
			if (e != null) {
				e = e.next;
				if (e != null) {
					current = e;
					return e;
				}
			}
			// Use the bucket at nextIndex and find the next nextIndex.
			Entry[] table = CachingFastMapIntValue.this.table;
			int i = nextIndex;
			e = current = table[i];
			while (--i >= 0) {
				if (table[i] != null) {
					break;
				}
			}
			nextIndex = i;
			return e;
		}

		@Override
		public void remove () {
			CachingFastMapIntValue.this.remove(current.key);
		}
	}

	static public class Entry<K> {
		int hash;
		K key;
		int value;
		@SuppressWarnings("rawtypes") Entry next;

		public K getKey () {
			return key;
		}

		public int getValue () {
			return value;
		}
	}
}