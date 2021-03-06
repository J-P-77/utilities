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

package jp77.utillib.utilities;

import jp77.utillib.strings.MyStringBuffer;

public class PercentString {
	final MyStringBuffer _BUFFER;

	public PercentString(String name) {
		this(name, 32);
	}

	public PercentString(String startstring, int intialsize) {
		_BUFFER = new MyStringBuffer(startstring.length() + intialsize);
		_BUFFER.append(startstring);
		_BUFFER.append(" - ");
		_BUFFER.storeLength();
		_BUFFER.append("0%");
	}

	public void setPercent(byte value) {
		_BUFFER.resetToStoredLength();
		_BUFFER.append(value, true);
		_BUFFER.append('%');
	}

	public void setPercent(int value) {
		_BUFFER.resetToStoredLength();
		_BUFFER.append(value, true);
		_BUFFER.append('%');
	}

	public void setPercent(long value) {
		_BUFFER.resetToStoredLength();
		_BUFFER.append(value, true);
		_BUFFER.append('%');
	}

	public String getString() {
		return _BUFFER.toString();
	}

	@Override
	public String toString() {
		return getString();
	}
}
