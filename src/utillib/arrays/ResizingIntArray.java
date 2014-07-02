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

package utillib.arrays;

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
public class ResizingIntArray {
	private int[] _Buffer = null;
	private int _Top = 0;
	private int _IncreaseBy = 5;

	public ResizingIntArray() {
		this(5);
	}

	public ResizingIntArray(int initalsize) {
		_Buffer = (initalsize < 1 ? new int[1] : new int[initalsize]);
	}

	public ResizingIntArray(int initalsize, int increaseby) {
		_Buffer = (initalsize < 1 ? new int[1] : new int[initalsize]);
		_IncreaseBy = (increaseby > 0 ? increaseby : 5);
	}

	public ResizingIntArray(int initalsize, int increaseby, int initalobject) {
		this(initalsize, increaseby);

		put(initalobject);
	}

	public synchronized void put(int object) {
		if(_Top == _Buffer.length) {
			resize();
		}

		_Buffer[_Top++] = object;
	}

	public synchronized void put(int[] buffer) {
		put(buffer, 0, buffer.length);
	}

	public synchronized void put(int[] buffer, int offset, int length) {
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

	public synchronized int get(int index) {
		if(validIndex(index)) {
			return _Buffer[index];
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index);
		}
	}

	public synchronized void set(int index, int object) {
		if(validIndex(index)) {
			_Buffer[index] = object;
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index);
		}
	}

	public synchronized void remove(int index) {
		if(validIndex(index)) {
			for(int X = (index + 1); X < (_Top); X++) {
				_Buffer[index++] = _Buffer[X];
			}

			_Buffer[_Top - 1] = -1;

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

	public int[] getArray() {
		return _Buffer;
	}

	public int[] toArray() {
		final int[] TEMP = new int[_Top];

		System.arraycopy(_Buffer, 0, TEMP, 0, _Top);

		return TEMP;
	}

	private synchronized void resize() {
		resize(_IncreaseBy);
	}

	private synchronized void resize(int increaseby) {
		int[] ObjTemp = _Buffer;
		_Buffer = null;
		_Buffer = new int[ObjTemp.length + increaseby];

		System.arraycopy(ObjTemp, 0, _Buffer, 0, ObjTemp.length);
	}
/*
    public static void main(String[] args) {
        ResizingIntArray Ints = new ResizingIntArray(1);
        
        int[] Arr = {0,1,2,3,4,5,6,7,8,9};

        Ints.put(Arr, 0, Arr.length);
        Ints.put(Arr, 0, Arr.length);
        Ints.put(Arr, 0, Arr.length);
        Ints.put(Arr, 0, Arr.length);
        Ints.put(Arr, 0, Arr.length);
        Ints.put(Arr, 0, Arr.length);
        Ints.put(Arr, 0, Arr.length);
        Ints.put(Arr, 0, Arr.length);
        Ints.put(Arr, 0, Arr.length);
    }
*/
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

