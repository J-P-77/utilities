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

package jp77.beta.utillib;

import java.util.Iterator;

public class LinkedListExtended<T> extends LinkedList<T> {

	public boolean exists(T i) {
		Node Top = _Top;
		while(Top != null) {
//			System.out.println(i.toString() + " - " + Top._Value.toString());

			if(i.equals(Top._Value)) {
				return true;
			}

			Top = Top._Next;
		}

		return false;
	}

	public T pop(T i) {
		Node Top = _Top;
		Node Pre = null;

		while(Top != null) {
//			System.out.println(i.toString() + " - " + Top._Value.toString());

			if(i.equals(Top._Value)) {
				final T TEMP = Top._Value;

//				System.out.println("equals");
				if(Pre == null) {
					_Top = Top._Next;
				} else {
					Pre._Next = Top._Next;
				}
//				_Length--;

				return TEMP;
			}

			Pre = Top;
			Top = Top._Next;
		}

		return null;
	}

	public void remove(T i) {
		Node Top = _Top;
		Node Pre = null;

		while(Top != null) {
//			System.out.println(i.toString() + " - " + Top._Value.toString());

			if(i.equals(Top._Value)) {
//				System.out.println("equals");
				if(Pre == null) {
					_Top = Top._Next;
				} else {
					Pre._Next = Top._Next;
				}
//				_Length--;
				break;
			}

			Pre = Top;
			Top = Top._Next;
		}
	}

	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private Node _T = _Top;

			@Override
			public boolean hasNext() {
				return _T != null;
			}

			@Override
			public T next() {
				final T TEMP = _T._Value;

				_T = _T._Next;

				return TEMP;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}
