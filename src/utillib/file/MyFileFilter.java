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

import utillib.utilities.Os;

import javax.swing.filechooser.FileFilter;

import java.io.File;

/**
 * <pre>
 * <b>Current Version 1.0.2</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * October 13, 2009 (Version 1.0.1)
 *     -Internal Stuff
 * 
 * December 8, 2009 (Version 1.0.2)
 *     -Renaming
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public final class MyFileFilter extends FileFilter {
	private final String _DESCRIPTION;
	private final String[] _EXTENSION;

	public MyFileFilter(String description, String ext) {
		_EXTENSION = new String[1];

		final String FILTER = addPeriod(ext);
		_EXTENSION[0] = (Os.isWindows() ? FILTER.toLowerCase() : FILTER);

		_DESCRIPTION = description + " (*" + _EXTENSION[0] + ')';
	}

	/**
	 * ex. (.txt)
	 */
	public MyFileFilter(String description, String... exts) {
		_EXTENSION = new String[exts.length];

		StringBuffer Description = new StringBuffer(description);

		Description.append(" (");
		for(int X = 0; X < exts.length; X++) {
			final String FILTER = addPeriod(exts[X]);
			_EXTENSION[X] = (Os.isWindows() ? FILTER.toLowerCase() : FILTER);

			Description.append('*');
			Description.append(_EXTENSION[X]);

			if(X != (exts.length - 1)) {
				Description.append(';');
			}
		}
		Description.append(')');

		_DESCRIPTION = Description.toString();
	}

	public String getExtension() {
		return _EXTENSION[0];
	}

	public String getExtensionAt(int index) {
		if(validIndex(index)) {
			return _EXTENSION[index];
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index + " Out Of Bounds");
		}
	}

	public int getExtensionCount() {
		return _EXTENSION.length;
	}

	@Override
	public String getDescription() {
		return _DESCRIPTION;
	}

	@Override
	public boolean accept(File pathname) {
		if(pathname.isDirectory()) {
			return true;
		} else {
			final String FILENAME = (Os.isWindows() ? pathname.getName().toLowerCase() : pathname.getName());

			for(int X = 0; X < _EXTENSION.length; X++) {
				if(FILENAME.endsWith(_EXTENSION[X])) {
					return true;
				}
			}

			return false;
		}
	}

	private boolean validIndex(int index) {
		if(index >= 0 && index < _EXTENSION.length) {
			return true;
		} else {
			return false;
		}
	}

	private static String addPeriod(String filter) {
		if(filter.startsWith(".")) {
			return filter;
		} else {
			return '.' + filter;
		}
	}
}
/*public String getExtension(String path) {
    int Period = path.lastIndexOf('.');

    if(Period == 0) {
        throw new IllegalArgumentException("Path Is A Directory");
    } else {
        return path.substring(Period + 1);
    }
}

public String getExtension(File file) {
    return getExtension(file.getName());
}*/