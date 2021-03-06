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

package jp77.utillib.security;

import java.lang.reflect.Field;

public class SecureString {
	private String _Str = null;

	public SecureString(String str) {
		_Str = str;
	}

	public String get() {
		return _Str;
	}

	public void erase() {
		try {
			final Field F_VALUE = String.class.getDeclaredField("value");
			F_VALUE.setAccessible(true);

			final char[] VALUE = (char[])F_VALUE.get(_Str);

			for(int X = 0; X < VALUE.length; X++) {
				VALUE[X] = '\0';
			}
		} catch(SecurityException e) {
			throw new RuntimeException("SecurityManager blocked erase");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return _Str;
	}

	@Override
	protected void finalize() throws Throwable {
		erase();
	}
}
