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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * <pre>
 *  Example Read:
 * 
 *  ArgumentFileInputStream IStream = null;
 *  try {
 *      IStream = new ArgumentFileInputStream(_File);
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
public class ArgumentFileInputStream extends ArgumentInputStream {
	private final File _FILE;

	public ArgumentFileInputStream(String path) throws FileNotFoundException {
		this(path, _DEFAULT_COMMENT_START_);
	}

	public ArgumentFileInputStream(File file) throws FileNotFoundException {
		this(file, _DEFAULT_COMMENT_START_);
	}

	public ArgumentFileInputStream(String path, String commentstart) throws FileNotFoundException {
		this(new File(path), commentstart);
	}

	public ArgumentFileInputStream(File file, String commentstart) throws FileNotFoundException {
		super(new FileInputStream(file));

		_FILE = file;
	}

	public File getFile() {
		return _FILE;
	}
}