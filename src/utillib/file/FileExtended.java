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

package utillib.file;

import java.io.File;

import java.net.URI;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class FileExtended extends File {

	public FileExtended(String file) {
		super(file);
	}

	public FileExtended(URI file) {
		super(file);
	}

	public FileExtended(String parent, String child) {
		super(parent, child);
	}

	public String getNameWithOutExtension() {//getNameWithOutExtension()
		return getNameWithOutExtension(super.getName());
	}

	public String getExtension() {//getExtension()
		return getExtension(super.getName());
	}

	public String getExtensionWithPeriod() {//getExtensionWithPeriod()
		return getExtensionWithPeriod(super.getName());
	}

	public String getDrive() {
		return getDrive(super.getPath());
	}

	//STATIC
	public static String getDrive(String path) {
		if(path == null) {
			throw new RuntimeException("Variable[path] - Is Null");
		}

		final File DRIVE = getDriveFile(path);

		return (DRIVE == null ? null : DRIVE.getPath());
	}

	public static File getDriveFile(String path) {
		if(path == null) {
			throw new RuntimeException("Variable[path] - Is Null");
		}

		if(path.length() > 0) {
			final File[] DRIVES = File.listRoots();
			for(int X = 0; X < DRIVES.length; X++) {
				if(path.startsWith(DRIVES[X].getPath())) {
					return DRIVES[X];
				}
			}
		}

		return null;
	}

	public static String getName(String path) {//getNameWithOutExtension()
		if(path == null) {
			throw new RuntimeException("Variable[path] - Is Null");
		}

		final int INDEX = path.lastIndexOf('/');

		return (INDEX != -1 ? path.substring(INDEX + 1, path.length()) : path);
	}

	public static String getNameWithOutExtension(String path) {//getNameWithOutExtension()
		if(path == null) {
			throw new RuntimeException("Variable[path] - Is Null");
		}

		final int INDEX = path.lastIndexOf('.');

		return (INDEX != -1 ? path.substring(0, INDEX) : path);
	}

	public static String getExtension(String path) {//getExtension()
		if(path == null) {
			throw new RuntimeException("Variable[path] - Is Null");
		}

		final int INDEX = path.lastIndexOf('.');

		return (INDEX == -1 ? "" : path.substring(INDEX + 1, path.length()));
	}

	public static String getExtensionWithPeriod(String path) {//getExtensionWithPeriod()
		if(path == null) {
			throw new RuntimeException("Variable[path] - Is Null");
		}

		return '.' + getExtension(path);
	}
/*
    public static void main(String[] args) {
        FileExtended Fex = new FileExtended("C:\\Documents and Settings\\Dalton Dell\\Desktop\\jvftp.session");
        
        System.out.println("getDrive()                - " + Fex.getDrive());
        System.out.println("getNameWithOutExtension() - " + Fex.getNameWithOutExtension());
        System.out.println("getExt()                  - " + Fex.getExtension());
        System.out.println("getExtensionWithPeriod()  - " + Fex.getExtensionWithPeriod());
    }
*/
}
