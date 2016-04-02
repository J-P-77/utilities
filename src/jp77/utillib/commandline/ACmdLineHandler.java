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

package jp77.utillib.commandline;

import jp77.utillib.arrays.ResizingArray;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * October 10, 2011 (Version 1.0.0)
 *     -First Released
 * 
 *  Examples:
 *      -openserver server1:local:1234
 *      -openserver:server1:local:1234
 *      -directory "folder" /include
 *      -file:"text 1.txt"
 *      -file:"text 1.txt":"text 2.txt"
 * 
 * @author Justin Palinkas
 * 
 * <pre>
 */
public abstract class ACmdLineHandler {
	private ResizingArray<Cmd> _Commands = new ResizingArray<Cmd>(5, 2);

//    private boolean _MatchCase = false;
//    
//    public ACmdLineHandler() {}
//    
//    public ACmdLineHandler(boolean matchcase) {
//    	_MatchCase = matchcase;
//	}
//    
//    public void setMatchCase(boolean value) {
//    	_MatchCase = value;
//    }
//    
//    public boolean getMatchCase() {
//    	return _MatchCase;
//    }

	public Cmd addCommand(String cmdname) {
//    	Cmd T_Cmd = findCommand(cmdname);
//    	
//    	if(T_Cmd == null) {
//	    	T_Cmd = new Cmd(cmdname);
//	    	
//	    	_Commands.put(T_Cmd);
//    	}

		return addCommand(false, cmdname);
	}

	public Cmd addCommand(boolean matchcase, String cmdname) {
		Cmd T_Cmd = findCommand(matchcase, cmdname);

		if(T_Cmd == null) {
			T_Cmd = new Cmd(cmdname);

			_Commands.put(T_Cmd);
		}

		return T_Cmd;
	}

	/**
	 * 
	 * @param index
	 *            (int) Index of Command
	 * @return returns Command At Index
	 */
	public Cmd getCommandAt(int index) {
		if(_Commands.validIndex(index)) {
			return _Commands.getAt(index);
		}

		return null;
	}

	/**
	 * 
	 * @return returns int Number of Commands
	 */
	public int length() {
		return _Commands.length();
	}

	/**
	 * @param name
	 *            String Name of Command To Get
	 * @return returns Commands
	 */
	public Cmd findCommand(String name) {
		return findCommand(false, name);
	}

	/**
	 * @param name
	 *            (String) Name of Command To Get
	 * @param matchcase
	 *            (boolean) Match Case Sensitive
	 * @return returns Commands
	 */
	public Cmd findCommand(boolean matchcase, String name) {
		final String COMMAND = (matchcase ? name : name.toLowerCase());

		for(int X = 0; X < _Commands.length(); X++) {
			final String T_CMD = (matchcase ? _Commands.getAt(X).getName() : _Commands.getAt(X).getName().toLowerCase());

			if(COMMAND.equals(T_CMD)) {
				return _Commands.getAt(X);
			}
		}

		return null;
	}

	/**
	 * @param name
	 *            (String Array) Different Versions For Command Name To Find
	 * @return returns true If Command Exists
	 */
	public Cmd findCommand(String... names) {
		return findCommand(false, names);
	}

	/**
	 * @param name
	 *            (String Array) Different Versions For Command Name To Find
	 * @param matchcase
	 *            (boolean) Match Case Sensitive
	 * @return returns true If Command Exists
	 */
	public Cmd findCommand(boolean matchcase, String... names) {
		for(int X = 0; X < _Commands.length(); X++) {
			final String T_CMD = (matchcase ? _Commands.getAt(X).getName() : _Commands.getAt(X).getName().toLowerCase());

			for(int Y = 0; Y < names.length; Y++) {
				final String COMMAND = (matchcase ? names[Y] : names[Y].toLowerCase());

				if(T_CMD.equals(COMMAND)) {
					return _Commands.getAt(X);
				}
			}
		}

		return null;
	}

	/**
	 * @param name
	 *            (String Array) Command Name To Find
	 * @return returns All Commands Of The Given Name
	 */
	public Cmd[] findCommands(String name) {
		return findCommands(false, name);
	}

	/**
	 * @param name
	 *            (String Array) Command Name To Find
	 * @return returns All Commands Of The Given Name
	 */
	public Cmd[] findCommands(String... names) {
		return findCommands(false, names);
	}

	/**
	 * @param matchcase
	 *            (boolean) Match Case Sensitive
	 * @param name
	 *            (String Array) Command Name To Find
	 * @return returns All Commands Of The Given Name
	 */
	public Cmd[] findCommands(boolean matchcase, String... names) {
		ResizingArray<Cmd> Result = new ResizingArray<Cmd>(2, 2);

		for(int X = 0; X < names.length; X++) {
			final String COMMAND = (matchcase ? names[X] : names[X].toLowerCase());

			for(int Y = 0; Y < _Commands.length(); Y++) {
				final String T_CMD = (matchcase ? _Commands.getAt(Y).getName() : _Commands.getAt(Y).getName().toLowerCase());

				if(COMMAND.equals(T_CMD)) {
					Result.put(_Commands.getAt(Y));
				}
			}
		}

		return Result.toArray(new Cmd[Result.length()]);
	}

	/**
	 * @param matchcase
	 *            (boolean) Match Case Sensitive
	 * @return returns true If Command Exists
	 */
	public boolean commandExists(String name) {
		return commandExists(false, name);
	}

	/**
	 * @param name
	 *            (String) Name of Command To Check If It Exists
	 * @return returns true If Command Exists
	 */
	public boolean commandExists(boolean matchcase, String name) {
		final String COMMAND = (matchcase ? name : name.toLowerCase());

		for(int X = 0; X < _Commands.length(); X++) {
			final String T_CMD = (matchcase ? _Commands.getAt(X).getName() : _Commands.getAt(X).getName().toLowerCase());

			if(COMMAND.equals(T_CMD)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @param name
	 *            (String) Name of Command To Check If It Exists
	 * @return returns true If Command Exists
	 */
	public boolean commandsExists(String... names) {
		return commandsExists(false, names);
	}

	/**
	 * @param name
	 *            (String) Name of Command To Check If It Exists
	 * @param matchcase
	 *            (boolean) Match Case Sensitive
	 * @return returns true If Command Exists
	 */
	public boolean commandsExists(boolean matchcase, String... names) {
		for(int X = 0; X < _Commands.length(); X++) {
			final String T_CMD = (matchcase ? _Commands.getAt(X).getName() : _Commands.getAt(X).getName().toLowerCase());

			for(int Y = 0; Y < names.length; Y++) {
				final String COMMAND = (matchcase ? names[Y] : names[Y].toLowerCase());

				if(T_CMD.equals(COMMAND)) {
					return true;
				}
			}
		}

		return false;
	}

	public abstract boolean parse(String args);

	public abstract boolean parse(String[] args);

	//STATIC	

	//CLASSES
	public class Cmd {
		private final String _COMMAND_NAME;
		private final ResizingArray<String> _VARIABLES = new ResizingArray<String>(2, 1);

		public Cmd(String command, String... variable) {
			_COMMAND_NAME = command;

			for(int X = 0; X < variable.length; X++) {
				_VARIABLES.puts(variable);
			}
		}

		public String getName() {
			return _COMMAND_NAME;
		}

		/**
		 * 
		 * @return returns First Item
		 */
		public String getVariable() {
			return (variableCount() > 0 ? getVariableAt(0) : null);
		}

		public String getVariableAt(int index) {
			if(_VARIABLES.validIndex(index)) {
				return _VARIABLES.getAt(index);
			}

			return null;
		}

		public int variableCount() {
			return _VARIABLES.length();
		}

		@Override
		public String toString() {
			return _COMMAND_NAME + ' ' + getVariable() + (variableCount() > 0 ? "...." : "");
		}

		public void addVariable(String variable) {
			_VARIABLES.put(variable);
		}
	}
}
