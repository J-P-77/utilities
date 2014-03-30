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

package utillib.file;

import utillib.interfaces.IMyInputStream;

import utillib.utilities.BitOperations.Byte_Ordering;

import utillib.io.MyInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * <pre>
 * <b>Current Version 1.0.4</b>
 * 
 * April 01, 2009 (version 1.0.1)
 *     -Updated
 *         -EveryThing
 * 
 * May 06, 2009 (version 1.0.2)
 *     -Updated
 *         -Method readln (Would Read Line Incorrectly)
 * 
 * May 06, 2009 (version 1.0.3)
 *     -Updated
 *         -Internal Stuff
 *     -Fixed Bug
 *         -One Of The write Method Would Actually Write A New Line
 * 
 * March 29, 2010 (version 1.0.4)
 *     -Updated
 *         -Exported Everything To abstract class AMyInputStream
 * 
 * <b>default ordering:</b> Little Endian
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class MyFileInputStream extends MyInputStream implements IMyInputStream {
	private final File _FILE;

	public MyFileInputStream(String path) throws FileNotFoundException {
		this(path, Byte_Ordering.LITTLE_ENDIAN);
	}

	public MyFileInputStream(String filepath, Byte_Ordering ordering) throws FileNotFoundException {
		this(new File(filepath), ordering);
	}

	public MyFileInputStream(File file) throws FileNotFoundException {
		this(file, Byte_Ordering.LITTLE_ENDIAN);
	}

	public MyFileInputStream(File file, Byte_Ordering ordering) throws FileNotFoundException {
		super(new FileInputStream(file), ordering);

		_FILE = file;
	}

	public long length() {
		return _FILE.length();
	}

	public File getFile() {
		return _FILE;
	}

//    public static void main(String[] args) {
//        final String[] PATHS = {
//            "C:\\Documents and Settings\\Dalton Dell\\Desktop\\Temp.txt"
//        };
//
//        MyFileInputStream IStream = null;
//        try {
//            IStream = new MyFileInputStream(PATHS[0]);
//            
//        } catch (Exception e) {
//            System.out.println("!!ERROR!!! - Cause, " + e.getMessage());
//        } finally {
//            try {
//                if(IStream != null) {
//                    IStream.close();
//                    IStream = null;
//                }
//            } catch (Exception e) {}
//        }
//    }
}
