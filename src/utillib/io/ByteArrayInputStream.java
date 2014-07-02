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

package utillib.io;

import utillib.arrays.ArraysUtil;
import utillib.interfaces.IInputStream;

import java.io.IOException;
import java.io.InputStream;

public class ByteArrayInputStream extends InputStream implements IInputStream {
	private final int _LENGTH;

	private byte[] _Buffer;

	private int _Pos = 0;

	public ByteArrayInputStream(byte[] buffer) {
		this(buffer, 0, buffer.length);
	}

	public ByteArrayInputStream(byte[] buffer, int offset, int length) {
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

	@Override
	public int read() {
		if(_Pos < _LENGTH) {
			return _Buffer[_Pos++];
		}

		return -1;
	}

	@Override
	public int read(byte[] buffer, int offset, int length) {
		ArraysUtil.checkBufferBounds(buffer.length, offset, length);

		int X = 0;
		while(X < length && _Pos < _LENGTH) {
			buffer[offset + X++] = _Buffer[_Pos++];
		}

		return X;
	}

	@Override
	public int available() throws IOException {
		return _LENGTH - _Pos;
	}

	@Override
	public boolean isClosed() {
		return _Buffer == null;
	}

	@Override
	public void close() throws IOException {
		_Buffer = null;
	}

	@Override
	public boolean markSupported() {
		return true;
	}

	@Override
	public synchronized void mark(int readlimit) {
		reset(readlimit);
	}

	public void reset() {
		_Pos = 0;
	}

	public void reset(int value) {
		if(value >= 0 && value < _LENGTH) {
			_Pos = value;
		} else {
			throw new RuntimeException("Variable[value] - Invalid Index");
		}
	}

	public static void main(String[] args) {
		ByteArrayInputStream BIStream = null;
		try {
			BIStream = new ByteArrayInputStream("Justin Palinkas".getBytes());

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
