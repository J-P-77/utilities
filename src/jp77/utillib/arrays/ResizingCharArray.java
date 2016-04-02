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
 * <b>Current Version 1.0.0</b>
 * 
 * November 16, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class ResizingCharArray {
	private char[] _Buffer = null;
	private int _Top = 0;
	private int _IncreaseBy = 5;

	public ResizingCharArray() {
		this(5);
	}

	public ResizingCharArray(int initalsize) {
		this(initalsize, 5);
	}

	public ResizingCharArray(int initalsize, int increaseby) {
		_Buffer = (initalsize < 1 ? new char[1] : new char[initalsize]);
		_IncreaseBy = (increaseby > 0 ? increaseby : 5);
	}

	public ResizingCharArray(int initalsize, char initalobject) {
		this(initalsize, 5, initalobject);
	}

	public ResizingCharArray(int initalsize, int increaseby, char initalobject) {
		this(initalsize, increaseby);

		put(initalobject);
	}

	public synchronized void put(char c) {
		if(_Top == _Buffer.length) {
			resize();
		}

		_Buffer[_Top++] = c;
	}

	public synchronized void put(char[] buffer) {
		put(buffer, 0, buffer.length);
	}

	public synchronized void put(char[] buffer, int offset, int length) {
		if((_Top + (length - offset)) >= _Buffer.length) {
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

	public synchronized char getAt(int index) {
		if(validIndex(index)) {
			return _Buffer[index];
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index);
		}
	}

	public synchronized void setAt(int index, char object) {
		if(validIndex(index)) {
			_Buffer[index] = object;
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index);
		}
	}

	public synchronized void removeAt(int index) {
		if(validIndex(index)) {
			for(int X = (index + 1); X < (_Top); X++) {
				_Buffer[index++] = _Buffer[X];
			}

			_Buffer[_Top - 1] = '\0';

			_Top = index;
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index + " Out Of Bounds");
		}
	}

	public synchronized boolean validIndex(int index) {
		if(index >= 0 && index < _Top) {
			return true;
		} else {
			return false;
		}
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

	public char[] getArray() {
		return _Buffer;
	}

	public char[] toArray() {
		final char[] TEMP = new char[_Top];

		System.arraycopy(_Buffer, 0, TEMP, 0, _Top);

		return TEMP;
	}

	private synchronized void resize() {
		resize(_IncreaseBy);
	}

	private synchronized void resize(int increaseby) {
		char[] ObjTemp = _Buffer;
		_Buffer = null;
		_Buffer = new char[ObjTemp.length + increaseby];

		System.arraycopy(ObjTemp, 0, _Buffer, 0, ObjTemp.length);
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

