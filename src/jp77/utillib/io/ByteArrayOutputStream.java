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

package jp77.utillib.io;

import jp77.utillib.arrays.ArraysUtil;
import jp77.utillib.interfaces.IOutputStream;

import java.io.IOException;
import java.io.OutputStream;

public class ByteArrayOutputStream extends OutputStream implements IOutputStream {
	private final int _LENGTH;

	private byte[] _Buffer;

	private int _Pos = 0;

	public ByteArrayOutputStream(byte[] buffer) {
		this(buffer, 0, buffer.length);
	}

	public ByteArrayOutputStream(byte[] buffer, int offset, int length) {
		if(buffer == null) {
			throw new RuntimeException("Variable[buffer] - Is Null");
		}

		ArraysUtil.checkBufferBounds(buffer.length, offset, length);

		_Buffer = buffer;
		_LENGTH = length;
		_Pos = offset;
	}

	public int length() {
		return _LENGTH;
	}

	public int getPosition() {
		return _Pos;
	}

	public void reset() {
		_Pos = 0;
	}

	public void reset(int value) {
		if(value >= 0 && value < _LENGTH) {
			_Pos = value;
		}
	}

	@Override
	public void write(int value) throws IOException {
		if(_Pos < _LENGTH) {
			_Buffer[_Pos++] = (byte)value;
		} else {
			throw new IOException();
		}
	}

	@Override
	public void write(byte[] buffer, int offset, int length) throws IOException {
		ArraysUtil.checkBufferBounds(buffer.length, offset, length);

		for(int X = 0; X < length && _Pos < _LENGTH; X++, _Pos++) {
			buffer[offset + X] = _Buffer[_Pos];
		}
	}

	@Override
	public void flush() throws IOException {}

	@Override
	public boolean isClosed() {
		return _Buffer == null;
	}

	@Override
	public void close() throws IOException {
		_Buffer = null;
	}

	public static void main(String[] args) {
		final byte[] BUFFER = new byte[100];

		ByteArrayOutputStream BOStream = null;
		MyOutputStream OStream = null;
		try {
			BOStream = new ByteArrayOutputStream(BUFFER);
			OStream = new MyOutputStream(BOStream);

			OStream.writeString("Justin Palinkas");
		} catch(Exception e) {}

		ByteArrayInputStream BIStream = null;
		try {
			BIStream = new ByteArrayInputStream(BUFFER, 0, BOStream.getPosition());

			System.out.println(BIStream.available());

			int Read = -1;
			while((Read = BIStream.read()) != -1) {
				System.out.print((char)Read);
			}
			System.out.println();
			System.out.println(BIStream.available());
		} catch(Exception e) {}
	}
}
