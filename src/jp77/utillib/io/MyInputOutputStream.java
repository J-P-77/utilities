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

import jp77.utillib.file.FileUtil;
import jp77.utillib.file.FileUtil.Line_Ending;
import jp77.utillib.file.FileUtil.Access;

import jp77.utillib.utilities.BitOperations.Byte_Ordering;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <pre>
 * <b>Current Version 1.1.0
 * 
 * January 12, 2011 (version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class MyInputOutputStream extends AMyInputOutputStream {
	private InputStream _IStream = null;
	private OutputStream _OStream = null;

	private final Access _DIRECTION;

	public MyInputOutputStream(InputStream istream, OutputStream ostream, FileUtil.Access direction, Line_Ending lineending, Byte_Ordering bitordering) throws IOException {
		super(lineending, bitordering);

		if(direction == null) {
			throw new RuntimeException("Variable[direction] - Is Null");
		}

		if(lineending == null) {
			throw new RuntimeException("Variable[lineending] - Is Null");
		}

		switch(direction) {
			case READ:
				_IStream = istream;
				_OStream = null;
				break;

			case WRITE:
				_OStream = ostream;
				_IStream = null;
				break;

			case READ_WRITE:
			default:
				_IStream = istream;
				_OStream = ostream;
		}

		_DIRECTION = (direction == null ? Access.READ_WRITE : direction);
	}

	public Access getDirection() {
		return _DIRECTION;
	}

	public boolean isOpen() {
		return (_IStream != null && _OStream != null);
	}

	public boolean isClosed() {
		return (_IStream == null && _OStream == null);
	}

	public void flush() throws IOException {
		if(_OStream == null) {
			throw new IOException("OutputStream Is Closed");
		} else {
			_OStream.flush();
		}
	}

	public synchronized void close() throws IOException {
		if(_IStream != null) {
			_IStream.close();
			_IStream = null;
		}

		if(_OStream != null) {
			_OStream.flush();
			_OStream.close();
			_OStream = null;
		}
	}

	@Override
	public int available() throws IOException {
		if(_IStream == null) {
			return 0;
		}

		return _IStream.available();
	}

	@Override
	public int read() throws IOException {
		return _IStream.read();
	}

	public void write(int b) throws IOException {
		_OStream.write(b);
	}

	@Override
	protected void finalize() throws Throwable {
		close();

		super.finalize();
	}
}
