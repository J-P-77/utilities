/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Justin Palinkas
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package jp77.utillib.arrays;

/**
 * <pre>
 * <b>Current Version 1.0.3</b>
 * 
 * November 16, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * April 10, 2009 (Version 1.0.1)
 *     -Added
 *         -New Constructors
 *              -ResizingArray(int initalsize, int increaseby, T initalobject)
 *              -ResizingArray(int initalsize, int increaseby)
 * 
 * December 25, 2009 (Version 1.0.2)
 *     -Updated
 *         -Constructors Can Now Have A Inital Size Of Zero
 * 
 * February 01, 2009 (Version 1.0.3)
 *     -Added
 *          -Methods deleteAt(int index), deleteAll(), delete(int from, int to)
 *              Removes Location And Shinks Array To Fit New Size
 * 
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class ResizingArray<T> implements Cloneable {
	private Object[] _Buffer = null;
	private int _Top = 0;
	private int _IncreaseBy = 5;

//    private int _MaxLength = 0;

	public ResizingArray() {
		this(5);
	}

	public ResizingArray(int initalsize) {
		this(initalsize, 5);
	}

//    public ResizingArray(int initalsize, int increaseby) {
//    	this(initalsize, increaseby, 0);
//    }

	public ResizingArray(int initalsize, int increaseby/*, int maxlength*/) {
		_Buffer = new Object[(initalsize < 0 ? 5 : initalsize)];
		_IncreaseBy = (increaseby < 1 ? 5 : increaseby);
//        _MaxLength = maxlength;
	}

	public ResizingArray(int initalsize, T initalobject) {
		this(initalsize, 5, initalobject);
	}

	public ResizingArray(int initalsize, int increaseby, T initalobject) {
		this(initalsize, increaseby);

		put(initalobject);
	}

	public synchronized/*T*/void put(T object) {
		if(_Top == _Buffer.length) {
			resize();
		}

		_Buffer[_Top++] = object;

		//return object;
	}

	public synchronized void puts(T[] buffer) {
		puts(buffer, 0, buffer.length);
	}

	public synchronized void puts(T[] buffer, int offset, int length) {
		if((_Top + (length - offset)) > _Buffer.length) {
			resize(((length - offset) - (_Buffer.length - _Top)) + _IncreaseBy);
		}

		//len - offset = Number Of Objects To Copy
		//minus
		//_Objects.length - _Top = Number Of Current Free Objects
		//plus _IncreaseBy (Bumper Size)

		for(int X = 0; X < length; X++) {
			_Buffer[_Top++] = buffer[X + offset];
		}
	}

	public synchronized T get(T object) {
		for(int X = 0; X < _Top; X++) {
			if(_Buffer[X].equals(object)) {
				return (T)_Buffer[X];
			}
		}

		return null;
	}

	public synchronized T getAt(int index) {
		if(validIndex(index)) {
			return (T)_Buffer[index];
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index);
		}
	}

	public synchronized void set(int index, T object) {
		if(validIndex(index)) {
			_Buffer[index] = object;
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index);
		}
	}

	public synchronized void removeAll() {
		while(_Top > 0) {
			_Buffer[--_Top] = null;
		}
	}

	public synchronized void remove(T object) {
		for(int X = 0; X < _Top; X++) {
			if(_Buffer[X].equals(object)) {
				removeAt(X);
				break;
			}
		}
	}

	public synchronized void removeAll(T object) {
		for(int X = 0; X < _Top;) {
			if(_Buffer[X].equals(object)) {
				removeAt(X);
			} else {
				X++;
			}
		}
	}

	public synchronized void removeAt(int index) {
		if(validIndex(index)) {
			int Top = _Top;
			_Top = index;
			for(int X = (index + 1); X < Top; X++) {
				_Buffer[_Top] = null;
				_Buffer[_Top++] = _Buffer[X];
			}

			_Buffer[_Top] = null;
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index + " Out Of Bounds");
		}
	}

	public synchronized void insert(int index, T object) {
		if(validIndex(index)) {
			if((_Top + 1) > _Buffer.length) {
				resize();
			}

			int Top = (_Top - 1);
			int Bottom = (index - 1);
			for(int X = (Top); X > Bottom; X--) {
				_Buffer[X + 1] = _Buffer[X];
				_Buffer[X] = null;
			}

			_Buffer[index] = object;

			_Top++;
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index + " Out Of Bounds");
		}
	}

	public synchronized void insert(int index, T[] objects) {
		insert(index, objects, 0, objects.length);
	}

	public synchronized void insert(int index, T[] objects, int offset, int length) {
		if(validIndex(index)) {
			if((_Top + (length - offset)) > _Buffer.length) {
				resize((length - offset) + _IncreaseBy);
			}

			int Top = (_Top - 1);
			_Top = index;
			int Bottom = (Top - (length - offset));
			for(int X = (Top); X > Bottom; X--) {
				_Buffer[X + (length - offset)] = _Buffer[X];
				_Buffer[X] = null;
			}

			for(int X = (offset); X < length; X++) {
				_Buffer[_Top++] = objects[X];
			}

			_Top = (Top + 1) + (length - offset);
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index + " Out Of Bounds");
		}
	}

	public synchronized boolean validIndex(int index) {
		return (index >= 0 && index < _Top);
//        if(index >= 0 && index < _Top) {return true;} else {return false;}
	}

	public void setIncreaseBy(int value) {
		_IncreaseBy = value;
	}

	public int getIncreaseBy() {
		return _IncreaseBy;
	}

	public int length() {
		return _Top;
	}

	public int getCapacity() {
		return _Buffer.length;
	}

	public boolean isEmpty() {
		return _Top == 0;
	}

	public Object[] getArray() {
		return _Buffer;
	}

	public Object[] toObjectArray() {
		final Object[] TEMP = new Object[_Top];

		System.arraycopy(_Buffer, 0, TEMP, 0, _Top);

//        for(int X = 0; X < _Top; X++) {
//            TEMP[X] = _Objects[X];
//        }

		return TEMP;
	}

	public T[] toArray(T[] array) {
		if(array == null) {
			throw new NullPointerException("Class[" + getClass().getName() + "] Method[toArray] Variable[array]");
		} else {
			for(int X = 0; X < _Top && X < array.length; X++) {
				array[X] = (T)_Buffer[X];
			}
		}

		return array;
	}

	public synchronized void eraseAt(int index) {
		if(validIndex(index)) {
			_Buffer[index] = null;
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index);
		}
	}

	public void eraseAll() {
		for(int X = 0; X < _Top; X++) {
			_Buffer[X] = null;
		}

		_Top = 0;
	}

	public synchronized void deleteAt(int index) {
		delete(index, index);
	}

	public synchronized void deleteAll() {
		if(_Top > 0) {
			delete(0, _Top - 1);
		}
	}

	public synchronized void delete(int from, int to) {
		if(validIndex(from) && validIndex(to)) {
			final int TOP = _Top;
			Object[] TempObject = new Object[(TOP - (to - from)) - 1];

			_Top = 0;
			for(int X = 0; X < from; X++) {
				TempObject[_Top++] = _Buffer[X];
			}

			for(int X = (to + 1); X < TOP; X++) {
				TempObject[_Top++] = _Buffer[X];
			}

			_Buffer = null;
			_Buffer = TempObject;
			TempObject = null;
		} else {
			throw new IndexOutOfBoundsException("Indexes Number: " + from + "-" + to + " Out Of Bounds");
		}
	}

	public synchronized void resize() {
		resize(_IncreaseBy);
	}

	public synchronized void resize(int increaseby) {
		Object[] ObjTemp = _Buffer;
		_Buffer = null;
		_Buffer = new Object[ObjTemp.length + increaseby];

		System.arraycopy(ObjTemp, 0, _Buffer, 0, ObjTemp.length);
		ObjTemp = null;
	}

	@Override
	public ResizingArray<T> clone() {
		final ResizingArray<T> CLONE = new ResizingArray<T>();

		CLONE._IncreaseBy = this._IncreaseBy;
		CLONE._Top = this._Top;

//        for(int X = 0; X < this._Top; X++) {
//            CLONE._Objects[X] = this._Objects[X].clone();
//        }

		CLONE._Buffer = this._Buffer.clone();

		return CLONE;
	}

	@Override
	public String toString() {
		return "Length=" + _Top;
	}

    public static void main(String[] args) {
        final ResizingArray<Integer> INTS = new ResizingArray<Integer>();

        INTS.puts(new Integer[] {9, 8, 7, 6, 5, 4, 3, 2, 1, 0, -1});

//        INTS.deleteAll();
        INTS.removeAll();
    }

}
////Resizing Array
//Item[] Items = null;
//int Top = 0;
//if(Items == null) {
//    Items = new Item[4];
//} else {
//    if(Top == Items.length) {
//        Item[] TempItems = Items;
//        Items = null;
//        Items = new Item[Top + 4];
//        System.arraycopy(TempItems, 0, Items, 0, TempItems.length);
//    }
//}
//Items[Top++] = Item;

