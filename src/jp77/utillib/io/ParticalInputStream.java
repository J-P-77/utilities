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
import java.io.InputStream;

import jp77.utillib.interfaces.IInputStream;

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
 *         -Exported Everything To abstract class AMyInputStream
 * 
 * <b>default ordering</b>: Little Endian
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class ParticalInputStream extends InputStream implements IInputStream /* implements ISeekable */{
	private InputStream _IStream = null;

	private final long _LENGTH;
	private long _Pos = 0;

	public ParticalInputStream(InputStream istream, long length) {
		if(istream == null) {
			throw new RuntimeException("Variable[istream] - Is Null");
		}

		_IStream = istream;
		_LENGTH = length;
	}

	public long getCurrentPosition() throws IOException {
		return _Pos;
	}

	public long getLength() {
		return _LENGTH;
	}

//    @Override
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
//    @Override
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
	public int read() throws IOException {
		if(_Pos < _LENGTH) {
			_Pos++;

			return _IStream.read();
		}

		return -1;
	}

	@Override
	public int available() throws IOException {
		return (int)(_LENGTH - _Pos);
	}

	@Override
	public boolean isClosed() {
		return _IStream == null;
	}

	@Override
	public void close() throws IOException {
		if(_IStream != null) {
			_IStream.close();
			_IStream = null;
		}
	}
	/*
	 * public static void main(String[] args) { ParticalInputStream IStream =
	 * null; try { final java.io.File FILE = new java.io.File(
	 * "C:\\Documents and Settings\\Dalton Dell\\Desktop\\System Properties.txt"
	 * ); IStream = new ParticalInputStream(new java.io.FileInputStream(FILE),
	 * FILE.length(), false);
	 * 
	 * IStream.seek(18); System.out.println(IStream.readString(31)); } catch
	 * (Exception e) { } finally { try { if(IStream != null) { IStream.close();
	 * IStream = null; } } catch (Exception e) {} } }
	 */
}