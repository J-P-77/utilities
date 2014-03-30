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

package utillib.debug;

import utillib.file.FileUtil.Line_Ending;

import utillib.file.MyFileOutputStream;

import utillib.strings.MyStringBuffer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;

/**
 * <pre>
 * <b>Current Version 1.0.1</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * April 01, 2009 (version 1.0.1)
 *     -Updated
 *         -EveryThing
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class DebugOutputStream extends MyFileOutputStream {
//    protected final Object _LOCK = new Object();

	private File _File = null;
	private boolean _IsAppending = true;

	private boolean _InsertDateAndTime = false;

	public DebugOutputStream(String filename) throws FileNotFoundException {
		this(new File(filename), true, false);
	}

	public DebugOutputStream(String filename, boolean append) throws FileNotFoundException {
		this(new File(filename), append, false);
	}

	public DebugOutputStream(String path, boolean append, boolean insertdateandtime) throws FileNotFoundException {
		this(new File(path), append, insertdateandtime);
	}

	public DebugOutputStream(File file, boolean append) throws FileNotFoundException {
		this(file, append, false);
	}

	public DebugOutputStream(File file, boolean append, boolean insertdateandtime) throws FileNotFoundException {
		super(file, append);

//        reOpen(file, append);
		_File = file;
		_IsAppending = append;
		_InsertDateAndTime = insertdateandtime;
	}

	public File getFile() {
		return _File;
	}

	public boolean isAppending() {
		return _IsAppending;
	}

	public void setInsertDateAndTime(boolean value) {
		_InsertDateAndTime = value;
	}

	public boolean getInsertDateAndTime() {
		return _InsertDateAndTime;
	}

	@Override
	public void write(int i) {
		if(!isClosed()) {
			try {
				if(_InsertDateAndTime) {
					writeDateAndTime();
				}

//                synchronized(_LOCK) {
				super.write(i);
//                }
			} catch(Exception e) {
				System.out.println("!!!DEBUG FILE ERROR!!! - write(int c)");
			}
		} else {
			System.out.println("!!!DEBUG FILE ERROR!!! - File Not Open");
		}
	}

	public void write(int[] ints) {
		write(ints, 0, ints.length);
	}

	public void write(int[] ints, int offset, int length) {
		if(!isClosed()) {
			try {
				if(_InsertDateAndTime) {
					writeDateAndTime();
				}

//                synchronized(_LOCK) {
				super.writeInts(ints, offset, length);
//                }
			} catch(Exception e) {
				System.out.println("!!!DEBUG FILE ERROR!!! -  write(int[] ints, int offset, int length)");
			}
		} else {
			System.out.println("!!!DEBUG FILE ERROR!!! - File Not Open");
		}
	}

	public void write(char c) {
		if(!isClosed()) {
			try {
				if(_InsertDateAndTime) {
					writeDateAndTime();
				}

//                synchronized(_LOCK) {
				super.write((int)c);
//                }
			} catch(Exception e) {
				System.out.println("!!!DEBUG FILE ERROR!!! - write(char c)");
			}
		} else {
			System.out.println("!!!DEBUG FILE ERROR!!! - File Not Open");
		}
	}

	public void write(char[] chars) {
		write(chars, 0, chars.length);
	}

	public void write(char[] chars, int offset, int length) {
		if(!isClosed()) {
			try {
				if(_InsertDateAndTime) {
					writeDateAndTime();
				}

//                synchronized(_LOCK) {
				super.writeChars(chars, offset, length);
//                }
			} catch(Exception e) {
				System.out.println("!!!DEBUG FILE ERROR!!! - write(char[] chars, int offset, int length)");
			}
		} else {
			System.out.println("!!!DEBUG FILE ERROR!!! - File Not Open");
		}
	}

	public void write(String str) {
		write(str, 0, str.length());
	}

	public void write(String str, int offset, int length) {
		if(!isClosed()) {
			try {
				if(_InsertDateAndTime) {
					writeDateAndTime();
				}

//                synchronized(_LOCK) {
				super.writeString(str, offset, length);
//                }
			} catch(Exception e) {
				System.out.println("!!!DEBUG FILE ERROR!!! - write(String str, int offset, int length)");
			}
		} else {
			System.out.println("!!!DEBUG FILE ERROR!!! - File Not Open");
		}
	}

	public void write(MyStringBuffer str) {
		write(str, 0, str.length());
	}

	public void write(MyStringBuffer str, int offset, int length) {
		if(!isClosed()) {
			try {
				if(_InsertDateAndTime) {
					writeDateAndTime();
				}

//                synchronized(_LOCK) {
				super.writeString(str, offset, length);
//                }
			} catch(Exception e) {
				System.out.println("!!!DEBUG FILE ERROR!!! - write(String str, int offset, int length)");
			}
		} else {
			System.out.println("!!!DEBUG FILE ERROR!!! - File Not Open");
		}
	}

	public void writeDateAndTime() {
		if(!isClosed()) {
			try {
//                synchronized(_LOCK) {
				super.writeString(DebugUtil.getDateAndTime() + " - ");
//                }
			} catch(Exception e) {
				System.out.println("!!!DEBUG FILE ERROR!!! - writeDateAndTime");
			}
		} else {
			System.out.println("!!!DEBUG FILE ERROR!!! - File Not Open");
		}
	}

	public void writeln(Exception e) {
		write(e.toString());
		newline();

		writeln(e.getCause(), true);
	}

	public void writeln(Throwable e) {
		writeln(e, false);
	}

	public void writeln(Throwable e, boolean indent) {
		StackTraceElement[] Trace = e.getStackTrace();

		MyStringBuffer Buffer = new MyStringBuffer(50);

		for(int X = 0; X < Trace.length; X++) {
			if(indent) {
				Buffer.append("    ");
			}

			DebugUtil.createTrace(Trace[X], Buffer);

			writeln(Buffer);
			Buffer.reset();
		}
	}

	public void writeln(char c) {
		write(c);
		newline();
	}

	public void writeln(char[] chars, int offset, int length) {
		write(chars, offset, length);
		newline();
	}

	public void writeln(String str) {
		writeln(str, 0, str.length());
	}

	public void writeln(String str, int offset, int length) {
		write(str, offset, length);
		newline();
	}

	public void writeln(MyStringBuffer str) {
		writeln(str, 0, str.length());
	}

	public void writeln(MyStringBuffer str, int offset, int length) {
		write(str, offset, length);
		newline();
	}

	public void writelnDateAndTime() {
		if(!isClosed()) {
			try {
//                synchronized(_LOCK) {
				super.writeString(DebugUtil.getDateAndTime());
//                }

				newline();
			} catch(Exception e) {
				System.out.println("!!!DEBUG FILE ERROR!!! - writeDateAndTime");
			}
		} else {
			System.out.println("!!!DEBUG FILE ERROR!!! - File Not Open");
		}
	}

	public void newline() {
		if(!isClosed()) {
			try {
//                synchronized(_LOCK) {
				super.newline();
//                }
			} catch(Exception e) {
				System.out.println("!!!DEBUG FILE ERROR!!! - writeln(String str)");
			}
		} else {
			System.out.println("!!!DEBUG FILE ERROR!!! - File Not Open");
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		close();
	}
}