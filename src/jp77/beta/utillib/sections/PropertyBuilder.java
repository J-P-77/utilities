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

package jp77.beta.utillib.sections;

import jp77.utillib.strings.MyStringBuffer;

public class PropertyBuilder {
	private MyStringBuffer _BuildString = null;
	private final String _SPACER;

	public PropertyBuilder(String intialstring, String spacer) {
		if(spacer == null) {
			throw new RuntimeException("Variable[spacer] - Is Null");
		}

		_SPACER = spacer;

		_BuildString = new MyStringBuffer(intialstring, _SPACER.length() + 16);
		_BuildString.append(_SPACER);
	}

	public PropertyBuilder(String intialstring, char spacer) {
		_SPACER = Character.toString(spacer);

		_BuildString = new MyStringBuffer(intialstring, 16);
		_BuildString.append(_SPACER);
	}

	public void add(char c) {
		_BuildString.append(c);
		_BuildString.append(_SPACER);
	}

	public void add(String str) {
		_BuildString.append(str);
		_BuildString.append(_SPACER);
	}

	public void add(StringBuffer str) {
		_BuildString.append(str);
		_BuildString.append(_SPACER);
	}

	public void add(MyStringBuffer str) {
		_BuildString.append(str);
		_BuildString.append(_SPACER);
	}

	public void add(PropertyBuilder str) {
		_BuildString.append(str.getResult());
		_BuildString.append(_SPACER);
	}

	public void reset() {
		_BuildString.reset();
	}

	public String getResult() {
		return toString();
	}

	@Override
	public String toString() {
		if(_BuildString.endsWith(_SPACER)) {
			_BuildString.reset(_BuildString.length() - _SPACER.length());
		}

		return _BuildString.toString();
	}
}
