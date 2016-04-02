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

import jp77.utillib.interfaces.IOutputBuffer;
import jp77.utillib.interfaces.IOutputStream;
import jp77.utillib.arrays.buffer.FixedOutputByteBuffer;

import java.io.IOException;
import java.io.OutputStream;

public class BufferedOutputStream extends OutputStream implements IOutputStream {
	private FixedOutputByteBuffer _Buffer = null;
	private OutputStream _OStream = null;

	public BufferedOutputStream(OutputStream ostream, int size) {
		if(ostream == null) {
			throw new RuntimeException("Variable[ostream] - Is Null");
		}

		_OStream = ostream;
		_Buffer = new FixedOutputByteBuffer(size, new IOutputBuffer() {

			@Override
			public void empty(byte[] buffer, int offset, int length) {
				try {
					_OStream.write(buffer, offset, length);
				} catch(Exception e) {}
			}
		});
	}

	@Override
	public void write(int b) throws IOException {
		_Buffer.put((byte)b);
	}

	@Override
	public boolean isClosed() {
		return _OStream == null;
	}

	@Override
	public synchronized void close() throws IOException {
		if(_OStream != null) {
			_OStream.close();
		}
		_OStream = null;
		_Buffer = null;
	}
}
