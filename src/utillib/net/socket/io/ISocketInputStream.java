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

package utillib.net.socket.io;

import utillib.net.socket.MySocket;
import utillib.interfaces.IInputStream;

import java.io.IOException;
import java.io.InputStream;

public class ISocketInputStream extends InputStream implements IInputStream {
	private MySocket _Socket = null;

	public ISocketInputStream(MySocket socket) {
		if(socket == null) {
			throw new RuntimeException("Variable[socket] - Is Null");
		}

		_Socket = socket;
	}

	@Override
	public int read() throws IOException {
		final byte[] BYTES = new byte[1];

		if(_Socket.receive(BYTES, 0, 1) == 1) {
			return (int)BYTES[0];
		}

		return -1;
	}

	@Override
	public int read(byte[] buffer, int offset, int length) throws IOException {
		return _Socket.receive(buffer, offset, length);
	}

	@Override
	public boolean isClosed() {
		if(_Socket == null) {
			return true;
		}

		return _Socket.isClosed();
	}

	@Override
	public void close() throws IOException {
		if(_Socket != null) {
			try {
				_Socket.close();
			} catch(Exception e) {}
			_Socket = null;
		}
	}
}