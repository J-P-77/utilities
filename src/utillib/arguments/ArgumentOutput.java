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

package utillib.arguments;

import utillib.file.FileUtil.Line_Ending;

import utillib.strings.MyStringBuffer;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 
 * @author Dalton Dell
 */
public class ArgumentOutput extends ArgumentsShared {
	protected Line_Ending _LineEndings = Line_Ending.WINDOWS;

	private MyStringBuffer _BulidString = null;
	private String _Spacer = "";

	private OutputStream _OStream;

	public ArgumentOutput(OutputStream ostream) {
		if(ostream == null) {
			throw new RuntimeException("Variable[ostream] - Is Null");
		}

		_OStream = ostream;
	}

	public void addToBuiltStr(char c) {
		if(_BulidString == null) {
			_BulidString = new MyStringBuffer();
		}

		_BulidString.append(c);
		_BulidString.append(_Spacer);
	}

	public void addToBuiltStr(String str) {
		if(_BulidString == null) {
			_BulidString = new MyStringBuffer(str.length() + _Spacer.length());
		}

		_BulidString.append(str);
		_BulidString.append(_Spacer);
	}

	public void setBulidSpacer(String spacer) {
		_Spacer = spacer;
	}

	public void setBulidSpacer(char spacer) {
		_Spacer = Character.toString(spacer);
	}

	public void resetBuiltString() {
		if(_BulidString != null) {
			_BulidString.reset();
		}
	}

	public void eraseBuiltString() {
		_BulidString = null;
	}

	public String getBuiltString() {
		if(_BulidString == null) {
			return "";
		}

		if(_BulidString.endsWith(_Spacer)) {
			return _BulidString.getSubString(0, _BulidString.length() - _Spacer.length());
		} else {
			return _BulidString.toString();
		}
	}

	public void setLineEnding(Line_Ending lineending) {
		_LineEndings = lineending;
	}

	public Line_Ending getLineEnding() {
		return _LineEndings;
	}

	/**
	 * 
	 * @return true if read was successful, false if not
	 */
	public boolean write() {
		return write(false);
	}

	/**
	 * 
	 * @param autoclose
	 *            will close stream after write
	 * @return true if read was successful, false if not
	 */
	public boolean write(boolean autoclose) {
//        byte[] NewLine = _LineEndings.getBytes();
//        byte[] CommentStart = _CommentLineStart.getBytes();

		boolean NoErrors = true;
		try {
			for(int X = 0; X < _Titles.length(); X++) {
				Title TempTitle = _Titles.getItemAt(X);

				//Write's Title (Don't Write Comment Title's)
				if(!TempTitle.isComment()) {
//					byte[] Title = (TempTitle.getName()).getBytes();

					_OStream.write('[');
					writeString(TempTitle.getName());
//                    _OStream.write(Title);
					_OStream.write(']');

//                    _OStream.write(NewLine);
					writeString(_LineEndings.getValue());
				}

				//Write's Arguments
				for(int Y = 0; Y < TempTitle.length(); Y++) {
					Argument Arg = TempTitle.getArgument(Y);

					//Write's Name
					if(Arg.isComment()) {
						writeString(_CommentLineStart);
//                        _OStream.write(CommentStart);
					} else {
						writeString(Arg.getName());

//                        byte[] ArgName = Arg.getName().getBytes();

//                        _OStream.write(ArgName);

						//Write's Separator
						_OStream.write('=');
					}

					//Write's Variable
					writeString(Arg.getVariable());

//                    byte[] ArgVariable = Arg.getVariable().getBytes();
//                    _OStream.write(ArgVariable);

					writeString(_LineEndings.getValue());
//                    _OStream.write(NewLine);
				}

				//Write's Blank Line
				writeString(_LineEndings.getValue());
//                _OStream.write(NewLine);
			}
		} catch(Exception e) {
			System.out.println("!!!ERROR!!!" + e.getMessage());
			NoErrors = false;
		} finally {
			if(autoclose) {
				close();
			}
		}

		return NoErrors;
	}

	public void close() {
		try {
			if(_OStream != null) {
				_OStream.flush();
				_OStream.close();
				_OStream = null;
			}
		} catch(Exception i) {}
	}

	private void writeString(String str) throws IOException {
		for(int X = 0; X < str.length(); X++) {
			_OStream.write(str.charAt(X));
		}
	}
}
