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

import utillib.arrays.ResizingArray;

import utillib.file.FileUtil.Line_Ending;

import java.io.IOException;
import java.io.OutputStream;

public class ArgumentOutputStream extends OutputStream {
	public static final String _DEFAULT_COMMENT_START_ = "//";

	private final String _COMMENT_START;
	private final Line_Ending _LINE_ENDING;

	private OutputStream _OStream;

	private Title _Current_Title = null;

	public ArgumentOutputStream(OutputStream ostream, String commentstart) {
		this(ostream, commentstart, Line_Ending.WINDOWS);
	}

	public ArgumentOutputStream(OutputStream ostream, Line_Ending lineending) {
		this(ostream, _DEFAULT_COMMENT_START_, lineending);
	}

	public ArgumentOutputStream(OutputStream ostream, String commentstart, Line_Ending lineending) {
		if(ostream == null) {
			throw new RuntimeException("Variable[ostream] - Is Null");
		}

		if(lineending == null) {
			throw new RuntimeException("Variable[lineending] - Is Null");
		}

		_OStream = ostream;
		_COMMENT_START = (commentstart == null ? _DEFAULT_COMMENT_START_ : commentstart);
		_LINE_ENDING = lineending;
	}

	@Override
	public void write(int b) throws IOException {
		_OStream.write(b);
	}

	@Override
	public void flush() throws IOException {
		_OStream.flush();
	}

	@Override
	public void close() throws IOException {
		if(_OStream != null) {
			_OStream.close();
		}
		_OStream = null;
	}

	public Line_Ending getLineEnding() {
		return _LINE_ENDING;
	}

	public String getCommentLineStart() {
		return _COMMENT_START;
	}

	public void writeTitle(Title title) throws IOException {
		writeTitle(title, true, false);
	}

	public void writeTitle(Title title, boolean writearguments) throws IOException {
		writeTitle(title, writearguments, false);
	}

	public void writeTitle(Title title, boolean writearguments, boolean autocase) throws IOException {
		//Write's Title (Don't Write Comment Title's)
		if(!title.isComment()) {
			_Current_Title = title;

			write('[');
			if(autocase) {
				final String NAME = title.getName();

				if(NAME.length() > 1) {
					this.write(Character.toUpperCase(NAME.charAt(0)));
					this.write(NAME.substring(1, NAME.length() - 1));
				} else {
					this.write(NAME);
				}
			} else {
				this.write(title.getName());
			}
			write(']');

			newline();

			if(writearguments) {
				//Write's Arguments
				for(int Y = 0; Y < title.length(); Y++) {
					writeArgument(title.getArgument(Y));
				}
			}
		}
	}

	public Title writeTitleName(String name) throws IOException {
		final Title TITLE = new Title(name);

		writeTitle(TITLE, false, false);

		return (_Current_Title = TITLE);
	}

	public void writeArgument(Argument argument) throws IOException {
		if(_Current_Title == null) {
			throw new RuntimeException("Variable[argument] - You Must Write A Title First");
		} else {
			_Current_Title.addArgument(argument);

			//Write's Name
			if(argument.isComment()) {
				this.write(_COMMENT_START);
			} else {
				this.write(argument.getName());

				//Write's Separator
				write('=');
			}

			//Write's Variable
			this.write(argument.getVariable());

			newline();
		}
	}

	public void writeArgument(String name, String variable) throws IOException {
		writeArgument(new Argument(name, variable));
	}

	public void writeComment(String comment) throws IOException {
		if(_Current_Title == null) {
			throw new RuntimeException("Variable[comment] - You Must Write A Title First");
		} else {
			_Current_Title.addComment(comment);
			//Write's Name
			this.write(_COMMENT_START);
			//Write's Variable
			this.write(comment);

			newline();
		}
	}

//    private void write(char[] chars) throws IOException {
//        write(chars, 0, chars.length);
//    }

//    private void write(char[] chars, int offset, int length) throws IOException {
//        for(int X = offset; X < length; X++) {
//            write((int)chars[X]);
//        }
//    }

	private void write(String str) throws IOException {
		write(str, 0, str.length());
	}

	private void write(String str, int offset, int length) throws IOException {
		for(int X = offset; X < length; X++) {
			write((int)str.charAt(X));
		}
	}

	public void newline() throws IOException {
		write(_LINE_ENDING.getValue());
	}

	public static boolean saveStream(ResizingArray<Title> titles, OutputStream ostream, String commentstart) {
		ArgumentOutputStream OStream = null;

		try {
			OStream = new ArgumentOutputStream(OStream, commentstart);

			for(int X = 0; X < titles.length(); X++) {
				OStream.writeTitle(titles.getAt(X), true, false);
			}

			return true;
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(OStream != null) {
				try {
					OStream.close();
				} catch(Exception e2) {}
				OStream = null;
			}
		}

		return false;
	}
}
