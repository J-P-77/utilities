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

package beta.utillib;

/**
 * Simple Push Pop
 * 
 */
public class LinkedList<T> {
	protected Node _Top = null;

//	protected int _Length = 0;

	public void push(T i) {
		final Node TEMP = new Node();
		TEMP._Value = i;
		TEMP._Next = _Top;

		_Top = TEMP;

//		_Length++;
	}

	public T pop() {
		final T TEMP = _Top._Value;

		_Top = _Top._Next;
//		_Length--;

		return TEMP;
	}

	public boolean isEmpty() {
		return _Top == null;
	}

//    public int length() {
//    	return _Length;
//    }

	protected class Node {
		public T _Value = null;
		public Node _Next = null;

		@Override
		public String toString() {
			return _Value.toString();
		}
	}

	public static void main(String[] args) {
		final LinkedList<Integer> LIST = new LinkedList<Integer>();

		for(int X = 0; X < 20; X++) {
			LIST.push(X);
		}

		while(!LIST.isEmpty()) {
			System.out.println(LIST.pop());
		}
	}
}