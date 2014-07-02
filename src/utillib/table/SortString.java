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

package utillib.table;

import java.util.Comparator;

/**
 * November 27, 2008 (Version 1.0.0)<br>
 * -First Released<br>
 * <br>
 * 
 * @author Justin Palinkas<br>
 * <br>
 *         Current Version 1.0.0
 */

public class SortString implements Comparator {
	private boolean _MatchCase = false;

	public void matchCase(boolean value) {
		_MatchCase = value;
	}

	public int compare(Object o1, Object o2) {
		final String Str1 = (_MatchCase ? (String)o1 : ((String)o1).toLowerCase());
		final String Str2 = (_MatchCase ? (String)o2 : ((String)o2).toLowerCase());

		int Len1 = Str1.length();
		int Len2 = Str2.length();
		int Pos = 0;
		while(Pos < Len1 && Pos < Len2) {
			int C1 = Str1.charAt(Pos);
			int C2 = Str2.charAt(Pos);

			if(C1 < C2) {
				return -1;
			} else if(C1 > C2) {
				return 1;
			}

			Pos++;
		}

		return 0;
	}
}
