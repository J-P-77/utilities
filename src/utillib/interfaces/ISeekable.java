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

package utillib.interfaces;

import java.io.IOException;

public interface ISeekable {
	public static enum Seek {
		SEEK_SET, //From Beginning Of The File
		SEEK_CUR, //From File Pointer Of The File
		SEEK_END;//From End Of The File
	};

	public long getCurrentPosition() throws IOException;

	public void seek(long pos) throws IOException;

	public void seek(Seek seek, long pos) throws IOException;

//SEEK_SET moves the pointer x bytes down from the  beginning of the file (from byte 0 in the file).
//SEEK_CUR moves the pointer x bytes down from the current pointer position.
//SEEK_END moves the pointer from the end of the file (so you must use negative offsets with this option).
}
