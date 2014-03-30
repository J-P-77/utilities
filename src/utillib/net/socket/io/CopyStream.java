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

package utillib.net.socket.io;

import java.io.InputStream;
import java.io.OutputStream;

public class CopyStream implements Runnable {
	private InputStream _IStream = null;
	private OutputStream _OStream = null;

	public CopyStream(InputStream istream, OutputStream ostream) {
		if(istream == null) {
			throw new RuntimeException("Variable[istream] - Is Null");
		}

		if(ostream == null) {
			throw new RuntimeException("Variable[ostream] - Is Null");
		}

		_IStream = istream;
		_OStream = ostream;
	}

	@Override
	public void run() {
		try {
			final byte[] BUFFER = new byte[512];

			int Read = 0;
			while((Read = _IStream.read(BUFFER)) > 0) {
				_OStream.write(BUFFER, 0, Read);
			}
		} catch(Exception e) {

		} finally {
			close();
		}
	}

	public boolean isClosed() {
		return (_IStream == null && _OStream == null);
	}

	public void close() {
		if(_IStream != null) {
			try {
				_IStream.close();
			} catch(Exception e) {}
			_IStream = null;
		}

		if(_OStream != null) {
			try {
				_OStream.flush();
				_OStream.close();
			} catch(Exception e) {}
			_OStream = null;
		}
	}
}
