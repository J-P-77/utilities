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

package jp77.utillib.arrays.buffer;

import jp77.utillib.arrays.ArraysUtil;

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
public class ResizingIntBuffer {
	private final Object __LOCK__ = new Object();

	private int[] _Buffer = null;
	private int _Top = 0;
	private int _IncreaseBy = 5;

	public ResizingIntBuffer() {
		this(5);
	}

	public ResizingIntBuffer(int initalsize) {
		this(initalsize, 5);
	}

	public ResizingIntBuffer(int initalsize, int increaseby) {
		_Buffer = (initalsize < 1 ? new int[1] : new int[initalsize]);
		_IncreaseBy = (increaseby > 0 ? increaseby : 5);
	}

	public void put(int object) {
		synchronized(__LOCK__) {
			if(_Top == _Buffer.length) {
				resize();
			}

			_Buffer[_Top++] = object;
		}
	}

	public void put(int[] buffer) {
		put(buffer, 0, buffer.length);
	}

	public void put(int[] buffer, int offset, int length) {
		synchronized(__LOCK__) {
			if((_Top + (length - offset)) > _Buffer.length) {
				resize(((length - offset) - (_Buffer.length - _Top)) + _IncreaseBy);
			}

			//len - offset = Number Of Objects To Copy
			//minus
			//_Objects.length - _Top = Number Of Current Free Objects
			//plus _IncreaseBy (Buffer Size)

			for(int X = 0; X < length; X++) {
				_Buffer[_Top++] = buffer[offset + X];
			}
		}
	}

	public int[] get(int length) {
//		synchronized(__LOCK__) {
		if(length < 0) {
			throw new RuntimeException("Variable[length] - Must Be Greater Than Zero");
		}

		final int[] TEMP = new int[length];

		get(TEMP, 0, TEMP.length);

		return TEMP;
//		}
	}

	public int get(int[] buffer) {
		return get(buffer, 0, buffer.length);
	}

	public int get(int[] buffer, int offset, int length) {
		synchronized(__LOCK__) {
			ArraysUtil.checkBufferBounds(buffer.length, offset, length);

			final int TO_MOVE = (_Top < length ? _Top : length);

			for(int X = 0; X < TO_MOVE; X++) {
				buffer[offset + X] = _Buffer[X];
				X++;
			}

			for(int X = 0; (TO_MOVE + X) < _Top; X++) {
				_Buffer[X] = _Buffer[TO_MOVE + X];
			}

			_Top -= TO_MOVE;

			return TO_MOVE;
		}
	}

//    public int get(int index) {
//        if(validIndex(index)) {
//            return _Buffer[index];
//        } else {
//            throw new IndexOutOfBoundsException("Index Number: " + index);
//        }
//    }
//    
//    public void set(int index, byte object) {
//        if(validIndex(index)) {
//            _Buffer[index] = object;
//        } else {
//            throw new IndexOutOfBoundsException("Index Number: " + index);
//        }
//    }
//    
//    public void remove(int index) {
//        if(validIndex(index)) {
//            for(int X = (index + 1); X < (_Top); X++) {
//                _Buffer[index++] = _Buffer[X];
//            }
//
//            _Buffer[_Top - 1] = -1;
//
//            _Top = index;
//        } else {
//            throw new IndexOutOfBoundsException("Index Number: " + index + " Out Of Bounds");
//        }
//    }

	public int available() {
		return _Buffer.length - _Top;
	}

	public void reset() {
		synchronized(__LOCK__) {
			_Top = 0;
		}
	}

	public void reset(int value) {
		synchronized(__LOCK__) {
			if(value >= 0 && value < _Buffer.length) {
				_Top = value;
			}
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
		int[] Temp = new int[_Top];

		System.arraycopy(_Buffer, 0, Temp, 0, _Top);

		return Temp;
	}

	private synchronized void resize() {
		resize(_IncreaseBy);
	}

	private synchronized void resize(int increaseby) {
		int[] Temp = _Buffer;
		_Buffer = null;
		_Buffer = new int[Temp.length + increaseby];

		System.arraycopy(Temp, 0, _Buffer, 0, Temp.length);
		Temp = null;
	}
}
