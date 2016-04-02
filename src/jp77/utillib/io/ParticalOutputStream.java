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

// import jp77.utillib.interfaces.ISeekable;

import java.io.IOException;
import java.io.OutputStream;

import jp77.utillib.interfaces.IOutputStream;

public class ParticalOutputStream extends OutputStream implements IOutputStream /* implements ISeekable */{
	private OutputStream _OStream = null;

	private final long _LENGTH;
	private long _Pos = 0;

	public ParticalOutputStream(OutputStream ostream, long length) {
		if(ostream == null) {
			throw new RuntimeException("Variable[istream] - Is Null");
		}

		_OStream = ostream;
		_LENGTH = length;
	}

	public long getCurrentPosition() throws IOException {
		return _Pos;
	}

	public long getLength() {
		return _LENGTH;
	}

//    public void seek(long pos) throws IOException {
//        if(pos < 0) {
//            _Pos = 0;
//        } else if(pos > _LENGTH) {
//            _Pos = _LENGTH;
//        } else {
//            _Pos = pos;
//        }
//    }
//
//    public void seek(Seek seek, long pos) throws IOException {
//        switch(seek) {
////SEEK_SET moves the pointer x bytes down from the beginning of the file (from byte 0 in the file).
//            case SEEK_SET://pos Must Be Positive
//                seek((pos < 0 ? 0 : pos));
//                break;
//
////SEEK_CUR moves the pointer x bytes down from the current pointer position.
//            case SEEK_CUR://pos Must Be Positive
//                seek(_Pos + (pos < 0 ? 0 : pos));
//                break;
//
////SEEK_END moves the pointer from the end of the file (so you must use negative offsets with this option).
//            case SEEK_END://pos Must Be Negative
//                seek(_LENGTH + (pos > 0 ? 0 : pos));
//                break;
//        }
//    }

	@Override
	public void flush() throws IOException {
		_OStream.flush();
	}

	@Override
	public void write(int i) throws IOException {
		if(_Pos < _LENGTH) {
			_OStream.write(i);
			_Pos++;
		} else {
			throw new IOException("Max Length Reached");
		}
	}

	@Override
	public boolean isClosed() {
		return _OStream == null;
	}

	@Override
	public void close() throws IOException {
		if(_OStream != null) {
			_OStream.flush();
			_OStream.close();
			_OStream = null;
		}
	}
}
