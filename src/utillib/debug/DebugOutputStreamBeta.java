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

import utillib.strings.MyStringBuffer;

import utillib.file.FileUtil;

import java.io.File;
import java.io.FileOutputStream;

import java.text.SimpleDateFormat;

import java.util.Date;

/**
 * <pre>
 * <b>Does Not Work Yet</b>
 * <b>Current Version 1.0.0</b>
 * 
 * December 22, 2009 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class DebugOutputStreamBeta {
	public static final String _DEFAULT_DATE_FORMAT_ = "MM,dd,yyyy hh-mm-ss aa";

	protected final Object _LOCK = new Object();

	private File _Current_File = null;

	private FileOutputStream _OStream = null;
	private Line_Ending _LineEnding = Line_Ending.LINUX;

	private boolean _InsertDateAndTime = false;
	private boolean _Append = true;
	private boolean _UseTimeFile = false;

	private long _Limit = 0;
	private final long _MAX_LIMIT;

	private final String _PARENT_DIRECTORY;
	private final String _FILENAME;
	private final String _EXTENSION;

	public DebugOutputStreamBeta(String parentdirectory) {
		this(parentdirectory, "Log", ".log", -1, false, true);
	}

	public DebugOutputStreamBeta(String parentdirectory, long limit) {
		this(parentdirectory, "Log", ".log", limit, false, true);
	}

	public DebugOutputStreamBeta(String parentdirectory, String filename, long limit, boolean usetimefile) {
		this(parentdirectory, filename, ".log", limit, false, usetimefile);
	}

	public DebugOutputStreamBeta(String parentdirectory, String filename, String ext, long limit, boolean usetimefile) {
		this(parentdirectory, "Log", ".log", limit, false, true);
	}

	public DebugOutputStreamBeta(String parentdirectory, String filename, String ext, long limit, boolean append, boolean usetimefile) {
		if(parentdirectory == null) {
			throw new RuntimeException("Variable[parentdirectory] - Is Null");
		}

		if(new File(parentdirectory).exists()) {
			_PARENT_DIRECTORY = parentdirectory;
		} else {
			throw new RuntimeException("Variable[parentdirectory] - Parent Path: " + parentdirectory + " - Does Not Exists");
		}

		_FILENAME = (filename == null ? "Log" : filename);

		if(ext == null) {
			_EXTENSION = ".log";
		} else {
			if(ext.startsWith(ext)) {
				_EXTENSION = ext;
			} else {
				_EXTENSION = "." + ext;
			}
		}

		_MAX_LIMIT = limit;
		_Append = (usetimefile ? false : append);
		_UseTimeFile = usetimefile;

		open();
	}

	public File getCurrentFile() {
		return _Current_File;
	}

	public void setUseTimeFile(boolean value) {
		_UseTimeFile = value;
	}

	public boolean getuseTimeFile() {
		return _UseTimeFile;
	}

	public void setInsertDateAndTime(boolean value) {
		_InsertDateAndTime = value;
	}

	public boolean getInsertDateAndTime() {
		return _InsertDateAndTime;
	}

	public void setAppend(boolean value) {
		_Append = value;
	}

	public boolean getAppend() {
		return _Append;
	}

	public void setLineEnding(Line_Ending lineending) {
		_LineEnding = lineending;
	}

	public Line_Ending getLineEnding() {
		return _LineEnding;
	}

	public void write(int i) {
		limitCheck(1);

		if(isOpen()) {
			try {
				if(_InsertDateAndTime) {
					writeDateAndTime();
				}

				synchronized(_LOCK) {
					_Limit += 1;

					_OStream.write(i);
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

	private void limitCheck(long length) {
		if(_Limit != -1) {
			if((_Limit + length) >= _MAX_LIMIT) {//ReOpen A File
				open();
			}
		}
	}

	public void write(int[] ints, int offset, int length) {
		limitCheck(length - offset);

		if(isOpen()) {
			try {
				if(_InsertDateAndTime) {
					writeDateAndTime();
				}

				synchronized(_LOCK) {
					_Limit += (length - offset);

					for(int X = offset; X < length; X++) {
						_OStream.write(ints[X]);
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
		limitCheck(1);

		if(isOpen()) {
			try {
				if(_InsertDateAndTime) {
					writeDateAndTime();
				}

				synchronized(_LOCK) {
					_Limit += 1;

					_OStream.write((int)c);
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
		limitCheck(length - offset);

		if(isOpen()) {
			try {
				if(_InsertDateAndTime) {
					writeDateAndTime();
				}

				synchronized(_LOCK) {
					_Limit += (length - offset);

					for(int X = offset; X < length; X++) {
						_OStream.write((int)chars[X]);
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
		limitCheck(length - offset);

		if(isOpen()) {
			try {
				if(_InsertDateAndTime) {
					writeDateAndTime();
				}

				synchronized(_LOCK) {
					_Limit += (length - offset);

					for(int X = offset; X < length; X++) {
						_OStream.write((int)str.charAt(X));
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

	public void write(StringBuffer str) {
		write(str, 0, str.length());
	}

	public void write(StringBuffer str, int offset, int length) {
		limitCheck(length - offset);

		if(isOpen()) {
			try {
				if(_InsertDateAndTime) {
					writeDateAndTime();
				}

				synchronized(_LOCK) {
					_Limit += (length - offset);

					for(int X = offset; X < length; X++) {
						_OStream.write((int)str.charAt(X));
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
		limitCheck(length - offset);

		if(isOpen()) {
			try {
				if(_InsertDateAndTime) {
					writeDateAndTime();
				}

				synchronized(_LOCK) {
					_Limit += (length - offset);

					for(int X = offset; X < length; X++) {
						_OStream.write((int)str.charAt(X));
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
				final String DATEANDTIME = getDateAndTime();

				synchronized(_LOCK) {
					write(DATEANDTIME);
				}
			} catch(Exception e) {
				System.out.println("!!!DEBUG FILE ERROR!!! - writeDateAndTime");
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

	public void writeln(StringBuffer str) {
		writeln(str, 0, str.length());
	}

	public void writeln(StringBuffer str, int offset, int length) {
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
				final String DATEANDTIME = getDateAndTime();

				synchronized(_LOCK) {
					writeln(DATEANDTIME);
				}
			} catch(Exception e) {
				System.out.println("!!!DEBUG FILE ERROR!!! - writeDateAndTime");
				close();
			}
		} else {
			System.out.println("!!!DEBUG FILE ERROR!!! - File Not Open");
		}
	}

	public void newline() {
		if(isOpen()) {
			try {
				synchronized(_LOCK) {
					write(_LineEnding.getValue());
				}
			} catch(Exception e) {
				System.out.println("!!!DEBUG FILE ERROR!!! - writeln(String str)");
				close();
			}
		} else {
			System.out.println("!!!DEBUG FILE ERROR!!! - File Not Open");
		}
	}

	public void open() {
		if(isOpen()) {
			close();
		}

		_Current_File = (_UseTimeFile ? getTimeFile() : getNextFile());

		if(_Current_File == null) {
			System.out.println("!!!DEBUG FILE ERROR!!! - File Not Set");
		} else {
			try {
				synchronized(_LOCK) {//Maybe Not
					if(_UseTimeFile) {
						_OStream = new FileOutputStream(_Current_File, _Append);
					} else {
						_OStream = new FileOutputStream(_Current_File, _Append);
					}
				}
			} catch(Exception e) {
				System.out.println("!!!DEBUG FILE ERROR!!! - open()");
			}
		}
	}

	public boolean isOpen() {
		return (_OStream != null);
	}

	public boolean isClosed() {
		return (_OStream == null);
	}

	public void close() {
		if(isOpen()) {
//            System.out.println("close(): " + _File.getAbsolutePath());
			try {
				synchronized(_LOCK) {//Maybe Not
					_OStream.flush();
					_OStream.close();
				}
			} catch(Exception e) {
				System.out.println("!!!DEBUG FILE ERROR!!! - close()");
			} finally {
				_OStream = null;
				_Current_File = null;
				_Limit = 0;
			}
		}
	}

	private File getNextFile() {
		File F = null;

		int X = 0;
		while(true) {
			F = new File(mergePaths(_PARENT_DIRECTORY, _FILENAME + X + _EXTENSION));

			if(F.exists()) {
				if(F.length() < _MAX_LIMIT) {
					_Limit = F.length();
					break;
				}
			} else {
				break;
			}

			X++;
		}

		if(F.getPath().length() >= FileUtil._WIN_MAX_PATH_LENGTH_) {
			throw new RuntimeException("Variable[F] - Path Length Greater Than 250");
		}

		return F;
	}

	private File getTimeFile() {
		final File FILE = new File(mergePaths(_PARENT_DIRECTORY, _FILENAME + getDateAndTime() + _EXTENSION));

		if(FILE.getPath().length() > 250) {
			throw new RuntimeException("Variable[F] - Path Length Greater Than 250");
		}

		return FILE;
	}

	private String mergePaths(String parent, String path) {
		boolean AppEndWith = parent.startsWith(FileUtil._S_);

		MyStringBuffer Buffer = new MyStringBuffer(parent.length() + path.length() + 2);

		boolean PathStartWith = path.startsWith(FileUtil._S_);
		boolean PathEndsWith = path.endsWith(FileUtil._S_);

		Buffer.append(parent);

		if(!AppEndWith && !PathStartWith) {
			Buffer.append(FileUtil._S_);
		}

		final int LEN = (PathEndsWith ? (path.length() - 1) : path.length());
		for(int X = 0; X < LEN; X++) {
			Buffer.append(path.charAt(X));
		}

//        if(!_Extension.startsWith(".")) {Buffer.append(".");}
//        Buffer.append(_Extension);

		return Buffer.toString();
	}

	private static String getDateAndTime() {
		return getDateAndTime(_DEFAULT_DATE_FORMAT_);
	}

	private static String getDateAndTime(String pattern) {
		SimpleDateFormat DateFormat = new SimpleDateFormat(pattern);

		return DateFormat.format(new Date());
	}

	@Override
	protected void finalize() throws Throwable {
		close();

		super.finalize();
	}
/*
    public static void main(String[] args) {
        DebugOutputStreamBeta DebugStream = new DebugOutputStreamBeta(
            "C:\\Documents and Settings\\Dalton Dell\\Desktop\\Java Test\\log", "Log Test", 7, false);


        DebugStream.writeln("Justin This is Just A Test");

        

        
        DebugStream.close();
    }
*/
}