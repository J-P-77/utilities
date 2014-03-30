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

import utillib.arrays.ResizingArray;
import utillib.file.FileUtil;

import utillib.strings.MyStringBuffer;

import java.io.IOException;
import java.io.InputStream;

/**
 * <pre>
 *  Example Read:
 * 
 *  ArgumentInputStream IStream = null;
 *  try {
 *      IStream = new ArgumentInputStream(new FileInputStream(_File));
 * 
 *      Title CurTitle = null;
 *      while((CurTitle = IStream.readTitle()) != null) {
 *          Argument CurArgument = null;
 *          while((CurArgument = IStream.readArgument()) != null) {
 *              CurTitle.addArgument(CurArgument);
 *          }
 * 
 *          //Add Title To A List Or Something
 *      }
 *  } catch (Exception ex) {
 *      System.out.println("!!!ERROR!!! - " + ex.getMessage());
 *  } finally {
 *      try {
 *          if(IStream != null) {
 *              IStream.close();
 *              IStream = null;
 *          }
 *      }catch (Exception i) {}
 *  }
 * 
 * </pre>
 * 
 * @author Justin Palinkas
 */
public class ArgumentInputStream extends InputStream {
	public static final String _DEFAULT_COMMENT_START_ = "//";

	private final String _COMMENT_START;

	private boolean _PreviousCharCR = false;

	private MyStringBuffer _LineBuffer = new MyStringBuffer();

	private InputStream _IStream = null;

	public ArgumentInputStream(InputStream istream) {
		this(istream, _DEFAULT_COMMENT_START_);
	}

	public ArgumentInputStream(InputStream istream, String commentstart) {
		if(istream == null) {
			throw new RuntimeException("Variable[istream] - Is Null");
		}

		_IStream = istream;
		_COMMENT_START = (commentstart == null ? _DEFAULT_COMMENT_START_ : commentstart);
	}

	@Override
	public int read() throws IOException {
		return _IStream.read();
	}

	@Override
	public int available() throws IOException {
		return _IStream.available();
	}

	@Override
	public void close() throws IOException {
		if(_IStream != null) {
			_IStream.close();
			_IStream = null;
		}
	}

	public String getCommentLineStart() {
		return _COMMENT_START;
	}

	public Title readTitle() throws IOException {
		return readTitle(false);
	}

	public Title readTitle(boolean autocase) throws IOException {
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

		if(isTitle(_LineBuffer)) {
			//TODO (AutoCase - Auto Capitailizes The First Letter)
			if(autocase) {
				MyStringBuffer Buffer = new MyStringBuffer(_LineBuffer);

				if(Buffer.length() > 0) {
					Buffer.toUpperLetter(0);
				}

				return new Title(Buffer.toString());

				/*final String NAME = _LineBuffer.getSubString(1, _LineBuffer.length() - 1);
				if(NAME.length() > 0) {return new Title(Character.toUpperCase(NAME.charAt(0)) + NAME.substring(1, NAME.length() - 1));
				} else {return new Title(NAME);}*/
			} else {
				//Removes Start Character [ and End Character ]
				return new Title(_LineBuffer.getSubString(1, _LineBuffer.length() - 1));
			}
		}

		return null;
	}

	public void readArguments(Title tile) throws IOException {
		while(readln(_LineBuffer, true)) {
			if(isBlankLine(_LineBuffer)) {
				continue;
			} else if(isCommentLine(_LineBuffer)) {
				continue;
			} else if(isArgument(_LineBuffer)) {
				tile.addArgument(_LineBuffer.toString());
				_LineBuffer.reset();
			} else {
				break;
			}
		}
	}

	public Argument readArgument() throws IOException {
		while(readln(_LineBuffer, true)) {
			if(isBlankLine(_LineBuffer)) {
				continue;
			} else if(isCommentLine(_LineBuffer)) {
				continue;
			} else if(isArgument(_LineBuffer)) {
				return new Argument(_LineBuffer.toString());
			} else {
				break;
			}
		}

		return null;
	}

	public String readln() throws IOException {
		MyStringBuffer Line = new MyStringBuffer();

		if(readln(Line, false)) {
			return Line.toString();
		} else {
			return null;
		}
	}

	private boolean readln(MyStringBuffer line, boolean reset) throws IOException {
		if(reset) {
			_LineBuffer.reset();
		}

		int CurrentChar = -1;
		//r = 13, n = 10 =  [\r and \n] or [\r] or [\n]
		while((CurrentChar = read()) != -1) {
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

	@Override
	protected void finalize() throws Throwable {
		close();

		super.finalize();
	}

	//STATIC
	private static boolean isBlankLine(MyStringBuffer str) {
		return str.length() == 0;
	}

	private static boolean isArgument(MyStringBuffer str) {
		return str.contains('=');
	}

	private static boolean isTitle(MyStringBuffer str) {
		return str.startsWith('[') && str.endsWith(']');
	}

	public static ResizingArray<Title> loadStream(InputStream istream, String commentstart) {
		ArgumentInputStream IStream = null;
		try {
			IStream = new ArgumentInputStream(istream, commentstart);

			final ResizingArray<Title> TITLES = new ResizingArray<Title>(2, 2);

			Title CurTitle = null;
			while((CurTitle = IStream.readTitle()) != null) {
				Argument CurArgument = null;
				while((CurArgument = IStream.readArgument()) != null) {
					CurTitle.addArgument(CurArgument);
				}

				TITLES.put(CurTitle);
			}

			return TITLES;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(IStream != null) {
				try {
					IStream.close();
				} catch(Exception i) {}
				IStream = null;
			}
		}
	}

/*
    public static void main(String[] args) {
        ArgumentFileInputStream IStream = null;

        try {
            IStream = new ArgumentFileInputStream(
                    "C:\\Documents and Settings\\Dalton Dell\\Desktop\\Test File.txt");

//            String Line = null;
//            while((Line = IStream.readln()) != null) {
//                System.out.println(Line);
//            }

            final utillib.arrays.ResizingArray<Title> TITLES = new utillib.arrays.ResizingArray<Title>();

            Title CurTitle = null;
            while((CurTitle = IStream.readTitle()) != null) {
                Argument CurArgument = null;
                while((CurArgument = IStream.readArgument()) != null) {
                    CurTitle.addArgument(CurArgument);
                }

                TITLES.put(CurTitle);
            }

            for(int X = 0; X < TITLES.length(); X++) {
                CurTitle = TITLES.getAt(X);

                System.out.println('[' + CurTitle.getName() + ']');

                Argument CurArgument = null;
                for(int Y = 0; Y < CurTitle.length(); Y++) {
                    CurArgument = CurTitle.getArgument(Y);
                            
                    System.out.println(CurArgument.getName() + '=' + CurArgument.getVariable());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(IStream != null) {
                    IStream.close();
                    IStream = null;
                }
            } catch (Exception e) {}
        }
    }
*/
}
/*
    private int _PreviousChar = -1;

//true if a line has been read, false if not
    public boolean readln(MyStringBuffer line) throws IOException {
        boolean EndofLine = false;
        boolean SkipChar = false;
        int CurrentChar = -1;
        //r = 13, n = 10
        //\r,\n
        //\r
        //\n

        while((CurrentChar = super.read()) != -1) {
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
                return true;
            } else if(SkipChar) {
                SkipChar = false;
            } else {
                line.append((char)CurrentChar);
            }
        }

        return false;
    }
*/
/*
// To Don't Like The Way This Works
    public Title readTitle() throws IOException {
        if(_Line.length() > 0) {
            if(isTitle(_Line)) {
                Title TempTitle = new Title(_Line.getSubstring(1, _Line.length() - 1));

                _Line.reset();

                return TempTitle;
            }
            _Line.reset();
        }

        while(readln(_Line)) {
            if(isBlankLine(_Line)) {
                _Line.reset();
                continue;
            } else if(isCommentLine(_Line)) {
                _Line.reset();
                continue;
            } else if(isTitle(_Line)) {
                Title TempTitle = new Title(_Line.getSubstring(1, _Line.length() - 1));

                _Line.reset();

                return TempTitle;
            } else {
                break;
            }
        }

        return null;
    }
// To Don't Like The Way This Works
    public void readArguments(Title tile) throws IOException {
        //MyStringBuffer Line = new MyStringBuffer();
        if(_Line.length() > 0) {
            if(isArgument(_Line)) {
                tile.addArgument(_Line.toString());
            }
            _Line.reset();
        }

        while(readln(_Line)) {
            if(isBlankLine(_Line)) {
                break;
            } else if(isCommentLine(_Line)) {
                _Line.reset();
                continue;
            } else if(isArgument(_Line)) {
                tile.addArgument(_Line.toString());
                _Line.reset();
            } else {
                break;
            }
        }
    }
// To Don't Like The Way This Works
    public Argument readArgument() throws IOException {
        //MyStringBuffer Line = new MyStringBuffer();

        if(_Line.length() > 0) {
            if(isArgument(_Line)) {

                Argument TempArg = new Argument(_Line.toString());

                _Line.reset();

                return TempArg;
            }
            _Line.reset();
        }

        while(readln(_Line)) {
            if(isBlankLine(_Line)) {
                break;
            } else if(isCommentLine(_Line)) {
                _Line.reset();
                continue;
            } else if(isArgument(_Line)) {
                Argument TempArg = new Argument(_Line.toString());

                _Line.reset();

                return TempArg;
            } else {
                break;
            }
        }

        return null;
    }
*/