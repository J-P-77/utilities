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

package jp77.utillib.interfaces;

import jp77.utillib.file.FileUtil.Line_Ending;
import jp77.utillib.strings.MyStringBuffer;

import jp77.utillib.utilities.BitOperations.Byte_Ordering;

import java.io.IOException;

/**
 * 
 * @author Justin Palinkas
 */
public interface IMyOutputStream extends IOutputStream {
	public void setLineEnding(Line_Ending lineending);

	public Line_Ending getLineEnding();

	public void setByteOrdering(Byte_Ordering bitordering);

	public Byte_Ordering getByteOrdering();

	public void writeByte(byte b) throws IOException;

	public void writeShort(short value) throws IOException;

	public void writeShort(short value, Byte_Ordering bitordering) throws IOException;

	public void writeInt(int value) throws IOException;

	public void writeInt(int value, Byte_Ordering bitordering) throws IOException;

	public void writeChar(char value) throws IOException;

	public void writeLong(long value) throws IOException;

	public void writeLong(long value, Byte_Ordering bitordering) throws IOException;

	public void writeBytes(byte[] buffer) throws IOException;

	public void writeBytes(byte[] buffer, int offset, int length) throws IOException;

	public void writeBytes(char[] buffer) throws IOException;

	public void writeBytes(char[] buffer, int offset, int length) throws IOException;

	public void writeBytes(int[] buffer) throws IOException;

	public void writeBytes(int[] buffer, int offset, int length) throws IOException;

	@Deprecated
	public void writeInts(int[] buffer) throws IOException;

	@Deprecated
	public void writeInts(int[] buffer, int offset, int length) throws IOException;

	@Deprecated
	public void writeChars(char[] buffer) throws IOException;

	@Deprecated
	public void writeChars(char[] buffer, int offset, int length) throws IOException;

	/**
	 * Writes 4-Bytes per Write
	 * 
	 * @param ints
	 * @throws IOException
	 */
	public void writeIntsW(int[] buffer) throws IOException;

	public void writeIntsW(int[] buffer, Byte_Ordering bitordering) throws IOException;

	public void writeIntsW(int[] buffer, int offset, int length, Byte_Ordering bitordering) throws IOException;

	/**
	 * Writes 4-Bytes per Write
	 * 
	 * @param ints
	 * @throws IOException
	 */
	public void writeCharsW(char[] buffer) throws IOException;

	public void writeCharsW(char[] buffer, Byte_Ordering bitordering) throws IOException;

	public void writeCharsW(char[] buffer, int offset, int length, Byte_Ordering bitordering) throws IOException;

	/*
	 * public void writeLongs(long[] longs) throws IOException; public void
	 * writeLongs(long[] longs, int offset, int length) throws IOException;
	 */
	public void writeString(String buffer) throws IOException;

	public void writeString(String buffer, int offset, int length) throws IOException;

	public void writeString(StringBuffer buffer) throws IOException;

	public void writeString(StringBuffer buffer, int offset, int length) throws IOException;

	public void writeString(MyStringBuffer buffer) throws IOException;

	public void writeString(MyStringBuffer buffer, int offset, int length) throws IOException;

	public void writeBytesln(byte[] buffer) throws IOException;

	public void writeBytesln(byte[] buffer, int offset, int length) throws IOException;

	public void writeIntsln(int[] buffer) throws IOException;

	public void writeIntsln(int[] buffer, int offset, int length) throws IOException;

	public void writeCharsln(char[] buffer) throws IOException;

	public void writeCharsln(char[] buffer, int offset, int length) throws IOException;

	public void writeStringln(String buffer) throws IOException;

	public void writeStringln(String buffer, int offset, int length) throws IOException;

	public void writeStringln(StringBuffer buffer) throws IOException;

	public void writeStringln(StringBuffer buffer, int offset, int length) throws IOException;

	public void writeStringln(MyStringBuffer buffer) throws IOException;

	public void writeStringln(MyStringBuffer buffer, int offset, int length) throws IOException;

	public void writeln(String str) throws IOException;

	public void newline() throws IOException;

	public void writeUByte(short value) throws IOException;

	public void writeUShort(int value) throws IOException;

	public void writeUShort(int value, Byte_Ordering bitordering) throws IOException;

	public void writeUInt(long value) throws IOException;

	public void writeUInt(long value, Byte_Ordering bitordering) throws IOException;
}
