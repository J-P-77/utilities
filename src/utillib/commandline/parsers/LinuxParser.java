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

package utillib.commandline.parsers;

import utillib.strings.MyStringBuffer;
import utillib.strings.StringUtil;

import utillib.commandline.ACmdLineHandler;
import utillib.lang.byref.IntByRef;

/**
 * Examples. -openserver server1:local:1234 -openserver:server1:local:1234 -file
 * "text 1.txt" /include
 * 
 * @author Justin Palinkas
 * 
 */
public class LinuxParser extends ACmdLineHandler {
	public static final String _DEFAULT_SWITCH_START_ = "-";

	private final String _SWITCH_START;
	private final char _VARIABLE_SEP;

	public LinuxParser() {
		this(_DEFAULT_SWITCH_START_, ':');
	}

	public LinuxParser(String swistart) {
		this(swistart, ':');
	}

	public LinuxParser(String swistart, char variablesep) {
		super();

		if(swistart == null) {
			throw new NullPointerException("Variable[swistart] - Is Null");
		}

		_SWITCH_START = swistart;
		_VARIABLE_SEP = variablesep;
	}

	public boolean parse(String args) {
		if(args == null) {
			throw new NullPointerException("Variable[args] - Is Null");
		}

		final MyStringBuffer BUFFER = new MyStringBuffer(args.length());

		for(IntByRef X = new IntByRef(0); X.value < args.length();) {
			StringUtil.skipsWhiteSpaces(args, X);

			readCmdName(BUFFER, args, X);

			final String NAME = BUFFER.toString(true);

			final ACmdLineHandler.Cmd CMD = super.findCommand(NAME);

			if(CMD == null) {
				super.addCommand(NAME);
			}
		}

		return true;
	}

	public boolean parse(String[] args) {
		return parse(combine(args));
	}

	/**
	 * 
	 * @return swistart String Start Of SwitchArgument (ex. /)
	 */
	public String getSwitchStart() {
		return _SWITCH_START;
	}

	private void readCmdName(MyStringBuffer cmdinfo, String args, IntByRef offset) {
		boolean InQuotes = false;
		for(; offset.value < args.length(); offset.value++) {
			if(args.charAt(offset.value) == '"') {
				InQuotes = !InQuotes;
				continue;
			}

			if(!InQuotes) {
				if(args.charAt(offset.value) == ' ') {
					break;
				} else if(args.charAt(offset.value) == _VARIABLE_SEP) {
					cmdinfo.append(args.charAt(offset.value));
					continue;
				}
			}

			cmdinfo.append(args.charAt(offset.value));
		}
	}

	public static String combine(String[] args) {
		final MyStringBuffer BUFFER = new MyStringBuffer(8 * args.length);

		for(int X = 0; X < args.length; X++) {
			BUFFER.append(args[X]);
			BUFFER.append(' ');
		}

		return BUFFER.toString();
	}

	public static void main(String[] args) {
		final String[][] TESTS = {
				//Arg Start, Swt Start, Args
//            new String[] {_DEFAULT_SWITCH_START_, "-open-server server1:localhost:1234"},
//            new String[] {_DEFAULT_SWITCH_START_, "-open-server:server2:localhost:7777"},
		new String[] {_DEFAULT_SWITCH_START_, "/cmd -file C:\\text 1.txt /gui -file \"C:\\text 2.txt\""}, new String[] {_DEFAULT_SWITCH_START_, "-overwrite \"C:\\text 1.txt\" \"C:\\text 2.txt\""}};

		for(int X = 0; X < TESTS.length; X++) {
			System.out.println("TEST " + X + " CommandLine[" + TESTS[X][1] + "]");
			final ACmdLineHandler CMDLINE = new LinuxParser(TESTS[X][0]);

			CMDLINE.parse(TESTS[X][1]);

			for(int Y = 0; Y < CMDLINE.length(); Y++) {
				final ACmdLineHandler.Cmd CMD = CMDLINE.getCommandAt(Y);

				System.out.println("    " + "Name: " + CMD.getName());
				for(int Z = 0; Z < CMD.variableCount(); Z++) {
					System.out.println("        Var " + Z + ": " + CMD.getVariableAt(Z));
				}

				if(Y < (CMDLINE.length() - 1)) {
					System.out.println();
				}
			}
			System.out.println();
		}
	}
}