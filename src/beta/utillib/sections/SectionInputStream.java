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

package beta.utillib.sections;

import utillib.file.FileUtil;

import utillib.strings.MyStringBuffer;

import java.io.IOException;
import java.io.InputStream;

import java.util.regex.Matcher;

/**
 * <pre>
 * File Structure:
 * 
 * [NAME1]
 * Key1=Value
 * Key2=Value
 * Key3=Value
 * 
 * [NAME2]
 * Key1=Value
 * Key2=Value
 * Key3=Value
 * 
 * 
 *  Example Read:
 * 
 *  SectionInputStream IStream = null;
 *  try {
 *      IStream = new SectionInputStream(new FileInputStream(null));
 * 
 *      Section Section = null;
 *      while((Section = IStream.readSection()) != null) {
 *          SectionProperty Prop = null;
 *          while((Prop = IStream.readProperty()) != null) {
 *              Section.addProperty(Prop);
 *          }
 * 
 *          //Add Section To A List Or Something
 *      }
 *  } catch (Exception e) {
 *      System.out.println("!!!ERROR!!! - " + ex.getMessage());
 *  } finally {
 *      if(IStream != null) {
 *          try {
 *              IStream.close();
 *          }catch (Exception e) {}
 *          IStream = null;
 *      }
 *  }
 * 
 * </pre>
 * 
 * @author Justin Palinkas
 */
public class SectionInputStream extends InputStream implements SectionConstants {
	private final String _COMMENT_START;

	private InputStream _IStream = null;

	private MyStringBuffer _LineBuffer = new MyStringBuffer();
	private boolean _PreviousCharCR = false;

	public SectionInputStream(InputStream istream) {
		this(istream, _DEFAULT_COMMENT_START_);
	}

	public SectionInputStream(InputStream istream, String commentstart) {
		_IStream = istream;
		_COMMENT_START = commentstart;
	}

	@Override
	public int read() throws IOException {
		return _IStream.read();
	}

	@Override
	public int read(byte[] b) throws IOException {
		return _IStream.read(b);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return _IStream.read(b, off, len);
	}

	@Override
	public void close() throws IOException {
		_IStream.close();
	}

	@Override
	public int available() throws IOException {
		return _IStream.available();
	}

	public String getCommentLineStart() {
		return _COMMENT_START;
	}

	public Section readSection() throws IOException {
		return readSection(false);
	}

	public Section readSection(boolean autocase) throws IOException {
		if(_LineBuffer.length() == 0) {
			while(readln(_LineBuffer, true)) {
				if(isBlankLine(_LineBuffer)) {
					continue;
				} else if(isCommentLine(_LineBuffer)) {
					continue;
				} else {
					break;
				}
			}
		}

		if(isSection(_LineBuffer)) {
			//TODO (AutoCase - Auto Capitailizes The First Letter)
			if(autocase) {
				final String NAME = _LineBuffer.getSubString(1, _LineBuffer.length() - 1);

				if(NAME.length() > 0) {
					return new Section(Character.toUpperCase(NAME.charAt(0)) + NAME.substring(1, NAME.length() - 1));
				} else {
					return new Section(NAME);
				}
			} else {
				return new Section(_LineBuffer.getSubString(1, _LineBuffer.length() - 1));
			}
		}

		return null;
	}

	public void readProperties(Section section) throws IOException {
		while(readln(_LineBuffer, true)) {
			if(isBlankLine(_LineBuffer)) {
				continue;
			} else if(isCommentLine(_LineBuffer)) {
				continue;
			} else if(isProperty(_LineBuffer)) {
				section.addProperty(_LineBuffer.toString());
				_LineBuffer.reset();
			} else {
				break;
			}
		}
	}

	public SectionProperty readSectionProperty() throws IOException {
		while(readln(_LineBuffer, true)) {
			if(isBlankLine(_LineBuffer)) {
				continue;
			} else if(isCommentLine(_LineBuffer)) {
				continue;
			} else if(isProperty(_LineBuffer)) {
				final String[] SPLIT = _LineBuffer.toString().split("=", 2);

				return new SectionProperty(SPLIT[0], SPLIT[1]);
			} else {
				break;
			}
		}

		return null;
	}

	private boolean readln(MyStringBuffer line, boolean reset) throws IOException {
		if(reset) {
			_LineBuffer.reset();
		}

		int CurrentChar = -1;
		//r = 13, n = 10 =  [\r and \n] or [\r] or [\n]
		while((CurrentChar = _IStream.read()) != -1) {
			if(CurrentChar == FileUtil._LF_) {//Unix or Windows
				if(!_PreviousCharCR) {//Unix
					return true;
				} else {
					continue;//Windows (Skip Character[\n])
				}
			}

			if(CurrentChar == FileUtil._CR_) {//Mac
				_PreviousCharCR = true;
				return true;
			} else {
				_PreviousCharCR = false;
			}

			line.append((char)CurrentChar);//PreviousCharCR = false;
		}

		return (line.length() > 0 ? true : false);
	}

	private boolean isCommentLine(MyStringBuffer str) {
		return str.startsWith(_COMMENT_START);
	}

	private static boolean isBlankLine(MyStringBuffer str) {
		return str.length() == 0;
	}

	private static boolean isProperty(MyStringBuffer str) {
		final Matcher MATCHER = _PROPERTY_PATTERN_.matcher(str.toString());

		return MATCHER.matches();
//        return str.contains('=');
	}

	private static boolean isSection(MyStringBuffer str) {
		final Matcher MATCHER = _SECTION_PATTERN_.matcher(str.toString());

		return MATCHER.matches();
//        return str.startsWith('[') && str.endsWith(']');
	}
}