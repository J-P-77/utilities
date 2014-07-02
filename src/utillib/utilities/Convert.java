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

package utillib.utilities;

import utillib.strings.MyStringBuffer;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 06, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class Convert {

	public static String arrayToString(byte[] buffer) {
		return arrayToString(buffer, 0, buffer.length);
	}

	public static String arrayToString(byte[] buffer, int offset, int length) {
		final MyStringBuffer BUFFER = new MyStringBuffer(buffer.length);

		for(int X = 0; X < length; X++) {
			BUFFER.append((char)buffer[offset + X]);
		}

		return BUFFER.toString();
	}

	public static String arrayToString(short[] buffer) {
		return arrayToString(buffer, 0, buffer.length);
	}

	public static String arrayToString(short[] buffer, int offset, int length) {
		final MyStringBuffer BUFFER = new MyStringBuffer(buffer.length);

		for(int X = 0; X < length; X++) {
			BUFFER.append((char)buffer[offset + X]);
		}

		return BUFFER.toString();
	}

	public static String arrayToString(char[] buffer) {
		return arrayToString(buffer, 0, buffer.length);
	}

	public static String arrayToString(char[] buffer, int offset, int length) {
		final MyStringBuffer BUFFER = new MyStringBuffer(buffer.length);

		for(int X = 0; X < length; X++) {
			BUFFER.append((char)buffer[offset + X]);
		}

		return BUFFER.toString();
	}

	public static String arrayToString(int[] buffer) {
		return arrayToString(buffer, 0, buffer.length);
	}

	public static String arrayToString(int[] buffer, int offset, int length) {
		final MyStringBuffer BUFFER = new MyStringBuffer(buffer.length);

		for(int X = 0; X < length; X++) {
			BUFFER.append((char)buffer[offset + X]);
		}

		return BUFFER.toString();
	}

	public static String toHex(byte[] bytes) {
		return toHex(bytes, false);
	}

	public static String toHex(byte[] bytes, boolean uppercase) {
		final MyStringBuffer BUFFER = new MyStringBuffer(2 * bytes.length);

		for(int X = 0; X < bytes.length; X++) {
			int J = (bytes[X] & 0xf0) >> 4;
			int K = bytes[X] & 0xf;

			char C1 = (char)(J <= 9 ? 48 + J : (97 + J) - 10);
			char C2 = (char)(K <= 9 ? 48 + K : (97 + K) - 10);

			if(uppercase) {
				C1 = Character.toUpperCase(C1);
				C2 = Character.toUpperCase(C2);
			}

			BUFFER.append(C1);
			BUFFER.append(C2);
		}

		return BUFFER.toString();
	}

	public static String toHex(boolean uppercase, byte... bytes) {
		final MyStringBuffer BUFFER = new MyStringBuffer(2 * bytes.length);

		for(int X = 0; X < bytes.length; X++) {
			int J = (bytes[X] & 0xf0) >> 4;
			int K = bytes[X] & 0xf;

			char C1 = (char)(J <= 9 ? 48 + J : (97 + J) - 10);
			char C2 = (char)(K <= 9 ? 48 + K : (97 + K) - 10);

			if(uppercase) {
				C1 = Character.toUpperCase(C1);
				C2 = Character.toUpperCase(C2);
			}

			BUFFER.append(C1);
			BUFFER.append(C2);
		}

		return BUFFER.toString();
	}

	public static String toHex(boolean add0x, boolean uppercase, byte... bytes) {
		return (add0x ? "0x" + toHex(uppercase, bytes) : toHex(uppercase, bytes));
	}

	public static String convertBytes(long sizebytes) {
		return convertBytes(sizebytes, 10);
	}

	public static String convertBytes(long sizebytes, int limit) {
		long SizeKB = sizebytes / 1024;//KiloByte
		long SizeMB = SizeKB / 1024;//MegaByte
		long SizeGB = SizeMB / 1024;//GigaByte
		long SizeTB = SizeGB / 1024;//TeraByte

		if(SizeTB >= limit) {
			return SizeTB + "tb";
		} else if(SizeGB >= limit) {
			return SizeGB + "gb";
		} else if(SizeMB >= limit) {
			return SizeMB + "mb";
		} else if(SizeKB >= limit) {
			return SizeKB + "kb";
		} else {
			return sizebytes + "b";
		}
	}

	public static int strToMillis(String value) {
		if(value == null || value.equals("") || value.startsWith("-")) {
			return -1;
		} else {
			try {
				StringBuffer T_Number = new StringBuffer(16);
				StringBuffer T_Type = new StringBuffer(8);

				boolean isDigit = true;
				for(int X = 0; X < value.length(); X++) {
					char C = value.charAt(X);

					if(Character.isDigit(C) && isDigit) {
						T_Number.append(C);
					} else {
						T_Type.append(C);
						isDigit = false;
					}
				}

				int Number = Integer.parseInt(T_Number.toString());
				String Type = T_Type.toString().toLowerCase();

				if(Type.startsWith("ms") || Type.startsWith("millis") || Type.startsWith("milli")) {
					return Number;
				} else if(Type.startsWith("sec") || Type.startsWith("second")) {
					return (Number * 1000);
				} else if(Type.startsWith("min") || Type.startsWith("minute")) {
					return (Number * 1000) * 60;
				} else if(Type.equals("hr") || Type.startsWith("hour")) {
					return ((Number * 1000) * 60) * 60;
				}
			} catch(Exception e) {}

			return -1;
		}
	}

	public static int convertStrToMillis_Beta(String value) {
		return strTo_Beta(value, new Pattern(1, "ms", "millis", "milli"), new Pattern(1000, "sec", "second"), new Pattern(1000 * 60, "min", "minute"), new Pattern(1000 * 60 * 60, "hr", "hour"));
	}

	public static int strToBytes_Beta(String value) {
		return strTo_Beta(value, new Pattern(1, "b", "byte"), new Pattern(1024, "kb", "kilobyte"), new Pattern(1024 * 1024, "mb", "megabyte"));
	}

	private static int strTo_Beta(String value, Pattern... pattern) {
		if(value == null || value.equals("") || value.startsWith("-")) {
			return -1;
		}

		try {
			StringBuffer TempNumber = new StringBuffer(16);
			StringBuffer TempType = new StringBuffer(8);

			boolean isDigit = true;
			for(int X = 0; X < value.length(); X++) {
				char C = value.charAt(X);

				if(Character.isDigit(C) && isDigit) {
					TempNumber.append(C);
				} else {
					TempType.append(C);
					isDigit = false;
				}
			}

			int Number = Integer.parseInt(TempNumber.toString());
			String Type = TempType.toString().toLowerCase();

			for(int X = 0; X < pattern.length; X++) {
				for(int Y = 0; Y < pattern[X].patternCount(); Y++) {
					if(Type.equalsIgnoreCase(pattern[X].getPattern(Y))) {
						return Number * pattern[X].getMultiplier();
					}
				}
			}

		} catch(Exception e) {}

		return -1;
	}

	private static class Pattern {
		private final int _MULTIPLIER;
		private final String[] _PATTERNS;

		public Pattern(int multiplier, String... patterns) {
			_MULTIPLIER = multiplier;
			_PATTERNS = patterns;
		}

		public int getMultiplier() {
			return _MULTIPLIER;
		}

		public String getPattern(int index) {
			return _PATTERNS[index];
		}

		public int patternCount() {
			return _PATTERNS.length;
		}
	}
}
