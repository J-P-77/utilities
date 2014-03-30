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

/**
 * <pre>
 * <b>Current Version 1.0.6</b>
 * 
 * Version 1.0.0
 *     -First Released
 * 
 * Version 1.0.1
 *     -Fixed
 *         -Method Flip (Would Not Flip Properly)
 *         -Method Push (Non-Growing Stack Only) (Pushing Object When Stack Is Full Index Out Of Bounds Error Would Occur)
 * 
 * November 02, 2008 (version 1.0.2)
 *     -Added
 *         -Method insertAt(T i, int index)
 *         -Method insertAt(T[] i, int index)
 * 
 * November 07, 2008 (version 1.0.3)
 *     -Added
 *         -Method Push(T[] i) (Pushes An Array of Objects)
 * 
 * November 29, 2008 (version 1.0.4)
 *     -Added
 *         -Method popItemAt(int index) (Gets And Removes Item)
 * 
 * March 26, 2009 (version 1.0.5)
 *     -Added
 *         -Method deleteItemAt(int index) (Remove Position From Array At index)
 *         -Method delete(int from, int to) (Removes Position From Array from to)
 * 
 * August 25, 2009 (version 1.0.6)
 *     -Added
 *         -Constructor (T[] initialobjects, int initialcapacity) Pushes Array of Ts
 *         -Constructor (T[] initialobjects) Pushes Array of Ts
 * 
 * June 17, 2010 (version 1.0.6)
 *     -Updated
 *         -removeItemAt(int) Now Returns Removed Object
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
@SuppressWarnings("unchecked")
public class MyStackAll<T> implements Cloneable {
	private Object[] _Arr = null;
	private int _Capacity = 1;
	private int _Top = 0;
	private int _IncreaseBy = 5;
	private boolean _Grow = true;

	/**
	 * Simple Stack\Collection
	 */
	public MyStackAll() {
		this(1);
	}

	/**
	 * Simple Stack\Collection
	 * 
	 * @param initialobject
	 *            - (T) Pushes Initial Object Into Stack
	 * 
	 */
	public MyStackAll(T initialobject) {
		this(initialobject, 1);
	}

	/**
	 * Simple Stack\Collection
	 * 
	 * @param initialcapacity
	 *            - (int) Sets Initial Capacity for Stack
	 * 
	 */
	public MyStackAll(int initialcapacity) {
		_Capacity = (initialcapacity <= 0 ? 1 : initialcapacity);

		_Arr = new Object[_Capacity];
	}

	/**
	 * Simple Stack\Collection
	 * 
	 * @param initialcapacity
	 *            - (int) Sets Initial Capacity for Stack
	 * @param initialobject
	 *            - (T) Pushes Initial Object Into Stack
	 * 
	 */
	public MyStackAll(T initialobject, int initialcapacity) {
		this(initialcapacity);
		push(initialobject);
	}

	public MyStackAll(T[] initialobjects) {
		this(initialobjects, initialobjects.length);
	}

	public MyStackAll(T[] initialobjects, int initialcapacity) {
		this(initialcapacity);
		push(initialobjects);
	}

	/**
	 * Simple Stack\Collection (Used for Non-Growing Stack)
	 * 
	 * @param maxcapacity
	 *            - (int) Sets Max Capacity for Stack
	 * @param grow
	 *            - (boolean) Sets Whether The Stack Can Grow (Used false for
	 *            non-growing stack)
	 * 
	 */
	public MyStackAll(int maxcapacity, boolean grow) {
		this(maxcapacity);
		_Grow = grow;
	}

	//Pushes Item To Top Of Stack
	public synchronized void push(T i) {
		if(_Top == _Capacity) {
			if(_Grow) {
				increaseCapacity();
				_Arr[_Top++] = i;
			}
		} else {
			_Arr[_Top++] = i;
		}
	}

	//Pushes Array Of Items To Top Of Stack
	public synchronized void push(T[] i) {
		push(i, 0, i.length);
	}

	//Pushes Array Of Items To Top Of Stack From offset to length
	public synchronized void push(T[] i, int offset, int length) {
		for(int X = offset; X < length; X++) {
			push(i[X]);
		}
	}

	//Gets It Item From Top Of Stack And Removes It
	public synchronized T pop() {
		if(isEmpty()) {
			return null;
		} else {
			T TempT = (T)_Arr[--_Top];
			_Arr[_Top] = null;

			return TempT;
		}
	}

	//Tell Whether The Stack Is Empty
	public synchronized boolean isEmpty() {
		if(_Arr == null || _Top == 0) {
			return true;
		}

		return false;
	}

	//Tell Whether The Stack Is Full
	public synchronized boolean isFull() {
		if(_Arr == null) {
			return true;
		} else {
			if(_Top == _Capacity) {
				return true;
			} else {
				return false;
			}
		}
	}

	//Length Of Stack
	public synchronized int length() {
		return _Top;
	}

	//Sets All Array Items To Null And Reset Top Of Stack
	public synchronized void eraseAll() {
		for(int X = 0; X < _Top; X++) {
			_Arr[X] = null;
		}

		_Top = 0;
	}

	public synchronized void flipStack() {
		int Bottom = 0;
		int Top = (_Top - 1);

		while(Bottom <= Top) {
			Object TempTop = _Arr[Top];

			_Arr[Top] = _Arr[Bottom];
			_Arr[Bottom] = TempTop;

			Bottom++;
			Top--;
		}
	}

	public synchronized T getItemAt(int index) {
		if(validIndex(index)) {
			return (T)_Arr[index];
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index + " Out Of Bounds");
		}
	}

	public synchronized void setItemAt(T i, int index) {
		if(validIndex(index)) {
			_Arr[index] = null;
			_Arr[index] = i;
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index + " Out Of Bounds");
		}
	}

	//Gets It Item From Index Of Stack And Removes It
	public synchronized T popItemAt(int index) {
		if(validIndex(index)) {
			final T TEMP = getItemAt(index);

			removeItemAt(index);

			return TEMP;
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index + " Out Of Bounds");
		}
	}

	public synchronized T removeItemAt(int index) {
		if(validIndex(index)) {
			final Object TEMP = _Arr[index];

			_Arr[index] = null;

			final int TOP = _Top;
			_Top = index;
			for(int X = (index + 1); X < TOP; X++) {
				_Arr[_Top++] = _Arr[X];
			}

			_Arr[_Top] = null;

			return (T)TEMP;
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index + " Out Of Bounds");
		}
	}

	public synchronized void removeItemAt(int from, int to) {
		if(validIndex(from) && validIndex(to)) {
			for(int X = from; X <= to; X++) {
				_Arr[X] = null;
			}

			final int TOP = _Top;
			_Top = from;
			for(int X = (to + 1); X < TOP; X++) {
				_Arr[_Top++] = _Arr[X];
				_Arr[X] = null;
			}
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + from + "-" + to + " Out Of Bounds");
		}
	}

//    public synchronized int removeItem(T i) {
//        return removeItem(i, 0);
//    }
//
//    public synchronized int removeItem(T i, int offset) {
//        for(int X = offset; X < (_Top); X++) {
//            if(_Arr[X].equals(i)) {
//                removeItemAt(X);
//                return X;
//            }
//        }
//
//        return -1;
//    }

	public synchronized T removeItem(T i) {
		return removeItem(i, 0);
	}

	public synchronized T removeItem(T i, int offset) {
		for(int X = offset; X < (_Top); X++) {
			if(_Arr[X].equals(i)) {
				return removeItemAt(X);
			}
		}

		return null;
	}

	public synchronized void removeAllItems(T i) {
		int X = 0;
		while(X < _Top) {
			if(_Arr[X].equals(i)) {
				removeItemAt(X);
			} else {
				X++;
			}
		}
	}

	public synchronized void insertAt(T i, int index) {
		if(validIndex(index)) {
			if(_Grow) {
				if(_Top == _Capacity) {
					increaseCapacity();
				}

				for(int X = (_Top - 1); X >= index; X--) {
					T Temp = (T)_Arr[X];
					_Arr[X] = null;
					_Arr[X + 1] = Temp;
				}

				setItemAt(i, index);
				_Top++;
			}
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index + " Out Of Bounds");
		}
	}

	public synchronized void insertAt(T[] i, int index) {
		if(validIndex(index)) {
			if(_Grow) {
				if((_Top + i.length - 1) >= _Capacity) {
					increaseCapacity((_Top + i.length - 1));
				}

				for(int X = (_Top - 1); X >= index; X--) {
					T Temp = (T)_Arr[X];
					_Arr[X] = null;
					_Arr[X + i.length] = Temp;
				}

				_Top += i.length;

				for(int X = 0; X < i.length; X++, index++) {
					setItemAt(i[X], index);
				}
			}
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index + " Out Of Bounds");
		}
	}

	public void setIncreaseBy(int value) {
		_IncreaseBy = (value <= 0 ? 1 : value);
	}

	public int getIncreaseBy() {
		return _IncreaseBy;
	}

	public Object[] toObjectArray() {
		final Object[] TEMP = new Object[_Top];

		for(int X = 0; X < _Top; X++) {
			TEMP[X] = _Arr[X];
		}

		return TEMP;
	}

	public T[] toArray(T[] array) {
		if(array == null) {
			throw new NullPointerException("Variable[array] - Is Null");
		} else {
			int Counter = 0;
			int Length = _Top;

			while(Counter < array.length && Counter < Length) {
				array[Counter] = (T)_Arr[Counter];
				Counter++;
			}

			return array;
		}
	}

	public synchronized boolean validIndex(int index) {
		return (index >= 0 && index < _Top);
	}

	@Override
	public MyStackAll<T> clone() {
		final MyStackAll<T> CLONE = new MyStackAll();

		CLONE._Capacity = this._Capacity;
		CLONE._Grow = this._Grow;
		CLONE._IncreaseBy = this._IncreaseBy;
		CLONE._Top = this._Top;

		CLONE._Arr = new Object[_Capacity];

		for(int X = 0; X < _Top; X++) {
			CLONE._Arr[X] = _Arr.clone();
		}

		return CLONE;
	}

	private synchronized void increaseCapacity() {
		increaseCapacity(_IncreaseBy);
	}

	private synchronized void increaseCapacity(int increaseby) {
		_Capacity += increaseby;

		Object[] Temp = new Object[_Capacity];

		for(int X = 0; X < _Top; X++) {
			Temp[X] = _Arr[X];
		}
		_Arr = null;
		_Arr = Temp;
		Temp = null;
	}
}
/*
 * T Temp = null; int Counter = 0; int X = (_Top - 1);
 * 
 * for(;X > Counter;) { Temp = (T)_Arr[Counter]; _Arr[Counter] = null;
 * _Arr[Counter] = _Arr[X]; _Arr[X] = null; _Arr[X] = Temp; X--; Counter++; }
 */
/*
 * Object TempTop = _Arr[Bottom]; Object TempBottom = _Arr[Top];
 * 
 * _Arr[Top] = TempBottom; _Arr[Bottom] = TempTop;
 * 
 * Bottom++; Top--;
 */