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

package utillib.file;

import utillib.file.FileUtil.Line_Ending;

import utillib.interfaces.ISeekable;

import utillib.utilities.BitOperations.Byte_Ordering;

import utillib.io.AMyInputOutputStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.RandomAccessFile;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * January 11, 2010 (version 1.0.0)
 *     -First Released
 * 
 * <b>default ordering</b>: Little Endian
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class RandomAccessFileInputOutputStream extends AMyInputOutputStream implements ISeekable {
	private final File _FILE;

	private RandomAccessFile _Raf;

	public RandomAccessFileInputOutputStream(String file) throws FileNotFoundException {
		this(new File(file), Line_Ending.WINDOWS, Byte_Ordering.LITTLE_ENDIAN);
	}

	public RandomAccessFileInputOutputStream(String file, Byte_Ordering ordering) throws FileNotFoundException {
		this(new File(file), Line_Ending.WINDOWS, ordering);
	}

	public RandomAccessFileInputOutputStream(String file, Line_Ending ending) throws FileNotFoundException {
		this(new File(file), ending, Byte_Ordering.LITTLE_ENDIAN);
	}

	public RandomAccessFileInputOutputStream(File file) throws FileNotFoundException {
		this(file, Line_Ending.WINDOWS, Byte_Ordering.LITTLE_ENDIAN);
	}

	public RandomAccessFileInputOutputStream(File file, Byte_Ordering ordering) throws FileNotFoundException {
		this(file, Line_Ending.WINDOWS, ordering);
	}

	public RandomAccessFileInputOutputStream(File file, Line_Ending ending) throws FileNotFoundException {
		this(file, ending, Byte_Ordering.LITTLE_ENDIAN);
	}

	public RandomAccessFileInputOutputStream(File file, Line_Ending lineending, Byte_Ordering bitordering) throws FileNotFoundException {
		super(lineending, bitordering);

		_Raf = new RandomAccessFile(file, FileUtil.Random_Access_Mode.READ_WRITE_S.getValue());

		_FILE = file;
	}

	public RandomAccessFileInputOutputStream(RandomAccessFile raf, Line_Ending ending, Byte_Ordering bitordering) throws FileNotFoundException {
		this(raf, null, ending, bitordering);
	}

	public RandomAccessFileInputOutputStream(RandomAccessFile raf, File file, Line_Ending ending, Byte_Ordering bitordering) throws FileNotFoundException {
		super(ending, bitordering);

		if(raf == null) {
			throw new RuntimeException("Variable[raf] - Is Null");
		}

		_Raf = raf;
		_FILE = file;
	}

	public File getFile() {
		return _FILE;
	}

	public long getCurrentPosition() throws IOException {
		return _Raf.getFilePointer();
	}

	public int skipBytes(int n) throws IOException {
		return _Raf.skipBytes(n);
	}

	public void seek(long pos) throws IOException {
		_Raf.seek(pos);
	}

	public void seek(Seek seek, long pos) throws IOException {
		switch(seek) {
//SEEK_SET moves the pointer x bytes down from the beginning of the file (from byte 0 in the file).
			case SEEK_SET://pos Must Be Positive
				_Raf.seek((pos < 0 ? 0 : pos));
				break;

//SEEK_CUR moves the pointer x bytes down from the current pointer position.
			case SEEK_CUR://pos Must Be Positive
				_Raf.seek(_Raf.getFilePointer() + (pos < 0 ? 0 : pos));
				break;

//SEEK_END moves the pointer from the end of the file (so you must use negative offsets with this option).
			case SEEK_END://pos Must Be Negative
				_Raf.seek(_Raf.length() + (pos > 0 ? 0 : pos));
				break;
		}
	}

	public void seekToBeginning() throws IOException {
		seek(Seek.SEEK_SET, 0);
	}

	public void seekToEnd() throws IOException {
		seek(Seek.SEEK_END, 0);
	}

	public long length() throws IOException {
		return _Raf.length();
	}

	public void setLength(long newLength) throws IOException {
		_Raf.setLength(newLength);
	}

	public void writeBoolean(boolean value) throws IOException {
		_Raf.writeBoolean(value);
	}

	public void writeFloat(float v) throws IOException {
		_Raf.writeFloat(v);
	}

	public void writeDouble(double v) throws IOException {
		_Raf.writeDouble(v);
	}

	@Override
	public void write(int b) throws IOException {
		_Raf.write(b);
	}

	public void write(byte[] buffer, int offset, int length) throws IOException {
		_Raf.write(buffer, offset, length);
	}

	@Override
	public int read() throws IOException {
		return _Raf.read();
	}

	@Override
	public boolean isClosed() {
		return _Raf == null;
	}

	@Override
	public void flush() throws IOException {}

	@Override
	public int available() throws IOException {
		throw new RuntimeException("Not Supported Yet");
	}

	@Override
	public synchronized void close() throws IOException {
		if(_Raf != null) {
			_Raf.close();
			_Raf = null;
		}
	}

//    public static void main(String[] args) {
//        String File = "C:\\Documents and Settings\\Dalton Dell\\Desktop\\Test.txt";
//        FileOutputStreamSeekable OStream1 = null;
//        FileOutputStreamSeekable OStream2 = null;
//        try {
//            OStream1 = new FileOutputStreamSeekable(File);
//            OStream2 = new FileOutputStreamSeekable(File);
//
//            OStream1.writeChar('J');
//            OStream2.seek(2);
//            OStream2.writeChar('P');
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if(OStream1 != null) {
//                    OStream1.close();
//                    OStream1 = null;
//                }
//            } catch (Exception e) {}
//            try {
//                if(OStream2 != null) {
//                    OStream2.close();
//                    OStream2 = null;
//                }
//            } catch (Exception e) {}
//        }
//    }
}
