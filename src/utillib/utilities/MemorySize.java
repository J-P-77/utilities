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

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class MemorySize {
	public static final int MIN = -1;

	public static final int Bytes = 0;
	public static final int KiloBytes = 1;
	public static final int MegaBytes = 2;
	public static final int GigaBytes = 3;
	public static final int TeraBytes = 4;

	public static final int MAX = 5;

	private long _Size;
	private int _Type;
	private int _ConvertLimit;

	public MemorySize() {
		this(0, MemorySize.Bytes, 10);
	}

	public MemorySize(long size) {
		this(size, MemorySize.Bytes, 10);
	}

	public MemorySize(long size, int type, int limit) {
		_Size = size;
		_Type = type;
		_ConvertLimit = limit;
	}

	public void setSize(long size) {
		_Size = size;
	}

	public void addToSize(int size) {
		_Size += size;
	}

	public void addToSize(long size) {
		_Size += size;
	}

	public long getSize() {
		return _Size;
	}

	public int getType() {
		return _Type;
	}

	public long[] getsBoth() {
		long[] Both = {_Size, (long)_Type};

		return Both;
	}

	@Override
	public String toString() {
		switch(_Type) {
			case 0:
				return _Size + " in Byte(s)";//Bytes
			case 1:
				return _Size + " in Kilobyte(s)";//KiloBytes
			case 2:
				return _Size + " in Megabyte(s)";//MegaBytes
			case 3:
				return _Size + " in Gigabyte(s)";//GigaBytes
			case 4:
				return _Size + " in Terabyte(s)";//TeraBytes
			default:
				return _Size + " in UnKnown Size";//Bytes
		}
	}

	public void downConvert() {
		downConvert(_ConvertLimit);
	}

	public void downConvert(int limit) {
		if(limit != 0) {
			if(_Size > limit) {
				_Size = _Size / 1024;
				_Type--;
			}
		}
	}

	public void upConvert() {
		upConvert(_ConvertLimit);
	}

	public void upConvert(int limit) {
		if(limit != 0) {
			if(_Size > limit) {
				_Size = _Size * 1024;
				_Type++;
			}
		}
	}

	public void setConvertLimit(int limit) {
		_ConvertLimit = limit;
	}

	public static long IntegerLength(int value) {
		return Integer.toString(value).toCharArray().length;
	}

	public static long LongLength(long value) {
		return Long.toString(value).toCharArray().length;
	}

	public static long convertFromTo(int frommode, long size, int tomode) {
		char op = '\0';

		if(frommode > MIN && frommode < MAX && tomode > MIN && tomode < MAX && size != 0) {
			if(frommode > tomode) {
				op = '*';
			} else if(frommode < tomode) {
				op = '/';
			} else {
				return size;
			}

			long Result = math(op, size);

			return Result;

		} else {
			return 0;
		}
	}

	public static String convertBToKB(long size) {
		long SizeB = size;
		long SizeKB = SizeB / 1024;//KiloByte

		return SizeKB + "KB(s)";
	}

	public static String convertKBToMB(long size) {
		long SizeKB = size;//KiloByte
		long SizeMB = SizeKB / 1024;//MegaByte

		return SizeMB + " MB(s)";
	}

	public static String convertMBToGB(long size) {
		long SizeMB = size;//MegaByte
		long SizeGB = SizeMB / 1024;//GigaByte

		return SizeGB + " GB(s)";
	}

	public static double convertFromTo(int frommode, MemorySize size, int tomode) {
		return convertFromTo(frommode, size.getSize(), tomode);
	}

	private static long math(char op, long size) {
		if(op == '*') {
			return (size * 1024);
		} else if(op == '/') {
			return (size / 1024);
		} else {
			return size;
		}
	}

/*
    public static void main(String[] args) {
        long Size = 1;
            
        convertFromTo(MemorySize.GigaBytes,Size,MemorySize.MegaBytes);
    }
*/
}
