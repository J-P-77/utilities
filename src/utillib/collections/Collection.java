/*
The MIT License (MIT)

Copyright (c) 2014 Justin Palinkas

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package utillib.collections;

import utillib.arrays.ResizingArray;

/**
 * <pre>
 * <b>Current Version 1.1.2</b>
 * 
 * March 10, 2009 (Version 1.0.0)
 *     -First Released
 * 
 * September 14, 2009 (Version 1.1.0)
 *     -Fixed Some Major Methods Conflicts With <T, U> If T Key Was A Number Class
 * 
 * August 17, 2010 (Version 1.1.1)
 *     -Added
 *         -Constructor Collection(int, int)
 * 
 * January 19, 2011 (Version 1.1.2)
 *     -Updated
 *         -<T, U> Stuff
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class Collection<T, U> {
	private ResizingArray<Entry> _Entries = null;

	public Collection() {
		this(5, 5);
	}

	public Collection(int initialcapacity) {
		this(initialcapacity, 5);
	}

	public Collection(int initialcapacity, int increaseby) {
		_Entries = new ResizingArray<Entry>(initialcapacity, increaseby);
	}

	public void add(T key, U object) {
		final int INDEX = keyPosition(key);

		if(INDEX == -1) {
			_Entries.put(new Entry(key, object));
		}
	}

	public void set(T key, U object) {
		final int INDEX = keyPosition(key);

		if(INDEX == -1) {
			_Entries.put(new Entry(key, object));
		} else {
			_Entries.getAt(INDEX).setObject(object);
		}
	}

	public void setAt(int index, U object) {
		_Entries.getAt(index).setObject(object);
	}

	public U get(T key) {
		final int INDEX = keyPosition(key);

		if(INDEX == -1) {
			return null;
		} else {
			return (U)_Entries.getAt(INDEX).getObject();
		}
	}

	public U getAt(int index) {
		final Entry T_ENTRY = _Entries.getAt(index);

		if(T_ENTRY != null) {
			return (U)T_ENTRY.getObject();
		}

		return null;
	}

	public void remove(T key) {
		final int INDEX = keyPosition(key);

		if(INDEX != -1) {
			_Entries.removeAt(INDEX);
		}
	}

	public void removeAll() {
		_Entries.removeAll();
	}

	public void removeAt(int index) {
		_Entries.removeAt(index);
	}

	public void removeObject(U object) {
		removeObject(object, false);
	}

	public void removeObject(U object, boolean removeall) {
		for(int Y = (_Entries.length() - 1); Y != -1; Y--) {
			if(_Entries.getAt(Y).getObject().equals(object)) {
				_Entries.removeAt(Y);

				if(!removeall) {
					break;
				}
			}
		}
	}

	public int keyPosition(T key) {
		if(key == null) {
			throw new RuntimeException("Variable[key] - Is Null");
		}

		for(int X = 0; X < _Entries.length(); X++) {
			if(_Entries.getAt(X).getKey().equals(key)) {
				return X;
			}
		}

		return -1;
	}

	public boolean keyExists(T key) {
		return (keyPosition(key) != -1);
	}

	public boolean isEmpty() {
		return _Entries.isEmpty();
	}

	public boolean validIndex(int index) {
		return _Entries.validIndex(index);
	}

	public Entry getEntry(T key) {
		final int INDEX = keyPosition(key);

		return (INDEX == -1 ? null : _Entries.getAt(INDEX));
	}

	public Entry getEntryAt(int index) {
		return _Entries.getAt(index);
	}

	public int count() {
		return _Entries.length();
	}

	/**
	 * 
	 * @return
	 * @Deprecated use Method count() instead
	 */
	@Deprecated
	public int length() {
		return _Entries.length();
	}

	//CLASS
	public class Entry {
		private final T _KEY;
		private U _Object = null;

		public Entry(T key, U object) {
			_KEY = key;
			_Object = object;
		}

		public T getKey() {
			return _KEY;
		}

		public void setObject(U object) {
			_Object = object;
		}

		public U getObject() {
			return _Object;
		}
	}
}
