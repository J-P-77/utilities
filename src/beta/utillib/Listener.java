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

package beta.utillib;

import utillib.arrays.ResizingArray;

/**
 * 
 * @author Dalton Dell
 */
public class Listener<T> {
	protected final Object __LOCK__ = new Object();

	protected final ResizingArray<T> _LISTENERS;

	public Listener() {
		_LISTENERS = new ResizingArray<T>(0, 1);
	}

	public Listener(int initalsize) {
		this(initalsize, 1);
	}

	public Listener(int initalsize, int increaseby) {
		_LISTENERS = new ResizingArray<T>(initalsize, increaseby);
	}

	public void addListener(T listener) {
		if(listener == null) {
			throw new RuntimeException("Variable[listener] - Is Null");
		}

		if(!listenerExists(listener)) {
			synchronized(__LOCK__) {
				_LISTENERS.put(listener);
			}
		}
	}

	public T getListenerAt(int index) {
		synchronized(__LOCK__) {
			if(_LISTENERS.validIndex(index)) {
				return _LISTENERS.getAt(index);
			}
		}

		return null;
	}

	public void removeListener(int index) {
		synchronized(__LOCK__) {
			if(_LISTENERS.validIndex(index)) {
				_LISTENERS.removeAt(index);
			}
		}
	}

	public void removeListener(T listener) {
		if(listener == null) {
			throw new RuntimeException("Variable[listener] - Is Null");
		}

		synchronized(__LOCK__) {
			_LISTENERS.removeAll(listener);
		}
	}

	public int listenerCount() {
		synchronized(__LOCK__) {
			return _LISTENERS.length();
		}
	}

	public boolean listenerExists(T listener) {
		if(listener == null) {
			throw new RuntimeException("Variable[listener] - Is Null");
		}

		synchronized(__LOCK__) {
			for(int X = 0; X < _LISTENERS.length(); X++) {
				if(listener.equals(_LISTENERS.getAt(X))) {
					return true;
				}
			}
		}

		return false;
	}
}
