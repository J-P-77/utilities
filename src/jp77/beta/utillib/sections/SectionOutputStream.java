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

package jp77.beta.utillib.sections;

import jp77.utillib.file.FileUtil.Line_Ending;

import java.io.IOException;
import java.io.OutputStream;

/**
 * File Structure:
 * 
 * [NAME1] Key1=Value Key2=Value Key3=Value
 * 
 * [NAME2] Key1=Value Key2=Value Key3=Value
 * 
 * @author Justin Palinkas
 */
public class SectionOutputStream extends OutputStream implements SectionConstants {
	private final String _COMMENT_START;

	private OutputStream _OStream = null;

	private final Line_Ending _LINE_ENDING;

	public SectionOutputStream(OutputStream ostream) {
		this(ostream, _DEFAULT_COMMENT_START_, Line_Ending.WINDOWS);
	}

	public SectionOutputStream(OutputStream ostream, String commentstart) {
		this(ostream, commentstart, Line_Ending.WINDOWS);
	}

	public SectionOutputStream(OutputStream ostream, Line_Ending lineending) {
		this(ostream, _DEFAULT_COMMENT_START_, lineending);
	}

	public SectionOutputStream(OutputStream ostream, String commentstart, Line_Ending lineending) {
		_OStream = ostream;
		_COMMENT_START = commentstart;
		_LINE_ENDING = lineending;
	}

	@Override
	public void write(byte[] b) throws IOException {
		_OStream.write(b);
	}

	@Override
	public void write(int b) throws IOException {
		_OStream.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		_OStream.write(b, off, len);
	}

	@Override
	public void close() throws IOException {
		_OStream.close();
	}

	@Override
	public void flush() throws IOException {
		_OStream.flush();
	}

	public Line_Ending getLineEnding() {
		return _LINE_ENDING;
	}

	public String getCommentLineStart() {
		return _COMMENT_START;
	}

	public void writeSection(Section section) throws IOException {
		writeSection(section, true, false);
	}

	public void writeSection(Section section, boolean writeproperties) throws IOException {
		writeSection(section, writeproperties, false);
	}

	public void writeSection(Section section, boolean writeproperties, boolean autocase) throws IOException {
		//Write's Title (Don't Write Comment Title's)
		if(!section.isComment()) {
			_OStream.write('[');
			if(autocase) {
				//TODO (AutoCase - Auto Capitailizes The First Letter)
				final String NAME = section.getName();

				if(NAME.length() > 0) {
					this.write(Character.toUpperCase(NAME.charAt(0)) + NAME.substring(1, NAME.length() - 1));
				} else {
					this.write(NAME);
				}
			} else {
				this.write(section.getName());
			}
			_OStream.write(']');

			newline();

			if(writeproperties) {
				//Write's Arguments
				for(int Y = 0; Y < section.length(); Y++) {
					writeProperty(section.getProperty(Y));
				}
			}
		} else {
			for(int Y = 0; Y < section.length(); Y++) {
				writeComment(section.getProperty(Y).getVariable());
			}
		}
	}

	public void writeProperty(SectionProperty argument) throws IOException {
		//Write's Name
		if(argument.isComment()) {
			this.write(_COMMENT_START);
		} else {
			this.write(argument.getName());

			//Write's Separator
			_OStream.write('=');
		}

		//Write's Variable
		this.write(argument.getVariable());

		newline();
	}

	public void writeProperty(String name, String variable) throws IOException {
		//Write's Name
		this.write(name);
		//Write's Separator
		_OStream.write('=');
		//Write's Variable
		this.write(variable);

		newline();
	}

	public void writeComment(String comment) throws IOException {
		//Write's Name
		this.write(_COMMENT_START);
		//Write's Variable
		this.write(comment);

		newline();
	}

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
}
