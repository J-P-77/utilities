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

import utillib.collections.MyStackAll;

import utillib.file.FileUtil.Line_Ending;

import java.io.File;

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
public class SettingShared extends ArgumentsUtil {
	protected final File _FILE;
	protected final Line_Ending _FILE_ENDING;// = Line_Ending.WIN

	protected MyStackAll<Argument> _Arguments = new MyStackAll<Argument>();

	public SettingShared(File file) {
		this(file, Line_Ending.WINDOWS);
	}

	public SettingShared(File file, Line_Ending lineending) {
		if(file == null) {
			throw new RuntimeException("Variable[file] - Is Null");
		}

		if(lineending == null) {
			throw new RuntimeException("Variable[lineending] - Is Null");
		}

		_FILE = file;
		_FILE_ENDING = lineending;
	}

	public Line_Ending getLineEnding() {
		return _FILE_ENDING;
	}

	public int findArgIndex(String argumentname) {
		if(_Arguments != null) {
			for(int X = 0; X < _Arguments.length(); X++) {
				if(_Arguments.getItemAt(X).getName().equals(argumentname)) {
					return X;
				}
			}

		}

		return -1;
	}

	public Argument getArg(String argumentname) {
		if(_Arguments != null) {
			for(int X = 0; X < _Arguments.length(); X++) {
				if(_Arguments.getItemAt(X).getName().equals(argumentname)) {
					return _Arguments.getItemAt(X);
				}
			}
		}

		return null;
	}

	public String findArg(String argumentname) {
		if(_Arguments != null) {
			for(int X = 0; X < _Arguments.length(); X++) {
				if(_Arguments.getItemAt(X).getName().equals(argumentname)) {
					return _Arguments.getItemAt(X).getVariable();
				}
			}
		}

		return null;
	}
}