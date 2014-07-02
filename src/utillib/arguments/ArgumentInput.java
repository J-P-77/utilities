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

package utillib.arguments;

import utillib.file.FileUtil;

import java.io.InputStream;

public class ArgumentInput extends ArgumentsShared {
	private InputStream _IStream;

	private int _PreviousChar = -1;

	public ArgumentInput(InputStream istream) {
		this(istream, false);
	}

	public ArgumentInput(InputStream istream, boolean autoread) {
		if(istream == null) {
			throw new RuntimeException("Variable[istream] - Is Null");
		}

		_IStream = istream;

		if(autoread) {
			read(false);
		}
	}

	public boolean hasRead() {
		return (_PreviousChar == -2);
	}

	/**
	 * 
	 * @return true if read was successful, false if not
	 */
	public boolean read() {
		return read(false);
	}

	/**
	 * 
	 * @param autoclose
	 *            will close stream after write
	 * @return true if read was successful, false if not
	 */
	public boolean read(boolean autoclose) {
		if(hasRead()) {
			return false;
		} else {
			boolean NoErrors = true;
			try {
				Title Working = null;
				String Str = null;
				while((Str = readln(_IStream)) != null) {
					if(isBlankLine(Str)) {
						//DO NOTHING
					} else if(isComment(Str)) {//DO NOTHING
						//TODO (Add Comment Titles)
//                        if(Working == null) {
//                            //ERROR
//                        } else {
//                            Working.addArgument(Str);
//                        }
					} else if(isTitle(Str)) {
						addTitle(Working);
						Working = null;
						Working = new Title(Str.substring(1, Str.length() - 1));
					} else if(isArgument(Str)) {
						if(Working == null) {
							//ERROR
						} else {
							Working.addArgument(Str);
						}
					}
				}
				addTitle(Working);
			} catch(Exception ex) {
				System.out.println("!!!ERROR!!! - " + ex.getMessage());
				NoErrors = false;
			} finally {
				if(autoclose) {
					close();
				}
			}
			_PreviousChar = -2;//Indicate That File Has Been Read

			return NoErrors;
		}
	}

	public void close() {
		try {
			if(_IStream != null) {
				_IStream.close();
				_IStream = null;
			}
		} catch(Exception i) {}
	}

	private boolean isComment(String str) {
		return str.startsWith(_CommentLineStart);
	}

	private String readln(InputStream istream) {
		StringBuffer Line = new StringBuffer();

		boolean EndofLine = false;
		boolean SkipChar = false;
		int CurrentChar = -1;
		//r = 13, n = 10
		//\r,\n
		//\r
		//\n
		try {
			while((CurrentChar = istream.read()) != -1) {
				if(CurrentChar == FileUtil._CR_) {
					EndofLine = true;
				} else if(CurrentChar == FileUtil._LF_) {
					if(_PreviousChar == FileUtil._CR_) {
						SkipChar = true;
					} else {
						EndofLine = true;
					}
				}

				_PreviousChar = CurrentChar;

				if(EndofLine) {
					return Line.toString();
				} else if(SkipChar) {
					SkipChar = false;
				} else {
					Line.append((char)CurrentChar);
				}
			}
		} catch(Exception e) {}

		return null;
	}

	private static boolean isBlankLine(String str) {
		return str.equals("");
	}

	private static boolean isArgument(String str) {
		return str.contains("=");
	}

	private static boolean isTitle(String str) {
		return str.startsWith("[") && str.endsWith("]");
	}
}
