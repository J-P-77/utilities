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
import utillib.interfaces.IInputStream;

import java.io.IOException;
import java.io.InputStream;

public class ResizingByteArrayInputStream extends InputStream implements IInputStream {
	private ResizingByteArray _Buffer;

	private int _Pos = 0;

	public ResizingByteArrayInputStream(ResizingByteArray buffer) {
		this(buffer, 0);
	}

	public ResizingByteArrayInputStream(ResizingByteArray buffer, int offset) {
		if(buffer == null) {
			throw new RuntimeException("Variable[buffer] - Is Null");
		}

		if(offset != 0 && buffer.length() != 0) {
			if(offset < 0 || offset >= buffer.length()) {
				throw new RuntimeException("Variable[offset] - Offset Must Be Greater Than Zero And Less Than Buffer Length");
			}
		}

		_Buffer = buffer;
		_Pos = offset;
	}

	public ResizingByteArray getResizingByteArray() {
		return _Buffer;
	}

	public int getPosition() {
		return _Pos;
	}

	@Override
	public int read() throws IOException {
		if(_Pos < _Buffer.length()) {
			return _Buffer.get(_Pos);
		}

		return -1;
	}

	@Override
	public int available() throws IOException {
		return _Buffer.length() - _Pos;
	}

	@Override
	public boolean isClosed() {
		return _Buffer == null;
	}

	@Override
	public void close() throws IOException {
		_Buffer = null;
	}

//	public static void main(String[] args) {
//		final ResizingByteArray BUFFER = new ResizingByteArray(36);
//		
//		ResizingByteArrayOutputStream BOStream = null;
//		try {
//			BOStream = new ResizingByteArrayOutputStream(BUFFER);
//			
//			BOStream.writeString("Justin Palinkas");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		ResizingByteArrayInputStream BIStream = null;
//		try {
//			BIStream = new ResizingByteArrayInputStream(BUFFER);
//			
//			System.out.println(BIStream.available());
//			System.out.println(BIStream.readString(15));
//			System.out.println(BIStream.available());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
