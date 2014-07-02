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

package utillib.strings;

import java.io.IOException;
import java.io.InputStream;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 09, 2008 (version 1.0.0)
 *     -First Released
 * 
 * November 09, 2008 (version 1.0.0)
 *     -Added
 *         -Constructor StringDocument(InputStream instream)
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
@Deprecated
public class StringDocument {
	private char _Previous = '\0';
	private char _Current = '\0';
	private char _Next = '\0';

	private final StringBuffer _BUFFER;
	private int _Pos = 0;

	public StringDocument(char c) {
		_BUFFER = new StringBuffer();
		_BUFFER.append(c);

		move();
	}

	public StringDocument(char[] chars) {
		this(chars, 0, chars.length);
	}

	public StringDocument(char[] chars, int offset, int length) {
		_BUFFER = new StringBuffer(length);

		for(int X = offset; X < length; X++) {
			_BUFFER.append(chars[X]);
		}

		move();
	}

	public StringDocument(int i) {
		_BUFFER = new StringBuffer();
		_BUFFER.append((char)i);

		move();
	}

	public StringDocument(int[] ints) {
		this(ints, 0, ints.length);
	}

	public StringDocument(int[] ints, int offset, int length) {
		_BUFFER = new StringBuffer(length);

		for(int X = offset; X < length; X++) {
			_BUFFER.append((char)ints[X]);
		}

		move();
	}

	public StringDocument(String str) {
		this(str, 0, str.length());
	}

	public StringDocument(String str, int offset, int length) {
		_BUFFER = new StringBuffer(length);

		for(int X = offset; X < length; X++) {
			_BUFFER.append(str.charAt(X));
		}

		move();
	}

	public StringDocument(StringBuffer buffer) {
		_BUFFER = buffer;
	}

	public StringDocument(InputStream instream, int intiallength) throws IOException {
		_BUFFER = new StringBuffer(intiallength);

		int C = -1;
		while((C = instream.read()) != -1) {
			_BUFFER.append((char)C);
		}

		move();
	}

	public char previous() {
		return _Previous;
	}

	public char current() {
		return _Current;
	}

	public char next() {
		return _Next;
	}

	public boolean hasMoreChars() {
		return _Next != '\0';
	}

	public void plus() {
		_Pos++;

		move();
	}

	public void minus() {
		_Pos--;

		move();
	}

	public void seek(int pos) {
		if(pos >= 0 && pos < _BUFFER.length()) {
			_Pos = pos;

			move();
		}
	}

	private void move() {
		if((_Pos - 1) > -1 && (_Pos - 1) < _BUFFER.length()) {
			_Previous = _BUFFER.charAt(_Pos - 1);
		} else {
			_Previous = '\0';
		}

		if(_Pos < _BUFFER.length()) {
			_Current = _BUFFER.charAt(_Pos);
		} else {
			_Current = '\0';
		}

		if((_Pos + 1) < _BUFFER.length()) {
			_Next = _BUFFER.charAt(_Pos + 1);
		} else {
			_Next = '\0';
		}
	}
}
