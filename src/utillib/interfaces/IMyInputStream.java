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

package utillib.interfaces;

import utillib.strings.MyStringBuffer;

import utillib.utilities.BitOperations.Byte_Ordering;

import java.io.IOException;

/**
 * 
 * @author Justin Palinkas
 */
public interface IMyInputStream extends IInputStream {
	public void setByteOrdering(Byte_Ordering bitordering);

	public Byte_Ordering getByteOrdering();

	public byte readByte() throws IOException;

	public int readBytes(byte[] buffer) throws IOException;

	public int readBytes(byte[] buffer, int offset, int length) throws IOException;

	public int readBytes(char[] buffer) throws IOException;

	public int readBytes(char[] buffer, int offset, int length) throws IOException;

	public int readBytes(int[] buffer) throws IOException;

	public int readBytes(int[] buffer, int offset, int length) throws IOException;

	/**
	 * Reads 1-byte per Read
	 * 
	 * @param buffer
	 * @return
	 * @throws IOException
	 */
	@Deprecated
	public int readChars(char[] buffer) throws IOException;

	@Deprecated
	public int readChars(char[] buffer, int offset, int length) throws IOException;

	/**
	 * Reads 1-bytes per Read
	 * 
	 * @param buffer
	 * @return
	 * @throws IOException
	 */
	@Deprecated
	public int readInts(int[] buffer) throws IOException;

	@Deprecated
	public int readInts(int[] buffer, int offset, int length) throws IOException;

	public String readString(int length) throws IOException;

	public int readString(StringBuffer buffer, int numtoread) throws IOException;

	public int readString(MyStringBuffer buffer, int numtoread) throws IOException;

	public String readln() throws IOException;

	public boolean readln(StringBuffer line) throws IOException;

	public boolean readln(MyStringBuffer line) throws IOException;

	public int readln(char[] buffer, int offset, int length) throws IOException;

	/**
	 * Reads 4-Bytes per Read
	 * 
	 * @param ints
	 * @throws IOException
	 */
	public int readIntsW(int[] buffer) throws IOException;

	public int readIntsW(int[] buffer, Byte_Ordering bitordering) throws IOException;

	public int readIntsW(int[] buffer, int offset, int length, Byte_Ordering bitordering) throws IOException;

	/**
	 * Reads 2-Bytes per Read
	 * 
	 * @param ints
	 * @throws IOException
	 */
	public int readCharsW(char[] buffer) throws IOException;

	public int readCharsW(char[] buffer, Byte_Ordering bitordering) throws IOException;

	public int readCharsW(char[] buffer, int offset, int length, Byte_Ordering bitordering) throws IOException;

	public short readShort() throws IOException;

	public short readShort(Byte_Ordering bitordering) throws IOException;

	public int readInt() throws IOException;

	public int readInt(Byte_Ordering bitordering) throws IOException;

	public long readLong() throws IOException;

	public long readLong(Byte_Ordering bitordering) throws IOException;

	public int readUBytes(short[] buffer, int offset, int length) throws IOException;

	public short readUByte() throws IOException;

	public int readUShort() throws IOException;

	public int readUShort(Byte_Ordering bitordering) throws IOException;

	public long readUInt() throws IOException;

	public long readUInt(Byte_Ordering bitordering) throws IOException;
}
