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

import utillib.file.FileUtil.Line_Ending;

import utillib.utilities.BitOperations.Byte_Ordering;

import utillib.interfaces.IMyOutputStream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * <pre>
 * <b>Current Version 1.0.2</b>
 * 
 * October 14, 2009 (version 1.0.0)
 *     -First Released
 * 
 * January 10, 2010 (version 1.0.1)
 *     -Added
 *         -Bit Ordering
 * 
 * March 29, 2010 (version 1.0.2)
 *     -Updated
 *         -Exported Everything To abstract class AMyOutputStream
 * 
 * <b>default ordering:</b> Little Endian
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class MyOutputStream extends AMyOutputStream implements IMyOutputStream {
	public static final MyOutputStream _SYSTEM_OUT_ = new MyOutputStream(System.out);

	private OutputStream _OStream = null;

	public MyOutputStream(OutputStream ostream) {
		this(ostream, Line_Ending.WINDOWS, Byte_Ordering.LITTLE_ENDIAN);
	}

	public MyOutputStream(OutputStream ostream, Line_Ending lineending) {
		this(ostream, lineending, Byte_Ordering.LITTLE_ENDIAN);
	}

	public MyOutputStream(OutputStream ostream, Byte_Ordering byteordering) {
		this(ostream, Line_Ending.WINDOWS, byteordering);
	}

	public MyOutputStream(OutputStream ostream, Line_Ending lineending, Byte_Ordering byteordering) {
		super(lineending, byteordering);

		if(ostream == null) {
			throw new RuntimeException("Variable[ostream] - Is Null");
		}

		_OStream = ostream;
	}

	public OutputStream getInternalStream() {
		return _OStream;
	}

	public void write(int value) throws IOException {
		_OStream.write(value);
	}

	@Override
	public void write(byte[] buffer) throws IOException {
		_OStream.write(buffer);
	}

	@Override
	public void write(byte[] buffer, int offset, int length) throws IOException {
		_OStream.write(buffer, offset, length);
	}

	@Override
	public boolean isClosed() {
		return _OStream == null;
	}

	@Override
	public void flush() throws IOException {
		_OStream.flush();
	}

	@Override
	public void close() throws IOException {
		if(_OStream != null) {
			_OStream.close();
			_OStream = null;
		}
	}
}
