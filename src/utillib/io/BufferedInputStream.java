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

import utillib.interfaces.IInputBuffer;
import utillib.interfaces.IInputStream;
import utillib.arrays.buffer.FixedInputByteBuffer;

import java.io.IOException;
import java.io.InputStream;

public class BufferedInputStream extends InputStream implements IInputStream {
	private FixedInputByteBuffer _Buffer = null;
	private InputStream _IStream = null;

	public BufferedInputStream(InputStream istream, int size) {
		if(istream == null) {
			throw new RuntimeException("Variable[istream] - Is Null");
		}

		_IStream = istream;
		_Buffer = new FixedInputByteBuffer(size, new IInputBuffer() {

			@Override
			public int fill(byte[] buffer, int offset, int length) {
				try {
					return _IStream.read(buffer, offset, length);
				} catch(Exception e) {}

				return -1;
			}
		});
	}

	@Override
	public int read() throws IOException {
		return _Buffer.get();
	}

	@Override
	public boolean isClosed() {
		return _IStream == null;
	}

	@Override
	public synchronized void close() throws IOException {
		if(_IStream != null) {
			_IStream.close();
		}
		_IStream = null;
		_Buffer = null;
	}
}
