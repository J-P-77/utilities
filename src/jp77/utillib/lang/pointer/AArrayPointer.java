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

package jp77.utillib.lang.pointer;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * April 12, 2010 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public abstract class AArrayPointer {
	protected int _Pointer = 0;

	public void increment() {
		_Pointer++;
	}

	public void increment(int value) {
		_Pointer += value;
	}

	public void decrement() {
		--_Pointer;
	}

	public void decrement(int value) {
		_Pointer -= value;
	}

	public void reset() {
		_Pointer = 0;
	}

	public void reset(int value) {
		if(value < 0 || value >= length()) {
			throw new RuntimeException("Variable[value] - Index Out Of Bounds: " + value);
		}

		_Pointer = value;
	}

	public boolean hasNext() {
		return (_Pointer < length());
	}

	public boolean hasPrevious() {
		return (_Pointer > 0);
	}

	public void newLength(int length) {
		newLength(length, true);
	}

	public int getPointer() {
		return _Pointer;
	}

	public abstract int length();

	public abstract void newLength(int length, boolean preserve);

//    public abstract Object get();
//    public abstract void set(Object value);

//    public abstract Object get(int index);
//    public abstract void set(int index), Object value);
}
