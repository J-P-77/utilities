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

package utillib.arrays.buffer;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 22, 2011 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public abstract class AFixedOutputByteBuffer {
	private final byte[] _BUFFER;
	private int _Top = 0;

	public AFixedOutputByteBuffer(int size) {
		_BUFFER = new byte[size];
	}

	public synchronized void reset() {
		_Top = 0;
	}

	public synchronized void reset(int value) {
		if(value >= 0 && value < _Top) {
			_Top = value;
		}
	}

	public synchronized void put(byte b) {
		if(_Top >= _BUFFER.length) {
			empty(_BUFFER, 0, _BUFFER.length);
			_Top = 0;
		}

		_BUFFER[_Top++] = b;
	}

	public synchronized void put(byte[] buffer) {
		put(buffer, 0, buffer.length);
	}

	public synchronized void put(byte[] buffer, int offset, int length) {
		if(length >= _BUFFER.length) {
			empty(_BUFFER, 0, _Top);
			empty(buffer, offset, length);
			_Top = 0;
		}

		if((_Top + length) >= _BUFFER.length) {
			empty(_BUFFER, 0, _Top);
			_Top = 0;
		}

		for(int X = 0; X < length; X++) {
			_BUFFER[_Top++] = buffer[X + offset];
		}
	}

	public synchronized void empty() {
		if(_Top > 0) {
			empty(_BUFFER, 0, _Top);
			_Top = 0;
		}
	}

	public synchronized int get(int index) {
		if(validIndex(index)) {
			return _BUFFER[index];
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index);
		}
	}

	public synchronized void set(int index, byte object) {
		if(validIndex(index)) {
			_BUFFER[index] = object;
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index);
		}
	}

//    public synchronized void remove(int index) {
//        if(validIndex(index)) {
//            for(int X = (index + 1); X < (_Top); X++) {
//            	_BUFFER[index++] = _BUFFER[X];
//            }
//
//            _BUFFER[_Top - 1] = -1;
//
//            _Top = index;
//        } else {
//            throw new IndexOutOfBoundsException("Index Number: " + index + " Out Of Bounds");
//        }
//    }

	public synchronized boolean validIndex(int index) {
		return (index >= 0 && index < _Top);
	}

	public int length() {
		return _Top;
	}

	public int getCapacity() {
		return _BUFFER.length;
	}

	public boolean isEmpty() {
		return _Top == 0;
	}

	public abstract void empty(byte[] buffer, int offset, int length);

/*
    public static void main(String[] args) {
        ResizingIntArray Bytes = new ResizingIntArray(1);
        
        int[] Arr = {0,1,2,3,4,5,6,7,8,9};

        Bytes.put(Arr, 0, Arr.length);
        Bytes.put(Arr, 0, Arr.length);
        Bytes.put(Arr, 0, Arr.length);
        Bytes.put(Arr, 0, Arr.length);
        Bytes.put(Arr, 0, Arr.length);
        Bytes.put(Arr, 0, Arr.length);
        Bytes.put(Arr, 0, Arr.length);
        Bytes.put(Arr, 0, Arr.length);
        Bytes.put(Arr, 0, Arr.length);
    }
*/
}
