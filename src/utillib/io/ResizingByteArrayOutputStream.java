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

package utillib.io;

import utillib.arrays.ResizingByteArray;
import utillib.interfaces.IOutputStream;

import java.io.IOException;
import java.io.OutputStream;

public class ResizingByteArrayOutputStream extends OutputStream implements IOutputStream {
	private ResizingByteArray _Buffer;

	public ResizingByteArrayOutputStream(ResizingByteArray buffer) {
		this(buffer, 0);
	}

	public ResizingByteArrayOutputStream(ResizingByteArray buffer, int offset) {
		if(buffer == null) {
			throw new RuntimeException("Variable[buffer] - Is Null");
		}

		if(offset != 0 && buffer.length() != 0) {
			if(offset < 0 || offset >= buffer.length()) {
				throw new RuntimeException("Variable[offset] - Offset Must Be Greater Than Zero And Less Than Buffer Length");
			}
		}

		_Buffer = buffer;
	}

	public ResizingByteArray getResizingByteArray() {
		return _Buffer;
	}

	@Override
	public void write(int value) throws IOException {
		_Buffer.put((byte)value);
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
}
