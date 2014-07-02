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

import utillib.file.FileUtil.Line_Ending;

import java.io.File;
import java.io.FileOutputStream;

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
public class WriteSettingFile extends SettingShared {
	public WriteSettingFile(String filename) {
		this(new File(filename));
	}

	public WriteSettingFile(File file) {
		super(file, Line_Ending.WINDOWS);
	}

	public void addArgument(String name, String variable) {
		addArgument(new Argument(name, variable));
	}

	public void addArgument(String fullargument) {
		String[] Split = fullargument.split("=", 2);
		addArgument(new Argument(Split[0], Split[1]));
	}

	public void addArgument(Argument argument) {
		if(argument != null) {
			int ArgIndex = findArgIndex(argument.getName());

			if(ArgIndex == -1) {
				_Arguments.push(argument);
			} else {
				_Arguments.setItemAt(argument, ArgIndex);
			}
		}
	}

	public void addComment(String comment) {
		addComment(new Argument("//", comment));
	}

	public void addComment(Argument comment) {
		if(comment != null) {
			if(!comment.getName().equals("//")) {
				comment.setName("//");
			}
			_Arguments.push(comment);
		}
	}

	public void writeFile() {
		byte[] NewLine = (_FILE_ENDING.getValue()).getBytes();

		FileOutputStream oStream = null;
		try {
			oStream = new FileOutputStream(_FILE);

			//WRITE'S ARGUMENTS
			for(int Y = 0; Y < _Arguments.length(); Y++) {
				byte[] Bytes = (_Arguments.getItemAt(Y).toString()).getBytes();
				oStream.write(Bytes);

				oStream.write(NewLine);
			}

			//WRITE'S BLANK LINE
			//fWriter.write("");
		} catch(Exception e) {
			System.out.println("!!!ERROR!!! - " + e.getMessage());
		} finally {
			try {
				if(oStream != null) {
					oStream.close();
					oStream = null;
				}
			} catch(Exception i) {}
		}
	}
	/*
	public static void main(String[] args) {
		WriteSettingFile Setting = new WriteSettingFile("C:\\Documents and Settings\\Dalton Dell\\Desktop\\Test 1.stf");
		
		Setting.addArgument("First Name=Justin");
		Setting.addArgument("Last Name=Palinkas");
		
		Setting.writeFile();
	}
	*/
}