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

package utillib.collections;

public class StringCollection<U> extends Collection<String, U> {
	private boolean _MatchCase = false;

	public StringCollection() {
		this(false);
	}

	public StringCollection(boolean matchcase) {
		_MatchCase = matchcase;
	}

	public void setMatchCase(boolean value) {
		_MatchCase = value;
	}

	public boolean getMatchCase() {
		return _MatchCase;
	}

	public String getKeyAt(int index) {
		final Entry E = super.getEntryAt(index);

		if(E != null) {
			return E.getKey();
		}

		return null;
	}

	public int keyPosition(String key) {
		if(key == null) {
			throw new RuntimeException("Variable[key] - Is Null");
		}

		for(int X = 0; X < super.count(); X++) {
			if(_MatchCase) {
				if(super.getEntryAt(X).getKey().equals(key)) {
					return X;
				}
			} else {
				if(super.getEntryAt(X).getKey().equalsIgnoreCase(key)) {
					return X;
				}
			}
		}

		return -1;
	}
}
