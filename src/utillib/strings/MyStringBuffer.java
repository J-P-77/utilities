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

/**
 * <pre>
 * <b>Current Version 1.0.2</b>
 * 
 * May 07, 2009 (Version 1.0.0)
 *     -First Released
 * 
 * July 21, 2009 (Version 1.0.1)
 *     -Added
 *         -Method reverse (Reverses Character Buffer)
 *         -Method getReverse (returns A Reverse String)
 * 
 * February 08, 2010 (Version 1.0.2)
 *     -Fixed Bug
 *         -Constructor (String, int) (Would Not Append Size)
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class MyStringBuffer implements Cloneable {
	private char[] _Chars = null;//Capacity = _Chars.length
	private int _Top = 0;
	private int _IncreaseBy = 16;

	private int _StoredLength = 0;

	private boolean _EraseOnFinalize = false;

	public MyStringBuffer() {
		this(16);
	}

	public MyStringBuffer(int initialcapacity) {
		this(initialcapacity, 16);
	}

	public MyStringBuffer(int initialcapacity, int increaseby) {
		_Chars = new char[initialcapacity];
		_IncreaseBy = increaseby;
	}

	public MyStringBuffer(byte[] str) {
		this(str, 0, str.length, 0);
	}

	public MyStringBuffer(byte[] str, int appendsize) {
		this(str, 0, str.length, appendsize);
	}

	public MyStringBuffer(byte[] str, int offset, int length) {
		this(str, offset, length, 0);
	}

	public MyStringBuffer(byte[] str, int offset, int length, int appendsize) {
		_Chars = new char[(length - offset) + appendsize];

		for(int X = offset; X < length; X++) {
			_Chars[_Top++] = (char)str[X];
		}
	}

	public MyStringBuffer(char[] str) {
		this(str, 0, str.length, 0);
	}

	public MyStringBuffer(char[] str, int appendsize) {
		this(str, 0, str.length, appendsize);
	}

	public MyStringBuffer(char[] str, int offset, int length) {
		this(str, offset, length, 0);
	}

	public MyStringBuffer(char[] str, int offset, int length, int appendsize) {
		_Chars = new char[(length - offset) + appendsize];

		for(int X = offset; X < length; X++) {
			_Chars[_Top++] = str[X];
		}
	}

	public MyStringBuffer(int[] str) {
		this(str, 0, str.length, 0);
	}

	public MyStringBuffer(int[] str, int appendsize) {
		this(str, 0, str.length, appendsize);
	}

	public MyStringBuffer(int[] str, int offset, int length) {
		this(str, offset, length, 0);
	}

	public MyStringBuffer(int[] str, int offset, int length, int appendsize) {
		_Chars = new char[(length - offset) + appendsize];

		for(int X = offset; X < length; X++) {
			_Chars[_Top++] = (char)str[X];
		}
	}

	public MyStringBuffer(String str) {
		this(str, 0, str.length(), 0);
	}

	public MyStringBuffer(String str, int appendsize) {
		this(str, 0, str.length(), appendsize);
	}

	public MyStringBuffer(String str, int offset, int length) {
		this(str, offset, length, 0);
	}

	public MyStringBuffer(String str, int offset, int length, int appendsize) {
		_Chars = new char[(length - offset) + appendsize];

		for(int X = offset; X < length; X++) {
			_Chars[_Top++] = str.charAt(X);
		}
	}

	public MyStringBuffer(StringBuffer str) {
		this(str, 0, str.length());
	}

	public MyStringBuffer(StringBuffer str, int appendsize) {
		this(str, 0, str.length(), appendsize);
	}

	public MyStringBuffer(StringBuffer str, int offset, int length) {
		this(str, 0, str.length(), 0);
	}

	public MyStringBuffer(StringBuffer str, int offset, int length, int appendsize) {
		_Chars = new char[(length - offset) - appendsize];

		for(int X = offset; X < length; X++) {
			_Chars[_Top++] = str.charAt(X);
		}
	}

	public MyStringBuffer(MyStringBuffer str) {
		this(str, 0, str.length(), 0);
	}

	public MyStringBuffer(MyStringBuffer str, int appendsize) {
		this(str, 0, str.length(), appendsize);
	}

	public MyStringBuffer(MyStringBuffer str, int offset, int length) {
		this(str, offset, length, 0);
	}

	public MyStringBuffer(MyStringBuffer str, int offset, int length, int appendsize) {
		_Chars = new char[(length - offset) + appendsize];

		for(int X = offset; X < length; X++) {
			_Chars[_Top++] = str.charAt(X);
		}
	}

	/**
	 * Help Information
	 * 
	 * @param index
	 *            (int) value index number
	 * @return returns character at index
	 */
	public char charAt(int index) {
		if((index < 0) || (index >= _Top)) {
			throw new StringIndexOutOfBoundsException(index);
		}

		return _Chars[index];
	}

	/**
	 * Help Information
	 * 
	 * @param index
	 *            (int) value index number
	 * @param c
	 *            (char) character to be set
	 */
	public void setCharAt(int index, char c) {
		if((index < 0) || (index >= _Top)) {
			throw new StringIndexOutOfBoundsException(index);
		}

		_Chars[index] = c;
	}

	public MyStringBuffer getSubstringBuffer(int beginIndex) {
		return new MyStringBuffer(_Chars, _Top);
	}

	public MyStringBuffer getSubstringBuffer(int beginIndex, int endIndex) {
		return new MyStringBuffer(_Chars, beginIndex, endIndex);
	}

	/**
	 * Help Information
	 * 
	 * @param beginIndex
	 *            (int) start of the substring
	 * @return returns String from beginIndex to end
	 */
	public String getSubString(int beginIndex) {
		return getSubString(beginIndex, _Top);
	}

	/**
	 * Help Information
	 * 
	 * @param beginIndex
	 *            (int) start of the substring
	 * @param endIndex
	 *            (int) end of the substring
	 * @return returns String from beginIndex to endIndex
	 */
	public String getSubString(int beginIndex, int endIndex) {
		if(beginIndex < 0) {
			throw new StringIndexOutOfBoundsException("Index: " + beginIndex + " - String: " + toString());
		}

		if(endIndex > _Top) {
			throw new StringIndexOutOfBoundsException("Index: " + endIndex + " - String: " + toString());
		}

		if(beginIndex > endIndex) {
			throw new StringIndexOutOfBoundsException("Index: " + (endIndex - beginIndex) + " - String: " + toString());
		}

		MyStringBuffer Buffer = new MyStringBuffer(endIndex - beginIndex);
		for(int X = beginIndex; X < endIndex; X++) {
			Buffer.append(_Chars[X]);
		}

		return Buffer.toString();
	}

	/**
	 * Help Information
	 * 
	 * @param beginIndex
	 *            (int) start of the substring
	 */
	public void substring(int beginIndex) {
		substring(beginIndex, _Top, false);
	}

	public void substring(int beginIndex, int endIndex) {
		substring(beginIndex, endIndex, false);
	}

	/**
	 * Help Information
	 * 
	 * @param beginIndex
	 *            (int) start of the substring
	 * @param endIndex
	 *            (int) end of the substring
	 * @param resizebuffer
	 *            (boolean) if true will Resize Internal Buffer to New
	 *            Length(endIndex - beginIndex)
	 */
	public void substring(int beginindex, int endindex, boolean resizebuffer) {
		if(beginindex < 0) {
			throw new StringIndexOutOfBoundsException("Index: " + beginindex + " - String: " + toString());
		}

		if(endindex > _Top) {
			throw new StringIndexOutOfBoundsException("Index: " + endindex + " - String: " + toString());
		}

		if(beginindex > endindex) {
			throw new StringIndexOutOfBoundsException("Index: " + (endindex - beginindex) + " - String: " + toString());
		}

		if(resizebuffer) {
			char[] Temp = new char[endindex - beginindex];

			_Top = 0;
			for(int X = beginindex; X < endindex; X++) {
				Temp[_Top++] = _Chars[X];
			}

			_Chars = null;
			_Chars = Temp;
			Temp = null;
		} else {
			_Top = 0;
			for(int X = beginindex; X < endindex; X++) {
				_Chars[_Top++] = _Chars[X];
			}
//            //You Don't Have To Add This
//            for(int X = _Top; X < _Chars.length; X++) {
//                _Chars[X] = '\0';
//            }
		}
	}

//
	/**
	 * Help Information
	 * 
	 * @param beginIndex
	 *            (int) start of the substring
	 * @param endIndex
	 *            (int) end of the substring
	 * @param appendcapacity
	 *            (boolean) will Resize Internal Buffer to New Length(endIndex -
	 *            beginIndex) plus appendcapacity
	 */
	public void substring(int beginIndex, int endIndex, int appendcapacity) {
		if(beginIndex < 0) {
			throw new StringIndexOutOfBoundsException("Index: " + beginIndex + " - String: " + toString());
		}

		if(endIndex > _Top) {
			throw new StringIndexOutOfBoundsException("Index: " + endIndex + " - String: " + toString());
		}

		if(beginIndex > endIndex) {
			throw new StringIndexOutOfBoundsException("Index: " + (endIndex - beginIndex) + " - String: " + toString());
		}

		char[] Temp = null;
		if(appendcapacity > 0) {
			Temp = new char[(endIndex - beginIndex) + appendcapacity];
		} else {
			Temp = new char[endIndex - beginIndex];
		}

		_Top = 0;
		for(int X = beginIndex; X < endIndex; X++) {
			Temp[_Top++] = _Chars[X];
		}

		_Chars = null;
		_Chars = Temp;
		Temp = null;
	}

//
	/**
	 * Help Information
	 * 
	 * @param ch
	 *            (int) start searches for char at
	 * @return returns first occurence of char
	 */
	public int indexOf(int ch) {
		return indexOf(ch, 0);
	}

	/**
	 * Help Information
	 * 
	 * @param ch
	 *            (int) start searches for char at
	 * @param fromIndex
	 *            (int) start searches for At fromIndex for char
	 * @return returns fromindex occurence of char
	 */
	public int indexOf(int c, int fromindex) {
		for(int X = fromindex; X < _Top; X++) {
			if(c == _Chars[X]) {
				return X;
			}
		}

		return -1;
	}

	public int indexOf(String str) {
		return indexOf(str, 0);
	}

	public int indexOf(String str, int fromindex) {
		if(str.length() == 0) {
			return -1;
		}//0

		int Pos = fromindex;
		int StrPos = 0;
		while((Pos = indexOf(str.charAt(StrPos), Pos)) != -1) {
			for(int X = Pos; X < _Top; X++) {
				if(_Chars[X] == str.charAt(StrPos)) {
					StrPos++;
				} else {
					break;
				}

				if(StrPos == str.length()) {
					return Pos;
				}
			}

			StrPos = 0;
			Pos++;
		}

		return -1;
	}

	public int lastIndexOf(int c) {
		return lastIndexOf(c, _Top - 1);
	}

	public int lastIndexOf(int c, int fromindex) {
		for(int X = fromindex; X > -1; X--) {
			if(c == _Chars[X]) {
				return X;
			}
		}

		return -1;
	}

	public boolean startsWith(char c) {
		if(_Top > 0) {
			return (_Chars[0] == c);
		}

		return false;
	}

	public boolean startsWith(String str) {
		return startsWith(str, 0);
	}

	public boolean startsWith(String str, int offset) {
		if(offset < 0) {
			return false;
		}

		int Pos = 0;
		for(int X = offset; X < _Top; X++) {
			if(str.charAt(Pos) == _Chars[X]) {
				if(++Pos == str.length()) {
					return true;
				}
			} else {
				return false;
			}
		}

		return false;
	}

	public boolean endsWith(char c) {
		if((_Top - 1) >= 0) {
			return (_Chars[_Top - 1] == c);
		}

		return false;
	}

	public boolean endsWith(String str) {
		return endsWith(str, _Top - str.length());
	}

	public boolean endsWith(String suffix, int offset) {
		return startsWith(suffix, offset);
	}

	public boolean contains(char c) {
		return (indexOf(c) != -1);
	}

	public boolean contains(String str) {
		return (indexOf(str) != -1);
	}

	public synchronized void append(char c) {
		if(_Top == _Chars.length) {
			increaseCapacity();
		}

		_Chars[_Top++] = c;
	}

	public synchronized void append(char c, boolean appendcapacity) {
		if(_Top == _Chars.length) {
			if(appendcapacity) {
				increaseCapacity();
			} else {
				increaseCapacity(1);
			}
		}

		_Chars[_Top++] = c;
	}

	public synchronized void append(char c, int appendsize) {
		if(_Top == _Chars.length) {
			increaseCapacity();
		} else {
			if(appendsize > 0) {
				increaseCapacity(1 + appendsize);
			}
		}

		_Chars[_Top++] = c;
	}

	public synchronized void append(String str) {
		append(str, true);
	}

	//If [appendsize] is false, then will only allocate the require size to fit string
	//else if true, will allocate string size plus [_IncreaseBy] size
	public synchronized void append(String str, boolean appendsize) {
		append(str, 0, str.length(), (appendsize ? _IncreaseBy : 0));
	}

	public synchronized void append(String str, int appendsize) {
		append(str, 0, str.length(), appendsize);
	}

	public synchronized void append(String str, int offset, int length) {
		append(str, offset, length, 0);
	}

	//If [appendcapacity] is false, then will only allocate the require size to fit string
	//else if true, will allocate string size plus [appendsize] size
	public synchronized void append(String str, int offset, int length, int appendsize) {
		if((_Top + length) > _Chars.length) {//>=
			increaseCapacity((length - (_Chars.length - _Top)) + (appendsize > 0 ? appendsize : 0));
		} else {
			if(appendsize > 0) {
				increaseCapacity(appendsize - ((_Chars.length - _Top) - length));
			}
		}

		for(int X = 0; X < length; X++) {
			_Chars[_Top++] = str.charAt(offset + X);
		}
	}

	public synchronized void append(StringBuffer str) {
		append(str, false);
	}

	//If appendsize is false, then will only allocate the require size to fit string
	//else if true, will allocate string size plus [_IncreaseBy] size
	public synchronized void append(StringBuffer str, boolean appendsize) {
		append(str, 0, str.length(), (appendsize ? _IncreaseBy : 0));
	}

	public synchronized void append(StringBuffer str, int appendsize) {
		append(str, 0, str.length(), appendsize);
	}

	public synchronized void append(StringBuffer str, int offset, int length) {
		append(str, offset, length, 0);
	}

	//If [appendcapacity] is false, then will only allocate the require size to fit string
	//else if true, will allocate string size plus [appendsize] size
	public synchronized void append(StringBuffer str, int offset, int length, int appendsize) {
		if((_Top + length) > _Chars.length) {//>=
			increaseCapacity((length - (_Chars.length - _Top)) + (appendsize > 0 ? appendsize : 0));
		} else {
			if(appendsize > 0) {
				increaseCapacity(appendsize - ((_Chars.length - _Top) - length));
			}
		}

		for(int X = 0; X < length; X++) {
			_Chars[_Top++] = str.charAt(offset + X);
		}
	}

	public synchronized void append(MyStringBuffer str) {
		append(str, false);
	}

	//If [appendsize] is false, then will only allocate the require size to fit string
	//else if true, will allocate string size plus _IncreaseBy size
	public synchronized void append(MyStringBuffer str, boolean appendsize) {
		append(str, 0, str.length(), (appendsize ? _IncreaseBy : 0));
	}

	public synchronized void append(MyStringBuffer str, int appendsize) {
		append(str, 0, str.length(), appendsize);
	}

	public synchronized void append(MyStringBuffer str, int offset, int length) {
		append(str, offset, length, 0);
	}

	//If [appendcapacity] is false, then will only allocate the require size to fit string
	//else if true, will allocate string size plus [appendsize] size
	public synchronized void append(MyStringBuffer str, int offset, int length, int appendsize) {
		if((_Top + length) > _Chars.length) {//>=
			increaseCapacity((length - (_Chars.length - _Top)) + (appendsize > 0 ? appendsize : 0));
		} else {
			if(appendsize > 0) {
				increaseCapacity(appendsize - ((_Chars.length - _Top) - length));
			}
		}

		for(int X = 0; X < length; X++) {
			_Chars[_Top++] = str.charAt(offset + X);
		}
	}

	public synchronized void append(char[] str) {
		append(str, 0);
	}

	public synchronized void append(char[] str, int appendsize) {
		append(str, 0, str.length, appendsize);
	}

	public synchronized void append(char[] str, boolean appendsize) {
		append(str, 0, str.length, (appendsize ? _IncreaseBy : 0));
	}

	public synchronized void append(char[] str, int offset, int length) {
		append(str, 0, str.length, 0);
	}

	public synchronized void append(char[] str, int offset, int length, int appendsize) {
		if((_Top + length) > _Chars.length) {//>=
			if(appendsize > 0) {
				increaseCapacity((length - (_Chars.length - _Top)) + appendsize);
			} else {
				increaseCapacity(length - (_Chars.length - _Top));//New
			}
		} else {
			if(appendsize > 0) {
				increaseCapacity(appendsize - ((_Chars.length - _Top) - length));
			}
		}

		for(int X = 0; X < length; X++) {
			_Chars[_Top++] = str[offset + X];
		}
	}

	public synchronized void append(byte c) {
		append((char)c);
	}

	public synchronized void append(byte c, boolean tostring) {
		if(tostring) {
			append(Byte.toString(c));
		} else {
			append((char)c);
		}
	}

	public synchronized void append(short c) {
		append((char)c);
	}

	public synchronized void append(short c, boolean tostring) {
		if(tostring) {
			append(Short.toString(c));
		} else {
			append((char)c);
		}
	}

	public synchronized void append(int c) {
		append((char)c);
	}

	public synchronized void append(int c, boolean tostring) {
		if(tostring) {
			append(Integer.toString(c));
		} else {
			append((char)c);
		}
	}

	public synchronized void append(long c) {
		append((char)c);
	}

	public synchronized void append(long c, boolean tostring) {
		if(tostring) {
			append(Long.toString(c));
		} else {
			append((char)c);
		}
	}

	public void concat(String str) {
		append(str);
	}

	public void replace(char oldChar, char newChar) {
		replace(0, oldChar, newChar);
	}

	public void replace(int offset, char oldChar, char newChar) {
		if(oldChar == newChar) {
			return;
		}

		for(int X = offset; X < _Top; X++) {
			if(_Chars[X] == oldChar) {
				_Chars[X] = newChar;
			}
		}
	}

	public void replace(String find, String replace) {
		if(find.length() == replace.length()) {
			if(find.equals(replace)) {
				return;
			}
		}

//        int Index = indexOf(find);
//
//        if(Index != -1) {
//            if(find.length() == replace.length()) {
//                for(int X = 0; X < replace.length(); X++) {
//                    _Chars[Index++] = replace.charAt(X);
//                }
//            } else {
//                remove(Index, Index + find.length() - 1);
//                insertAt(Index, replace);
//            }
//        }

		int Index = -1;

		while((Index = indexOf(find)) != -1) {
			remove(Index, Index + find.length() - 1);
			insertAt(Index, replace);
		}
	}

	public void replaceAll(String find, String replace) {
		if(find.length() == replace.length()) {
			if(find.equals(replace)) {
				return;
			}
		}

		int Index = -1;
		while((Index = indexOf(find)) != -1) {
			if(find.length() == replace.length()) {
				for(int X = 0; X < replace.length(); X++) {
					_Chars[Index++] = replace.charAt(X);
				}
			} else {
				remove(Index, Index + find.length() - 1);
				insertAt(Index, replace);
			}
		}
	}

	public char[] toCharArray() {
		return getChars();
	}

	public char[] getChars() {
		char[] Temp = new char[_Top];

		for(int X = 0; X < _Top; X++) {
			Temp[X] = _Chars[X];
		}

		return Temp;
	}

	public byte[] toByteArray() {
		return getBytes();
	}

	public byte[] getBytes() {
		byte[] Temp = new byte[_Top];

		for(int X = 0; X < _Top; X++) {
			Temp[X] = (byte)_Chars[X];
		}

		return Temp;
	}

	public int[] toIntArray() {
		return getInt();
	}

	public int[] getInt() {
		int[] Temp = new int[_Top];

		for(int X = 0; X < _Top; X++) {
			Temp[X] = _Chars[X];
		}

		return Temp;
	}

	/**
	 * Shifts All Chars. Does Not Delete Char Array Capacity
	 */
	public synchronized void remove(int index) {
		int Top = _Top;
		_Top = index;
		for(int X = (index + 1); X < Top; X++) {
			_Chars[_Top++] = _Chars[X];
		}
//        _Chars[_Top] = '\0';
	}

	/**
	 * Shifts All Chars. Does Not Delete Char Array Capacity
	 */
	public synchronized void remove(int fromindex, int toindex) {
		if(fromindex < 0) {
			throw new StringIndexOutOfBoundsException("Index: " + fromindex + " - String: " + toString());
		}

		if(toindex > _Top) {
			throw new StringIndexOutOfBoundsException("Index: " + toindex + " - String: " + toString());
		}

		if(fromindex > toindex) {
			throw new StringIndexOutOfBoundsException("Index: " + (toindex - fromindex) + " - String: " + toString());
		}

		int Top = _Top;
		_Top = fromindex;
		for(int X = (toindex + 1); X < Top; X++) {
			_Chars[_Top++] = _Chars[X];
		}
//        for(int X = _Top; X < _Chars.length; X++) {
//            _Chars[X] = '\0';
//        }
	}

	/**
	 * Deletes Char Array Capacity
	 */
	public synchronized void delete(int index) {
		int Top = _Top;
		_Top = 0;
		char[] TempChars = new char[Top - 1];

		for(int X = 0; X < index; X++) {
			TempChars[_Top++] = _Chars[X];
		}

		for(int X = (index + 1); X < Top; X++) {
			TempChars[_Top++] = _Chars[X];
		}

		_Chars = null;
		_Chars = TempChars;
		TempChars = null;
	}

	/**
	 * Deletes Char Array Capacity
	 */
	public synchronized void delete(int from, int to) {
		if(from < 0) {
			throw new StringIndexOutOfBoundsException("Index: " + from + " - String: " + toString());
		}

		if(to > _Top) {
			throw new StringIndexOutOfBoundsException("Index: " + to + " - String: " + toString());
		}

		if(from > to) {
			throw new StringIndexOutOfBoundsException("Index: " + (from - to) + " - String: " + toString());
		}

		int Top = _Top;
		_Top = 0;
		char[] TempChars = new char[Top - (to - from) - 1];

		for(int X = 0; X < from; X++) {
			TempChars[_Top++] = _Chars[X];
		}

		for(int X = (to + 1); X < Top; X++) {
			TempChars[_Top++] = _Chars[X];
		}

		_Chars = null;
		_Chars = TempChars;
		TempChars = null;
	}

	public synchronized void deleteAll() {
		delete(0, _Top);
	}

	public synchronized void erase(int index) {
		if(index < 0 || index > _Top) {
			throw new StringIndexOutOfBoundsException("Index: " + index + " - String: " + toString());
		}

		_Chars[index] = '\0';
	}

	public synchronized void eraseAll() {
		for(int X = 0; X < _Top; X++) {
			_Chars[X] = '\0';
		}
	}

	public synchronized int getBufferLength() {
		return _Chars.length;
	}

	public synchronized int length() {
		return _Top;
	}

	public void storeLength() {
		_StoredLength = _Top;
	}

	public int getStoredLength() {
		return _StoredLength;
	}

	public void reset() {
		_Top = 0;
	}

	public void reset(int to) {
		if(to < 0 || to > _Top) {
			throw new StringIndexOutOfBoundsException("Index: " + to + " - String: " + toString());
		}

		_Top = to;
	}

	public void resetToStoredLength() {
		reset(_StoredLength);
	}

	public synchronized void setIncreaseBy(int value) {
		_IncreaseBy = (value <= 0 ? 16 : value);
	}

	public synchronized int getIncreaseBy() {
		return _IncreaseBy;
	}

	public synchronized boolean validIndex(int index) {
		return (index >= 0 && index < _Top ? true : false);
	}

	public void insertAt(int index, char c) {
		if((_Top + 1) > _Chars.length) {
			increaseCapacity(1);
		}

		for(int X = (_Top - 1); X >= index; X--) {
			_Chars[X + 1] = _Chars[X];
		}

		_Chars[index] = c;

		_Top++;
	}

	public void insertAt(int index, String str) {
		if((_Top + str.length()) > _Chars.length) {
			increaseCapacity(str.length());
		}

		final int LENGTH = str.length();

		for(int X = (_Top - 1); X >= index; X--) {
			_Chars[X + LENGTH] = _Chars[X];
		}

		for(int X = 0; X < LENGTH; X++) {
			_Chars[index + X] = str.charAt(X);
		}

		_Top += LENGTH;
	}

	public void reverse() {
		int Top = _Top - 1;
		int Bottom = 0;

		while(Top > Bottom) {
			final char CTop = _Chars[Top];
			final char CBottom = _Chars[Bottom];

			_Chars[Bottom] = CTop;
			_Chars[Top] = CBottom;

			Bottom++;
			Top--;
		}
	}

	public String getReverse() {
		MyStringBuffer Buffer = new MyStringBuffer(_Top);

		for(int X = (_Top - 1); X != -1; X--) {
			Buffer.append(_Chars[X]);
		}

		return Buffer.toString();
	}

	public int trimStartCount() {
		return trimStartCount(" \t\n\r\0");
	}

	public int trimStartCount(String trimchars) {
		int TrimStart = 0;
		for(int X = 0; X < _Top; X++) {
			boolean Found = false;
			for(int Y = 0; Y < trimchars.length(); Y++) {
				if(_Chars[X] == trimchars.charAt(Y)) {
					//TrimStart++;
					Found = true;
					break;
				}
			}

			if(Found) {
				TrimStart++;
			} else {
				break;
			}
		}

		return TrimStart;
	}

	public int trimEndCount() {
		return trimEndCount(" \t\n\r\0");
	}

	public int trimEndCount(String trimchars) {
		int TrimEnd = 0;

		for(int X = (_Top - 1); X > -1; X--) {
			boolean Found = false;
			for(int Y = 0; Y < trimchars.length(); Y++) {
				if(_Chars[X] == trimchars.charAt(Y)) {
					//TrimEnd++;
					Found = true;
					break;
				}
			}

			if(Found) {
				TrimEnd++;
			} else {
				break;
			}
		}

		return TrimEnd;
	}

	public void trim() {
		trim(false);
	}

	public synchronized void trim(String trimstart, String trimend, boolean resizebuffer) {
		substring(trimStartCount(trimstart), _Top - trimEndCount(trimend), resizebuffer);
	}

	public synchronized void trim(boolean resizebuffer) {
		substring(trimStartCount(), _Top - trimEndCount(), resizebuffer);
	}

	public String getTrim() {
		return getSubString(trimStartCount(), _Top - trimEndCount());
	}

	public boolean willFit(int length) {
		return (_Top + length) <= _Chars.length;
	}

	public void newLength(int value) {
		if(value == _Chars.length) {
			return;
		} else if(value < 0) {
			return;
		} else {
			if(value > _Chars.length) {
				increaseCapacity(_Chars.length - value);
			} else {
				substring(0, value, true);
			}
		}
	}

	public void shinkToFit() {
		if(_Top < _Chars.length) {
			char[] Temp = new char[_Top];

			for(int X = 0; X < _Top; X++) {
				Temp[X] = _Chars[X];
			}

			_Chars = null;
			_Chars = Temp;
			Temp = null;
		}
	}

	public synchronized void increaseCapacity() {
		increaseCapacity(_IncreaseBy);
	}

	public synchronized void increaseCapacity(int increaseby) {
		char[] Temp = new char[_Chars.length + increaseby];

		for(int X = 0; X < _Top; X++) {
			Temp[X] = _Chars[X];
		}

		_Chars = null;
		_Chars = Temp;
		Temp = null;
	}

	public void allToLowerCase() {
		for(int X = 0; X < _Top; X++) {
			_Chars[X] = toLowerCase(_Chars[X]);
		}
	}

	public void allToUpperCase() {
		for(int X = 0; X < _Top; X++) {
			_Chars[X] = toLowerCase(_Chars[X]);
		}
	}

	public void toLowerLetter(int index) {
		if(index < 0 || index > _Top) {
			throw new StringIndexOutOfBoundsException("Index: " + index + " - String: " + toString());
		}

		_Chars[index] = toLowerCase(_Chars[index]);
	}

	public void toUpperLetter(int index) {
		if(index < 0 || index > _Top) {
			throw new StringIndexOutOfBoundsException("Index: " + index + " - String: " + toString());
		}

		_Chars[index] = toUpperCase(_Chars[index]);
	}

	public boolean equals(String anotherString) {
		if(_Top != anotherString.length()) {
			return false;
		}

		for(int X = 0; X < _Top; X++) {
			if(_Chars[X] != anotherString.charAt(X)) {
				return false;
			}
		}

		return true;
	}

	public boolean equals(StringBuffer anotherString) {
		if(_Top != anotherString.length()) {
			return false;
		}

		for(int X = 0; X < _Top; X++) {
			if(_Chars[X] != anotherString.charAt(X)) {
				return false;
			}
		}

		return true;
	}

	public boolean equals(MyStringBuffer anotherString) {
		if(_Top != anotherString.length()) {
			return false;
		}

		for(int X = 0; X < _Top; X++) {
			if(_Chars[X] != anotherString.charAt(X)) {
				return false;
			}
		}

		return true;
	}

	public boolean equalsIgnoreCase(String anotherString) {
		if(_Top != anotherString.length()) {
			return false;
		}

		int C1 = -1;
		int C2 = -1;
		for(int X = 0; X < _Top; X++) {
			C1 = _Chars[X];
			C2 = anotherString.charAt(X);

			if(C1 >= 'A' && C1 <= 'Z') {
				C1 -= 32;
			}//C is Upper Convert To Lower
			if(C2 >= 'A' && C2 <= 'Z') {
				C2 -= 32;
			}//C is Upper Convert To Lower

			if(C1 != C2) {
				return false;
			}
		}

		return true;
	}

	public boolean equalsIgnoreCase(StringBuffer anotherString) {
		if(_Top != anotherString.length()) {
			return false;
		}

		int C1 = -1;
		int C2 = -1;
		for(int X = 0; X < _Top; X++) {
			C1 = _Chars[X];
			C2 = anotherString.charAt(X);

			if(C1 >= 'A' && C1 <= 'Z') {
				C1 -= 32;
			}//C is Upper Convert To Lower
			if(C2 >= 'A' && C2 <= 'Z') {
				C2 -= 32;
			}//C is Upper Convert To Lower

			if(C1 != C2) {
				return false;
			}
		}

		return true;
	}

	public boolean equalsIgnoreCase(MyStringBuffer anotherString) {
		if(_Top != anotherString.length()) {
			return false;
		}

		int C1 = -1;
		int C2 = -1;
		for(int X = 0; X < _Top; X++) {
			C1 = _Chars[X];
			C2 = anotherString.charAt(X);

			if(C1 >= 'A' && C1 <= 'Z') {
				C1 -= 32;
			}//C is Upper Convert To Lower
			if(C2 >= 'A' && C2 <= 'Z') {
				C2 -= 32;
			}//C is Upper Convert To Lower

			if(C1 != C2) {
				return false;
			}
		}

		return true;
	}

	public void toUpperCase() {
		for(int X = 0; X < _Top; X++) {
			if(_Chars[X] >= 'A' && _Chars[X] <= 'Z') {
				_Chars[X] = (char)(_Chars[X] - 32);
			}
		}
	}

	public void toLowerCase() {
		for(int X = 0; X < _Top; X++) {
			if(_Chars[X] >= 'a' && _Chars[X] <= 'z') {
				_Chars[X] = (char)(_Chars[X] + 32);
			}
		}
	}

	public MyStringBuffer toBuffer() {
		return new MyStringBuffer(_Chars, 0, _Top);
	}

	@Override
	public String toString() {
		return new String(_Chars, 0, _Top);
	}

	public String toString(boolean reset) {
		final String STRING = new String(_Chars, 0, _Top);

		if(reset) {
			_Top = 0;
		}

		return STRING;
	}

	public String getUpperCase() {
		final char[] CHARS = new char[_Top];

		for(int X = 0; X < _Top; X++) {
			CHARS[X] = toUpperCase(_Chars[X]);
		}

		return new String(CHARS, 0, _Top);
	}

	public String getLowerCase() {
		final char[] CHARS = new char[_Top];

		for(int X = 0; X < _Top; X++) {
			CHARS[X] = toLowerCase(_Chars[X]);
		}

		return new String(CHARS, 0, _Top);
	}

	public boolean isWholeNumber() {
		if(_Chars == null || _Top == 0) {
			return false;
		} else {
			return StringUtil.isWholeNumber(_Chars);
		}
	}

	public boolean isNumber() {
		if(_Chars == null || _Top == 0) {
			return false;
		} else {
			return StringUtil.isNumber(_Chars);
		}
	}

	public void eraseOnFinalize() {
		_EraseOnFinalize = true;
	}

	@Override
	protected void finalize() throws Throwable {
		if(_EraseOnFinalize) {
			eraseAll();
		}

		_Chars = null;

		super.finalize();
	}

	@Override
	protected MyStringBuffer clone() throws CloneNotSupportedException {
		final MyStringBuffer CLONE = new MyStringBuffer();

		CLONE._Chars = this._Chars.clone();
		CLONE._EraseOnFinalize = this._EraseOnFinalize;
		CLONE._IncreaseBy = this._IncreaseBy;
		CLONE._StoredLength = this._StoredLength;
		CLONE._Top = this._Top;

		return CLONE;
	}

	/**
	 * Lower Convert To Upper (ONLY ASCII)
	 * 
	 * @param c
	 * @return
	 */
	public static int toUpperCase(int c) {
		if(c >= 'A' && c <= 'Z') {
			return (c - 32);
		} else {
			return c;
		}
	}

	/**
	 * Upper Convert To Lower (ONLY ASCII)
	 * 
	 * @param c
	 * @return
	 */
	public static int toLowerCase(int c) {
		if(c >= 'a' && c <= 'z') {
			return (c + 32);
		} else {
			return c;
		}
	}

	/**
	 * Lower Convert To Upper (ONLY ASCII)
	 * 
	 * @param c
	 * @return
	 */
	public static char toUpperCase(char c) {
		return (char)(toUpperCase((int)c));
	}

	/**
	 * Upper Convert To Lower (ONLY ASCII)
	 * 
	 * @param c
	 * @return
	 */
	public static char toLowerCase(char c) {
		return (char)(toLowerCase((int)c));
	}

	public static char toLowerCaseOther(char c) {
		return Character.toLowerCase(c);
	}

	public static char toUpperCaseOther(char c) {
		return Character.toUpperCase(c);
	}

//    public static void main(String[] args) {
//        MyStringBuffer Test1 = new MyStringBuffer("1", 16);
//
//        Test1.shinkToFit();
//    }
}

//public static final int LETTER_A_U = 65;
//public static final int LETTER_B_U = 66;
//public static final int LETTER_C_U = 67;
//public static final int LETTER_D_U = 68;
//public static final int LETTER_E_U = 69;
//public static final int LETTER_F_U = 70;
//public static final int LETTER_G_U = 71;
//public static final int LETTER_H_U = 72;
//public static final int LETTER_I_U = 73;
//public static final int LETTER_J_U = 74;
//public static final int LETTER_K_U = 75;
//public static final int LETTER_L_U = 76;
//public static final int LETTER_M_U = 77;
//public static final int LETTER_N_U = 78;
//public static final int LETTER_O_U = 79;
//public static final int LETTER_P_U = 80;
//public static final int LETTER_Q_U = 81;
//public static final int LETTER_R_U = 82;
//public static final int LETTER_S_U = 83;
//public static final int LETTER_T_U = 84;
//public static final int LETTER_U_U = 85;
//public static final int LETTER_V_U = 86;
//public static final int LETTER_W_U = 87;
//public static final int LETTER_X_U = 88;
//public static final int LETTER_Y_U = 89;
//public static final int LETTER_Z_U = 90;
//public static final int LETTER_A_L = 97;
//public static final int LETTER_B_L = 98;
//public static final int LETTER_C_L = 99;
//public static final int LETTER_D_L = 100;
//public static final int LETTER_E_L = 101;
//public static final int LETTER_F_L = 102;
//public static final int LETTER_G_L = 103;
//public static final int LETTER_H_L = 104;
//public static final int LETTER_I_L = 105;
//public static final int LETTER_J_L = 106;
//public static final int LETTER_K_L = 107;
//public static final int LETTER_L_L = 108;
//public static final int LETTER_M_L = 109;
//public static final int LETTER_N_L = 110;
//public static final int LETTER_O_L = 111;
//public static final int LETTER_P_L = 112;
//public static final int LETTER_Q_L = 113;
//public static final int LETTER_R_L = 114;
//public static final int LETTER_S_L = 115;
//public static final int LETTER_T_L = 116;
//public static final int LETTER_U_L = 117;
//public static final int LETTER_V_L = 118;
//public static final int LETTER_W_L = 119;
//public static final int LETTER_X_L = 120;
//public static final int LETTER_Y_L = 121;
//public static final int LETTER_Z_L = 122;

/*
 * public int trimStartCount() { return trimStartCount(' ', '\t', '\n', '\r',
 * '\0'); }
 * 
 * public int trimStartCount(char... trimchars) { int TrimStart = 0; for(int X =
 * 0; X < _Top; X++) { for(int Y = 0; Y < trimchars.length; Y++) { if(_Chars[X]
 * == trimchars[Y]) { TrimStart++; break; } }
 * 
 * // if(_Chars[X] == ' ' || _Chars[X] == '\t' || // _Chars[X] == '\n' ||
 * _Chars[X] == '\r' || _Chars[X] == '\0') { // // TrimStart++; // } else { //
 * break; // } }
 * 
 * return TrimStart; }
 * 
 * public int trimCount(int start, int end, char... trimchars) { int TrimCount =
 * 0;
 * 
 * for(int X = start; X < end; X++) { for(int Y = 0; Y < trimchars.length; Y++)
 * { if(_Chars[X] == trimchars[Y]) { TrimCount++; break; } } }
 * 
 * return TrimCount; }
 * 
 * // public int trimStartCount() { // int TrimStart = 0; // for(int X = 0; X <
 * _Top; X++) { // if(_Chars[X] == ' ' || _Chars[X] == '\t' || // _Chars[X] ==
 * '\n' || _Chars[X] == '\r' || _Chars[X] == '\0') { // // TrimStart++; // }
 * else { // break; // } // } // // return TrimStart; // }
 * 
 * public int trimEndCount() { return trimEndCount(' ', '\t', '\n', '\r', '\0');
 * }
 * 
 * public int trimEndCount(char... trimchars) { int TrimEnd = 0;
 * 
 * for(int X = (_Top - 1); X > -1; X--) { for(int Y = 0; Y < trimchars.length;
 * Y++) { if(_Chars[X] == trimchars[Y]) { TrimEnd++; break; } }
 * 
 * // if(_Chars[X] == ' ' || _Chars[X] == '\t' || // _Chars[X] == '\n' ||
 * _Chars[X] == '\r' || _Chars[X] == '\0') { // // TrimEnd++; // } else { //
 * break; // }
 * 
 * }
 * 
 * return TrimEnd; }
 * 
 * public void trim() { trim(false); }
 * 
 * public synchronized void trim(boolean resizebuffer) { final int TRIMSTART =
 * trimStartCount(); final int TRIMEND = trimEndCount();
 * 
 * // for(int X = 0; X < _Top; X++) { // if(_Chars[X] == ' ' || _Chars[X] ==
 * '\t' || // _Chars[X] == '\n' || _Chars[X] == '\r' || _Chars[X] == '\0') { //
 * // TrimStart++; // } else { // break; // } // } // // for(int X = (_Top - 1);
 * X > -1; X--) { // if(_Chars[X] == ' ' || _Chars[X] == '\t' || // _Chars[X] ==
 * '\n' || _Chars[X] == '\r' || _Chars[X] == '\0') { // // TrimEnd++; // } else
 * { // break; // } // }
 * 
 * substring(TRIMSTART, _Top - TRIMEND, resizebuffer); }
 */