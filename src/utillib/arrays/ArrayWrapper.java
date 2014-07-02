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

public class ArrayWrapper<T> {
	private T[] _Array = null;

	public ArrayWrapper(T[] array) {
		if(array == null) {
			throw new RuntimeException("Variable[array] - Is Null");
		}

		_Array = array;
	}

	public void set(int index, T obj) {
		_Array[index] = obj;
	}

	public T get(int index) {
		return _Array[index];
	}

	public int length() {
		return _Array.length;
	}

	public boolean contains(T obj) {
		return (indexOf(0, obj) != -1);
	}

	public int indexOf(T obj) {
		return indexOf(0, obj);
	}

	public int indexOf(int offset, T obj) {
		for(int X = offset; X < _Array.length; X++) {
			if(_Array[X] != null && _Array[X].equals(obj)) {
				return X;
			}
		}

		return -1;
	}
}
