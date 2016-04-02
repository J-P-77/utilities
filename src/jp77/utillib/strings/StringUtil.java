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

package jp77.utillib.strings;

import jp77.utillib.arrays.ResizingArray;

import jp77.utillib.lang.byref.IntByRef;

import jp77.utillib.utilities.BitOperations;
import jp77.utillib.utilities.BitOperations.Byte_Ordering;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 27, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class StringUtil {
	public static final String[] _TRUE_ = {"true", "t", "yes", "1"};
	public static final String[] _FALSE_ = {"false", "f", "no", "0"};

	public static void skipWord(String str, IntByRef offset) {
		while(Character.isLetter(str.charAt(offset.value))) {
			offset.value++;
		}
	}

	public static void skipWholeNumber(String str, IntByRef offset) {
		while(Character.isDigit(str.charAt(offset.value))) {
			offset.value++;
		}
	}

	/**
	 * This Consumes Characters
	 * 
	 * @param str
	 * @param offset
	 * @return
	 */
	public static String getWord(String str, IntByRef offset) {
		final MyStringBuffer BUFFER = new MyStringBuffer();

		while(offset.value < str.length() && (Character.isLetter(str.charAt(offset.value)) || Character.isDigit(str.charAt(offset.value)))) {
			BUFFER.append(str.charAt(offset.value));
			offset.value++;
		}

		return BUFFER.toString();
	}

	public static int getIntValue(String str) {
		return getIntValue(str, 0, 0);
	}

	public static int getIntValue(String str, int offset) {
		return getIntValue(str, offset, 0);
	}

	public static int getIntValue(String str, int offset, int defaultvalue) {
		if(str == null || str.length() == 0) {
			return defaultvalue;
		}

		try {
			return Integer.parseInt(str);
		} catch(Exception e) {}

		return defaultvalue;
	}

	public static int getIntValue(String str, IntByRef offset) {
		return getIntValue(str, offset, 0);
	}

	/**
	 * This Consumes Characters
	 * 
	 * @param str
	 * @param offset
	 * @return
	 */
	public static int getIntValue(String str, IntByRef offset, int defaultvalue) {
		final MyStringBuffer BUFFER = new MyStringBuffer();

		if(str.charAt(offset.value) == '-') {
			BUFFER.append('-');
			offset.value++;
		}

		while(offset.value < str.length() && Character.isDigit(str.charAt(offset.value))) {
			BUFFER.append(str.charAt(offset.value));
			offset.value++;
		}

		return getIntValue(BUFFER.toString(), defaultvalue);
	}

	public static long getLongValue(String str) {
		return getLongValue(str, 0, 0);
	}

	public static long getLongValue(String str, int offset) {
		return getLongValue(str, offset, 0);
	}

	public static long getLongValue(String str, int offset, long defaultvalue) {
		if(str == null || str.length() == 0) {
			return defaultvalue;
		}

		try {
			return Long.parseLong(str);
		} catch(Exception e) {}

		return defaultvalue;

	}

	public static long getLongValue(String str, IntByRef offset) {
		return getLongValue(str, offset, 0);
	}

	/**
	 * This Consumes Characters
	 * 
	 * @param str
	 * @param offset
	 * @return
	 */
	public static long getLongValue(String str, IntByRef offset, long defaultvalue) {
		final MyStringBuffer BUFFER = new MyStringBuffer();

		if(str.charAt(offset.value) == '-') {
			BUFFER.append('-');
			offset.value++;
		}

		while(offset.value < str.length() && Character.isDigit(str.charAt(offset.value))) {
			BUFFER.append(str.charAt(offset.value));
			offset.value++;
		}

		return getLongValue(BUFFER.toString());
	}

	public static String repeatString(String str, int numoftimes) {
		final MyStringBuffer BUFFER = new MyStringBuffer(str.length() * numoftimes);

		repeatString(BUFFER, str, numoftimes);

		return BUFFER.toString();
	}

	public static void repeatString(MyStringBuffer str, String repeat, int numoftimes) {
		for(int X = 0; X < numoftimes; X++) {
			str.append(repeat);
		}
	}

	public static String repeatChar(char c, int numoftimes) {
		final MyStringBuffer BUFFER = new MyStringBuffer(1 * numoftimes);

		repeatChar(BUFFER, c, numoftimes);

		return BUFFER.toString();
	}

	public static void repeatChar(MyStringBuffer str, char c, int numoftimes) {
		for(int X = 0; X < numoftimes; X++) {
			str.append(c);
		}
	}

	public static boolean isPositiveWholeNumber(String str) {
		return isWholeNumber(str, 0, str.length(), false);
	}

	public static boolean isPositiveWholeNumber(String str, int offset, int length) {
		return isWholeNumber(str, offset, length, false);
	}

	public static boolean isPositiveWholeNumber(char[] str) {
		return isWholeNumber(str, 0, str.length, false);
	}

	public static boolean isPositiveWholeNumber(char[] str, int offset, int length) {
		return isWholeNumber(str, offset, length, false);
	}

	public static boolean isPositiveWholeNumber(MyStringBuffer str) {
		return isWholeNumber(str, 0, str.length(), false);
	}

	public static boolean isPositiveWholeNumber(MyStringBuffer str, int offset, int length) {
		return isWholeNumber(str, offset, length, false);
	}

	public static boolean isWholeNumber(String value) {
		return isWholeNumber(value, 0, value.length(), true);
	}

	public static boolean isWholeNumber(String value, boolean allownegative) {
		return isWholeNumber(value, 0, value.length(), allownegative);
	}

	public static boolean isWholeNumber(String str, int offset, int length) {
		return isWholeNumber(str, 0, str.length(), true);
	}

	public static boolean isWholeNumber(String str, int offset, int length, boolean allownegative) {
		if(str == null || str.length() == 0) {
			return false;
		} else {
			offset += StringUtil.skipsWhiteSpaces(str);

			if((str.charAt(offset) == '-' & allownegative) || str.charAt(0) == '+') {
				offset++;
			}

			for(int X = 0; X < str.length() && X < length; X++) {
				if(str.charAt(offset + X) == '.') {
					return false;
				} else if(!isDigit(str.charAt(offset + X))) {
					return false;
				}
			}

			return true;
		}
	}

	public static boolean isWholeNumber(char[] value) {
		return isWholeNumber(value, 0, value.length, true);
	}

	public static boolean isWholeNumber(char[] value, boolean allownegative) {
		return isWholeNumber(value, 0, value.length, allownegative);
	}

	public static boolean isWholeNumber(char[] str, int offset, int length) {
		return isWholeNumber(str, 0, str.length, true);
	}

	public static boolean isWholeNumber(char[] str, int offset, int length, boolean allownegative) {
		if(str == null || str.length == 0) {
			return false;
		} else {
			offset += StringUtil.skipsWhiteSpaces(str);

			if((str[offset] == '-' & allownegative) || str[0] == '+') {
				offset++;
			}

			for(int X = 0; X < str.length && X < length; X++) {
				if(str[offset + X] == '.') {
					return false;
				} else if(!isDigit(str[offset + X])) {
					return false;
				}
			}

			return true;
		}
	}

	public static boolean isWholeNumber(MyStringBuffer str) {
		return isWholeNumber(str, 0, str.length(), true);
	}

	public static boolean isWholeNumber(MyStringBuffer str, boolean allownegative) {
		return isWholeNumber(str, 0, str.length(), allownegative);
	}

	public static boolean isWholeNumber(MyStringBuffer str, int offset, int length) {
		return isWholeNumber(str, 0, str.length(), true);
	}

	public static boolean isWholeNumber(MyStringBuffer str, int offset, int length, boolean allownegative) {
		if(str == null || str.length() == 0) {
			return false;
		} else {
			offset += StringUtil.skipsWhiteSpaces(str);

			if((str.charAt(offset) == '-' & allownegative) || str.charAt(0) == '+') {
				offset++;
			}

			for(int X = 0; X < str.length() && X < length; X++) {
				if(str.charAt(offset + X) == '.') {
					return false;
				} else if(!isDigit(str.charAt(offset + X))) {
					return false;
				}
			}

			return true;
		}
	}

	public static boolean isPositiveNumber(String str) {
		return isWholeNumber(str, 0, str.length(), false);
	}

	public static boolean isPositiveNumber(String str, int offset, int length) {
		return isWholeNumber(str, offset, length, false);
	}

	public static boolean isPositiveNumber(char[] str) {
		return isWholeNumber(str, 0, str.length, false);
	}

	public static boolean isPositiveNumber(char[] str, int offset, int length) {
		return isWholeNumber(str, offset, length, false);
	}

	public static boolean isPositiveNumber(MyStringBuffer str) {
		return isWholeNumber(str, 0, str.length(), false);
	}

	public static boolean isPositiveNumber(MyStringBuffer str, int offset, int length) {
		return isWholeNumber(str, offset, length, false);
	}

	public static boolean isNumber(String str) {
		return isPositiveNumber(str, 0, str.length());
	}

	public static boolean isNumber(String str, int offset, int length) {
		return isNumber(str, offset, length);
	}

	public static boolean isNumber(String str, int offset, int length, boolean allownegative) {
		if(str == null || str.length() == 0) {
			return false;
		} else {
			offset += StringUtil.skipsWhiteSpaces(str);

			if((str.charAt(offset) == '-' & allownegative) || str.charAt(0) == '+') {
				offset++;
			}

			boolean FoundPeriod = false;
			for(int X = 0; X < str.length() && X < length; X++) {
				if(str.charAt(offset + X) == '.') {
					if(FoundPeriod) {
						return false;
					} else {
						FoundPeriod = true;
					}
				} else if(!isDigit(str.charAt(offset + X))) {
					return false;
				}
			}

			return true;
		}
	}

	public static boolean isNumber(char[] str) {
		return isPositiveNumber(str, 0, str.length);
	}

	public static boolean isNumber(char[] str, int offset, int length) {
		return isNumber(str, offset, length);
	}

	public static boolean isNumber(char[] str, int offset, int length, boolean allownegative) {
		if(str == null || str.length == 0) {
			return false;
		} else {
			offset += StringUtil.skipsWhiteSpaces(str);

			if((str[offset] == '-' & allownegative) || str[0] == '+') {
				offset++;
			}

			boolean FoundPeriod = false;
			for(int X = 0; X < str.length && X < length; X++) {
				if(str[offset + X] == '.') {
					if(FoundPeriod) {
						return false;
					} else {
						FoundPeriod = true;
					}
				} else if(!isDigit(str[offset + X])) {
					return false;
				}
			}

			return true;
		}
	}

	public static boolean isNumber(MyStringBuffer str) {
		return isPositiveNumber(str, 0, str.length());
	}

	public static boolean isNumber(MyStringBuffer str, int offset, int length) {
		return isNumber(str, offset, length);
	}

	public static boolean isNumber(MyStringBuffer str, int offset, int length, boolean allownegative) {
		if(str == null || str.length() == 0) {
			return false;
		} else {
			offset += StringUtil.skipsWhiteSpaces(str);

			if((str.charAt(offset) == '-' & allownegative) || str.charAt(0) == '+') {
				offset++;
			}

			boolean FoundPeriod = false;
			for(int X = 0; X < str.length() && X < length; X++) {
				if(str.charAt(offset + X) == '.') {
					if(FoundPeriod) {
						return false;
					} else {
						FoundPeriod = true;
					}
				} else if(!isDigit(str.charAt(offset + X))) {
					return false;
				}
			}

			return true;
		}
	}

	public static boolean isDigit(int c) {
		return Character.isDigit(c);
	}

	public static boolean isDigit(char c) {
		return Character.isDigit(c);
	}

	public static boolean isString(String str, String match) {
		return isString(str, match, 0);
	}

	public static boolean isString(String str, String match, int offset) {
		final IntByRef OFFSET = new IntByRef(offset);

		return isString(str, match, OFFSET);
	}

	public static boolean isString(String str, String match, IntByRef offset) {
		if(offset.value >= 0 && offset.value < str.length()) {
			int Counter = 0;
			for(int X = offset.value; X < str.length(); X++) {
				if(str.charAt(X) == match.charAt(Counter)) {
					if((++Counter) == match.length()) {
						return true;
					}
				} else {
					return false;
				}
			}
		}

		return false;
	}

	public static boolean isChar(String str, char match, IntByRef offset) {
		if(offset.value > 0 && offset.value < str.length()) {
			return str.charAt(offset.value) == match;
		}

		return false;
	}

	public static int skipsWhiteSpaces(String str) {
		return skipsWhiteSpaces(str, 0, str.length());
	}

	public static int skipsWhiteSpaces(String str, int offset) {
		return skipsWhiteSpaces(str, offset, str.length());
	}

	public static int skipsWhiteSpaces(String str, int offset, int length) {
		final IntByRef OFFSET = new IntByRef(offset);

		skipsWhiteSpaces(str, OFFSET, length);

		return OFFSET.value;
	}

	public static void skipsWhiteSpaces(String str, IntByRef offset) {
		skipsWhiteSpaces(str, offset, str.length());
	}

	public static void skipsWhiteSpaces(String str, IntByRef offset, int length) {
		for(; offset.value < length; offset.value++) {
			if(str.charAt(offset.value) == ' ') {
				continue;
			} else {
				break;
			}
		}
	}

	public static int skipsWhiteSpaces(char[] str) {
		return skipsWhiteSpaces(str, 0, str.length);
	}

	public static int skipsWhiteSpaces(char[] str, int offset) {
		return skipsWhiteSpaces(str, offset, str.length);
	}

	public static int skipsWhiteSpaces(char[] str, int offset, int length) {
		final IntByRef OFFSET = new IntByRef(offset);

		skipsWhiteSpaces(str, OFFSET, length);

		return OFFSET.value;
	}

	public static void skipsWhiteSpaces(char[] str, IntByRef offset) {
		skipsWhiteSpaces(str, offset, str.length);
	}

	public static void skipsWhiteSpaces(char[] str, IntByRef offset, int length) {
		for(; offset.value < length; offset.value++) {
			if(str[offset.value] == ' ') {
				continue;
			} else {
				break;
			}
		}
	}

	public static int skipsWhiteSpaces(MyStringBuffer str) {
		return skipsWhiteSpaces(str, 0, str.length());
	}

	public static int skipsWhiteSpaces(MyStringBuffer str, int offset) {
		return skipsWhiteSpaces(str, offset, str.length());
	}

	public static int skipsWhiteSpaces(MyStringBuffer str, int offset, int length) {
		final IntByRef OFFSET = new IntByRef(offset);

		skipsWhiteSpaces(str, OFFSET, length);

		return OFFSET.value;
	}

	public static void skipsWhiteSpaces(MyStringBuffer str, IntByRef offset) {
		skipsWhiteSpaces(str, offset, str.length());
	}

	public static void skipsWhiteSpaces(MyStringBuffer str, IntByRef offset, int length) {
		for(; offset.value < length; offset.value++) {
			if(str.charAt(offset.value) == ' ') {
				continue;
			} else {
				break;
			}
		}
	}

	public static String[] split(String str, char c) {
		return split(str, c, false);
	}

	public static String[] split(String str, char c, boolean trim) {
		return split(str, c, -1, trim);
	}

	public static String[] split(String str, char c, int limit) {
		return split(str, c, limit, false);
	}

	public static String[] split(String str, char c, int limit, boolean trim) {
		final ResizingArray<String> RESULTS = new ResizingArray<String>();
		final MyStringBuffer BUFFER = new MyStringBuffer();

		int Count = 0;
		for(int X = 0; X < str.length(); X++) {
			final char CHAR = str.charAt(X);

			if(CHAR == c && (Count < (limit - 1) || limit <= 0)) {
				RESULTS.put((trim ? BUFFER.getTrim() : BUFFER.toString()));
				BUFFER.reset();
				Count++;
			} else {
				BUFFER.append(CHAR);
			}
		}

		if(BUFFER.length() > 0) {
			RESULTS.put((trim ? BUFFER.getTrim() : BUFFER.toString()));
		}

		return RESULTS.toArray(new String[RESULTS.length()]);
	}

	public static int copyString(String str, byte[] outbuffer, int offset) {
		for(int X = 0; X < str.length() && (offset + X) < outbuffer.length; X++) {
			outbuffer[offset + X] = (byte)str.charAt(X);
		}

		return str.length();
	}

	public static int copyStringW(String str, byte[] outbuffer) {
		return copyStringW(str, outbuffer, 0);
	}

	public static int copyStringW(String str, byte[] outbuffer, int offset) {
		if(offset + str.length() * 2 > outbuffer.length) {
			throw new RuntimeException("Variable[outbuffer] - Buffer Not Large Enough");
		}

		for(int X = 0, Y = 0; X < str.length() && (offset + Y) < outbuffer.length; X++, Y += 2) {
			final byte[] BUFFER = BitOperations.shortToBytes((short)str.charAt(X), Byte_Ordering.BIG_ENDIAN);

			outbuffer[offset + Y] = BUFFER[0];
			outbuffer[offset + Y + 1] = BUFFER[1];
		}

		return (str.length() * 2);
	}

	public static int copyString(MyStringBuffer str, byte[] outbuffer, int offset) {
		for(int X = 0; X < str.length() && (offset + X) < outbuffer.length; X++) {
			outbuffer[offset + X] = (byte)str.charAt(X);
		}

		return str.length();
	}

	public static int copyStringW(MyStringBuffer str, byte[] outbuffer) {
		return copyStringW(str, outbuffer, 0);
	}

	public static int copyStringW(MyStringBuffer str, byte[] outbuffer, int offset) {
		if(offset + str.length() * 2 > outbuffer.length) {
			throw new RuntimeException("Variable[outbuffer] - Buffer Not Large Enough");
		}

		for(int X = 0, Y = 0; X < str.length() && (offset + Y) < outbuffer.length; X++, Y += 2) {
			final byte[] BUFFER = BitOperations.shortToBytes((short)str.charAt(X), Byte_Ordering.BIG_ENDIAN);

			outbuffer[offset + Y] = BUFFER[0];
			outbuffer[offset + Y + 1] = BUFFER[1];
		}

		return (str.length() * 2);
	}

	public static int copyString(StringBuffer str, byte[] outbuffer, int offset) {
		for(int X = 0; X < str.length() && (offset + X) < outbuffer.length; X++) {
			outbuffer[offset + X] = (byte)str.charAt(X);
		}

		return str.length();
	}

	public static int copyStringW(StringBuffer str, byte[] outbuffer) {
		return copyStringW(str, outbuffer, 0);
	}

	public static int copyStringW(StringBuffer str, byte[] outbuffer, int offset) {
		if(offset + str.length() * 2 > outbuffer.length) {
			throw new RuntimeException("Variable[outbuffer] - Buffer Not Large Enough");
		}

		for(int X = 0, Y = 0; X < str.length() && (offset + Y) < outbuffer.length; X++, Y += 2) {
			final byte[] BUFFER = BitOperations.shortToBytes((short)str.charAt(X), Byte_Ordering.BIG_ENDIAN);

			outbuffer[offset + Y] = BUFFER[0];
			outbuffer[offset + Y + 1] = BUFFER[1];
		}

		return (str.length() * 2);
	}

	public static int copyString(char[] str, byte[] outbuffer, int offset) {
		for(int X = 0; X < str.length && (offset + X) < outbuffer.length; X++) {
			outbuffer[offset + X] = (byte)str[X];
		}

		return str.length;
	}

	public static int copyStringW(char[] str, byte[] outbuffer) {
		return copyStringW(str, outbuffer, 0);
	}

	public static int copyStringW(char[] str, byte[] outbuffer, int offset) {
		if(offset + str.length * 2 > outbuffer.length) {
			throw new RuntimeException("Variable[outbuffer] - Buffer Not Large Enough");
		}

		for(int X = 0, Y = 0; X < str.length && (offset + Y) < outbuffer.length; X++, Y += 2) {
			final byte[] BUFFER = BitOperations.shortToBytes((short)str[X], Byte_Ordering.BIG_ENDIAN);

			outbuffer[offset + Y] = BUFFER[0];
			outbuffer[offset + Y + 1] = BUFFER[1];
		}

		return (str.length * 2);
	}

	public static boolean isBoolean(String value) {
		if(value == null || value.length() == 0) {
			return false;
		} else {
			final String VALUE = value.toLowerCase();
			for(int X = 0; X < _TRUE_.length; X++) {
				if(VALUE.equals(_TRUE_[X]) || VALUE.equals(_FALSE_[X])) {
					return true;
				}
			}

			return false;
		}
	}

	public static boolean getBooleanValue(String value) {
		return getBooleanValue(value, false);
	}

	public static boolean getBooleanValue(String value, boolean defaultvalue) {
		if(value == null || value.length() == 0) {
			return defaultvalue;
		} else {
			final String VALUE = value.toLowerCase();
			for(int X = 0; X < _TRUE_.length; X++) {
				if(VALUE.equals(_TRUE_[X])) {
					return true;
				} else if(VALUE.equals(_FALSE_[X])) {
					return false;
				}
			}

			return defaultvalue;
		}
	}

//65 - 90 Lower
//97 - 122 Upper

	public static String getString(byte[] buffer) {
		return new String(buffer);
	}

	public static String getString(byte[] buffer, int offset, int length) {
		return new String(buffer, offset, length);
	}

	public static String getString(int[] buffer, int offset, int length) {
		return new String(buffer, offset, length);
	}
}
