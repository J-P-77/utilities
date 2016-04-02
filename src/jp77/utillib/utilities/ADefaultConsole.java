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

package jp77.utillib.utilities;

import jp77.utillib.utilities.AConsole;

public abstract class ADefaultConsole extends AConsole {

	@Override
	public void processLine(String line) {
		if(line.length() == 0) {
			return;
		}

		final String CMD;
		final String PARAMETER;

		final int INDEX = line.indexOf(' ');
		if(INDEX == -1) {
			CMD = line.toLowerCase();
			PARAMETER = null;
		} else {
			CMD = line.substring(0, INDEX).toLowerCase();
			PARAMETER = line.substring(INDEX + 1);
		}

		if("help".equals(CMD) || "?".equals(CMD) || "listcmds".equals(CMD)) {
			helpCmd();
		} else if("exit".equals(CMD) || "quit".equals(CMD)) {
			exitCmd();
			super.close();
		} else {
			if(!processCommand(CMD, PARAMETER)) {
				System.out.println("Command: " + CMD + " Does Not Exists");
			}
		}
	}

	/**
	 * 
	 * @param cmd
	 * @param parameters
	 * @return true if command exists, false if not
	 */
	public abstract boolean processCommand(String cmd, String parameters);

	public abstract void helpCmd();

	public abstract void exitCmd();
}
