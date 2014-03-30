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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
public class ReadArgumentFile extends ArgumentInput {
	protected final File _File;

	public ReadArgumentFile(String file) throws FileNotFoundException {
		this(new File(file), false);
	}

	public ReadArgumentFile(String file, boolean autoread) throws FileNotFoundException {
		this(new File(file), false);
	}

	public ReadArgumentFile(File file) throws FileNotFoundException {
		this(file, false);
	}

	public ReadArgumentFile(File file, boolean autoread) throws FileNotFoundException {
		super(new FileInputStream(file));

		if(file == null) {
			throw new RuntimeException("Variable[file] - Is Null");
		}

		_File = file;

		if(autoread) {
			read();
		}
	}

	public File getFile() {
		return _File;
	}

	public static ReadArgumentFile create(String file) {
		return create(new File(file));
	}

	public static ReadArgumentFile create(File file) {
		try {
			return new ReadArgumentFile(file);
		} catch(Exception e) {}

		return null;
	}

//    public boolean read() {
//        return read(false);
//    }
//
//    //returns true if read was successful
//    public boolean read(boolean autocase) {
//        if(hasRead()) {
//            return false;
//        } else if(!_File.exists()) {
//            System.out.println("!!!ERROR!!! - File: " + _File.getPath() + " Does Not Exists");
//        } else if(!_File.isFile()) {
//            System.out.println("!!!ERROR!!! - Path: " + _File.getPath() + " Is Not A File");
//        } else {
//            ArgumentFileInputStream IStream = null;
//            boolean NoErrors = true;
//            try {
//                IStream = new ArgumentFileInputStream(_File);
//
//                Title CurTitle = null;
//                while((CurTitle = IStream.readTitle(autocase)) != null) {
//                    Argument CurArgument = null;
//                    while((CurArgument = IStream.readArgument()) != null) {
//                        CurTitle.addArgument(CurArgument);
//                    }
//
//                    addTitle(CurTitle);
//                }
//            } catch (Exception ex) {
//                System.out.println("!!!ERROR!!! - " + ex.getMessage());
//                NoErrors = false;
//            } finally {
//                try {
//                    if(IStream != null) {
//                        IStream.close();
//                        IStream = null;
//                    }
//                }catch (Exception i) {}
//            }
//            _PreviousChar = -2;//Indicate That File Has Been Read
//            return NoErrors;
//        }
//
//        return false;
//    }
}