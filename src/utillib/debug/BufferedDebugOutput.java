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

package utillib.debug;

import utillib.file.FileUtil.Line_Ending;

import utillib.file.MyFileOutputStream;

import utillib.strings.MyStringBuffer;

import java.io.File;
import java.io.OutputStream;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * August 23, 2009 (Version 1.0.0)
 *     -First Released<br>
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class BufferedDebugOutput extends OutputStream {
	private int[] _Buffer = null;
	private int _BufferTop = 0;

	protected final Object _LOCK = new Object();

	private File _File = null;

	private MyFileOutputStream _OStream = null;
	private Line_Ending _LineEnding = Line_Ending.LINUX;

	private boolean _InsertDateAndTime = false;

	private boolean _Append = true;

	public BufferedDebugOutput() {}

	public BufferedDebugOutput(String filename) {
		this(new File(filename), true, false, 1024);
	}

	public BufferedDebugOutput(String filename, boolean append) {
		this(new File(filename), append, false, 1024);
	}

	public BufferedDebugOutput(String path, boolean append, boolean insertdateandtime, int buffersize) {
		this(new File(path), append, insertdateandtime, buffersize);
	}

	public BufferedDebugOutput(File file, boolean append) {
		this(file, append, false, 1024);
	}

	public BufferedDebugOutput(File file, boolean append, boolean insertdateandtime, int buffersize) {
		setFile(file);
		setInsertDateAndTime(insertdateandtime);

		_Buffer = new int[(buffersize <= 0 ? 1024 : buffersize)];
	}

	public void setFile(String filename) {
		if(filename != null) {
			setFile(new File(filename));
		}
	}

	public void setFile(File file) {
		if(file != null) {
//            if(isOpen()) {
//                close();
//            }

			_File = file;
		}
	}

	public File getFile() {
		return _File;
	}

	public void setInsertDateAndTime(boolean value) {
		_InsertDateAndTime = value;
	}

	public boolean getInsertDateAndTime() {
		return _InsertDateAndTime;

	}

	public void open() {
		if(isClosed()) {
			if(_File == null) {
				System.out.println("!!!DEBUG FILE ERROR!!! - File Not Set");
			} else {
//                System.out.println("open(): " + _File.getAbsolutePath());
				try {
					synchronized(_LOCK) {//Maybe Not
						if(_File.exists() && _Append) {
							_OStream = new MyFileOutputStream(_File, true);
						} else {
							_OStream = new MyFileOutputStream(_File);
						}
					}
				} catch(Exception e) {
					System.out.println("!!!DEBUG FILE ERROR!!! - open()");
				}
			}
		} else {
			System.out.println("!!!DEBUG FILE ERROR!!! - Must Close Open File");
		}
	}

	@Override
	public void write(int i) {
		if(isOpen()) {
			try {
				if(_InsertDateAndTime) {
					writeDateAndTime();
				}

				synchronized(_LOCK) {
					//If MyStringBuffer(str) Length Is Greater Than Buffer Than
					//Flush Buffer Than Write Directly To File
					if(1 >= _Buffer.length) {
						flushBuffer();

						_OStream.write(i);
					} else {
						//Will MyStringBuffer(str) Fit In Buffer Is Not Flush Buffer
						if((1 + _BufferTop) >= _Buffer.length) {
							flushBuffer();
						}

						_Buffer[_BufferTop++] = i;
					}
				}
			} catch(Exception e) {
				System.out.println("!!!DEBUG FILE ERROR!!! - write(int c)");
				close();
			}
		} else {
			System.out.println("!!!DEBUG FILE ERROR!!! - File Not Open");
		}
	}

	public void write(int[] ints) {
		write(ints, 0, ints.length);
	}

	public void write(int[] ints, int offset, int length) {
		if(isOpen()) {
			try {
				if(_InsertDateAndTime) {
					writeDateAndTime();
				}

				synchronized(_LOCK) {
					final int LENGTH = length - offset;

					//If MyStringBuffer(str) Length Is Greater Than Buffer Than
					//Flush Buffer Than Write Directly To File
					if(LENGTH >= _Buffer.length) {
						flushBuffer();

						_OStream.writeInts(ints, offset, length);
					} else {
						//Will MyStringBuffer(str) Fit In Buffer Is Not Flush Buffer
						if((LENGTH + _BufferTop) >= _Buffer.length) {
							flushBuffer();
						}

						for(int X = offset; X < LENGTH; X++) {
							_Buffer[_BufferTop++] = ints[X];
						}
					}
				}
			} catch(Exception e) {
				System.out.println("!!!DEBUG FILE ERROR!!! -  write(int[] ints, int offset, int length)");
				close();
			}
		} else {
			System.out.println("!!!DEBUG FILE ERROR!!! - File Not Open");
		}
	}

	public void write(char c) {
		if(isOpen()) {
			try {
				synchronized(_LOCK) {
					if(getInsertDateAndTime()) {
						writeDateAndTime();
					}

					if((1 + _BufferTop) >= _Buffer.length) {
						flushBuffer();
					}

					_Buffer[_BufferTop++] = c;
				}
			} catch(Exception e) {
				System.out.println("!!!DEBUG FILE ERROR!!! - write(char c)");
				close();
			}
		} else {
			System.out.println("!!!DEBUG FILE ERROR!!! - File Not Open");
		}
	}

	public void write(char[] chars) {
		write(chars, 0, chars.length);
	}

	public void write(char[] chars, int offset, int length) {
		if(isOpen()) {
			try {
				if(_InsertDateAndTime) {
					writeDateAndTime();
				}

				synchronized(_LOCK) {
					final int LENGTH = length - offset;

					//If MyStringBuffer(str) Length Is Greater Than Buffer Than
					//Flush Buffer Than Write Directly To File
					if(LENGTH >= _Buffer.length) {
						flushBuffer();

						_OStream.writeChars(chars, offset, length);
					} else {
						//Will MyStringBuffer(str) Fit In Buffer Is Not Flush Buffer
						if((LENGTH + _BufferTop) >= _Buffer.length) {
							flushBuffer();
						}

						for(int X = offset; X < LENGTH; X++) {
							_Buffer[_BufferTop++] = chars[X];
						}
					}
				}
			} catch(Exception e) {
				System.out.println("!!!DEBUG FILE ERROR!!! - write(char[] chars, int offset, int length)");
				close();
			}
		} else {
			System.out.println("!!!DEBUG FILE ERROR!!! - File Not Open");
		}
	}

	public void write(String str) {
		write(str, 0, str.length());
	}

	public void write(String str, int offset, int length) {
		if(isOpen()) {
			try {
				if(_InsertDateAndTime) {
					writeDateAndTime();
				}

				synchronized(_LOCK) {
					final int LENGTH = length - offset;

					//If MyStringBuffer(str) Length Is Greater Than Buffer Than
					//Flush Buffer Than Write Directly To File
					if(LENGTH >= _Buffer.length) {
						flushBuffer();

						_OStream.writeString(str, offset, length);
					} else {
						//Will MyStringBuffer(str) Fit In Buffer Is Not Flush Buffer
						if((LENGTH + _BufferTop) >= _Buffer.length) {
							flushBuffer();
						}

						for(int X = offset; X < LENGTH; X++) {
							_Buffer[_BufferTop++] = str.charAt(X);
						}
					}
				}
			} catch(Exception e) {
				System.out.println("!!!DEBUG FILE ERROR!!! - write(String str, int offset, int length)");
				close();
			}
		} else {
			System.out.println("!!!DEBUG FILE ERROR!!! - File Not Open");
		}
	}

	public void write(MyStringBuffer str) {
		write(str, 0, str.length());
	}

	public void write(MyStringBuffer str, int offset, int length) {
		if(isOpen()) {
			try {
				if(_InsertDateAndTime) {
					writeDateAndTime();
				}

				synchronized(_LOCK) {
					final int LENGTH = length - offset;

					//If MyStringBuffer(str) Length Is Greater Than Buffer Than
					//Flush Buffer Than Write Directly To File
					if(LENGTH >= _Buffer.length) {
						flushBuffer();

						_OStream.writeString(str, offset, length);
					} else {
						//Will MyStringBuffer(str) Fit In Buffer Is Not Flush Buffer
						if((LENGTH + _BufferTop) >= _Buffer.length) {
							flushBuffer();
						}

						for(int X = offset; X < LENGTH; X++) {
							_Buffer[_BufferTop++] = str.charAt(X);
						}
					}
				}
			} catch(Exception e) {
				System.out.println("!!!DEBUG FILE ERROR!!! - write(String str, int offset, int length)");
				close();
			}
		} else {
			System.out.println("!!!DEBUG FILE ERROR!!! - File Not Open");
		}
	}

	public void writeDateAndTime() {
		if(isOpen()) {
			try {
				final String DATE_AND_TIME = DebugUtil.getDateAndTime() + " - ";

				synchronized(_LOCK) {
					//If String(DATE_AND_TIME) Length Is Greater Than Buffer Than
					//Flush Buffer Than Write Directly To File
					if(DATE_AND_TIME.length() > _Buffer.length) {
						flushBuffer();
						_OStream.writeString(DATE_AND_TIME);
					} else {
						//Will String(DATE_AND_TIME) Fit In Buffer Is Not Flush Buffer
						if((DATE_AND_TIME.length() + _BufferTop) >= _Buffer.length) {
							flushBuffer();
						}

						for(int X = 0; X < DATE_AND_TIME.length(); X++) {
							_Buffer[_BufferTop++] = DATE_AND_TIME.charAt(X);
						}
					}
				}
			} catch(Exception e) {
				System.out.println("!!!DEBUG FILE ERROR!!! - write(int c)");
				close();
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
		if(isOpen()) {
			try {
				final String DATE_AND_TIME = DebugUtil.getDateAndTime() + _LineEnding.getValue();

				synchronized(_LOCK) {
					//If String(DATE_AND_TIME) Length Is Greater Than Buffer Than
					//Flush Buffer Than Write Directly To File
					if(DATE_AND_TIME.length() > _Buffer.length) {
						flushBuffer();
						_OStream.writeString(DATE_AND_TIME);
					} else {
						//Will String(DATE_AND_TIME) Fit In Buffer If Not Flush Buffer
						if((DATE_AND_TIME.length() + _BufferTop) >= _Buffer.length) {
							flushBuffer();
						}

						for(int X = 0; X < DATE_AND_TIME.length(); X++) {
							_Buffer[_BufferTop++] = DATE_AND_TIME.charAt(X);
						}
					}
				}
			} catch(Exception e) {
				System.out.println("!!!DEBUG FILE ERROR!!! - write(int c)");
				close();
			}
		} else {
			System.out.println("!!!DEBUG FILE ERROR!!! - File Not Open");
		}
	}

	public void newline() {
		if(isOpen()) {
			try {
				final String NEW_LINE = _LineEnding.getValue();

				synchronized(_LOCK) {
					//If String(DATE_AND_TIME) Length Is Greater Than Buffer Than
					//Flush Buffer Than Write Directly To File
					if(NEW_LINE.length() > _Buffer.length) {
						flushBuffer();
						_OStream.writeString(NEW_LINE);
					} else {
						//Will String(DATE_AND_TIME) Fit In Buffer If Not Flush Buffer
						if((NEW_LINE.length() + _BufferTop) >= _Buffer.length) {
							flushBuffer();
						}

						for(int X = 0; X < NEW_LINE.length(); X++) {
							_Buffer[_BufferTop++] = NEW_LINE.charAt(X);
						}
					}
				}
			} catch(Exception e) {
				System.out.println("!!!DEBUG FILE ERROR!!! - writeln(String str)");
				close();
			}
		} else {
			System.out.println("!!!DEBUG FILE ERROR!!! - File Not Open");
		}
	}

	public boolean isOpen() {
		return (_OStream != null);
	}

	public boolean isClosed() {
		return (_OStream == null);
	}

	@Override
	public void close() {
		if(isOpen()) {
			try {
				synchronized(_LOCK) {//Maybe Not
					flushBuffer();
					_OStream.flush();
					_OStream.close();
					_OStream = null;
				}
			} catch(Exception e) {
				System.out.println("!!!DEBUG FILE ERROR!!! - close()");
				_OStream = null;
			}
		}
	}

	private void flushBuffer() {
		if(isOpen()) {
			try {
				if(_BufferTop > 0) {
					_OStream.writeInts(_Buffer, 0, _BufferTop);
				}

				_BufferTop = 0;
			} catch(Exception e) {
				System.out.println("!!!DEBUG FILE ERROR!!! - flushBuffer()");
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		close();

		super.finalize();
	}
/*
    public static void main(String[] args) {
        BufferedDebugOutput BDebugOutput = new BufferedDebugOutput("C:\\Documents and Settings\\Dalton Dell\\Desktop\\Debug Test.txt", false, false, 16);
        
        BDebugOutput.open();
        
        BDebugOutput.write("Justin");
        
        BDebugOutput.write("Palinkas");

        BDebugOutput.close();
    }
*/
}