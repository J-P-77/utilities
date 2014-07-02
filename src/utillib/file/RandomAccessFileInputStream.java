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

package utillib.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class RandomAccessFileInputStream extends InputStream {
	private RandomAccessFile _Raf;

	public RandomAccessFileInputStream(String file) throws FileNotFoundException, IOException {
		this(new File(file), FileUtil.Random_Access_Mode.READ_ONLY);
	}

	public RandomAccessFileInputStream(String file, long seekto) throws FileNotFoundException, IOException {
		this(new File(file), seekto, FileUtil.Random_Access_Mode.READ_ONLY);
	}

	public RandomAccessFileInputStream(String file, FileUtil.Random_Access_Mode mode) throws FileNotFoundException, IOException {
		this(new File(file), 0, mode);
	}

	public RandomAccessFileInputStream(File file) throws FileNotFoundException, IOException {
		this(file, FileUtil.Random_Access_Mode.READ_ONLY);
	}

	public RandomAccessFileInputStream(File file, long seekto) throws FileNotFoundException, IOException {
		this(file, seekto, FileUtil.Random_Access_Mode.READ_ONLY);
	}

	public RandomAccessFileInputStream(File file, FileUtil.Random_Access_Mode mode) throws FileNotFoundException, IOException {
		this(file, 0, mode);
	}

	public RandomAccessFileInputStream(File file, long seekto, FileUtil.Random_Access_Mode mode) throws FileNotFoundException, IOException {
		this(new RandomAccessFile(file, mode.getValue()));

		if(seekto < 0 /* || seekto >= _Raf.length() */) {
			throw new IOException("Invalid Seek Position: " + seekto);
		}

		if(seekto != 0) {
			_Raf.seek(seekto);
		}
	}

	public RandomAccessFileInputStream(RandomAccessFile raf) {
		if(raf == null) {
			throw new RuntimeException("Variable[raf] - Is Null");
		}

		_Raf = raf;
	}

	@Override
	public int read() throws IOException {
		return _Raf.read();
	}

	@Override
	public int read(byte[] buffer, int offset, int length) throws IOException {
		return _Raf.read(buffer, offset, length);
	}

	@Override
	public synchronized void close() throws IOException {
		if(_Raf != null) {
			_Raf.close();
			_Raf = null;
		}
	}
}
