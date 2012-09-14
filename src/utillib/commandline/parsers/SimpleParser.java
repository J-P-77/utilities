package utillib.commandline.parsers;

import utillib.strings.MyStringBuffer;
import utillib.strings.StringUtil;

import utillib.arrays.ResizingArray;

import utillib.commandline.ACmdLineHandler;

import utillib.lang.byref.IntByRef;

/**
 * Examples. -openserver server1 local:1234
 * 
 * -file "text 1.txt" /include
 * 
 * @author Dalton Dell
 * 
 */
public class SimpleParser extends ACmdLineHandler {
	public static final String _DEFAULT_ARGUMENT_START_ = "-";
	public static final String _DEFAULT_SWITCH_START_ = "/";

	private final String _ARGUMENT_START;
	private final String _SWITCH_START;

	public SimpleParser() {
		this(_DEFAULT_ARGUMENT_START_, _DEFAULT_SWITCH_START_);
	}

	public SimpleParser(String argstart, String swistart) {
		if(argstart == null) {
			throw new NullPointerException("Variable[argstart] - Is Null");
		}

		if(swistart == null) {
			throw new NullPointerException("Variable[swistart] - Is Null");
		}

		_ARGUMENT_START = argstart;
		_SWITCH_START = swistart;
	}

	public boolean parse(String args) {
		if(args == null) {
			throw new NullPointerException("Variable[args] - Is Null");
		}

		return parse(split(args));
	}

	public boolean parse(String[] args) {
		if(args == null) {
			throw new NullPointerException("Variable[args] - Is Null");
		}

		final ACmdLineHandler.Cmd BLANK_CMD = super.addCommand("");
		final boolean ONLY_ONE = false;

		for(int X = 0; X < args.length; X++) {
			if(StringUtil.isString(args[X], _ARGUMENT_START, 0)) {
				final String T_STR = args[X].substring(_ARGUMENT_START.length());

				if(T_STR.length() > 0) {
					final ACmdLineHandler.Cmd CMD = super.addCommand(T_STR);

					if(ONLY_ONE) {
						if((++X) < args.length) {
							if(!StringUtil.isString(args[X], _ARGUMENT_START, 0) && !StringUtil.isString(args[X], _SWITCH_START, 0)) {
								CMD.addVariable(args[X]);
							}
						}

//	                for(X++; X < args.length; X++) {
//	        			if(!StringUtil.isString(args[X], _ARGUMENT_START, 0) && !StringUtil.isString(args[X], _SWITCH_START, 0)) {
//	        				CMD.addVariable(args[X]);
//	        			} else {
//	        				X--;
//	        				break;
//	        			}
//	                }
					} else {
						int Y = 1;
						for(; (X + Y) < args.length; Y++) {
							if(!StringUtil.isString(args[X + Y], _ARGUMENT_START, 0) && !StringUtil.isString(args[X + Y], _SWITCH_START, 0)) {
								CMD.addVariable(args[X + Y]);
							} else {
								Y--;
								break;
							}
						}

						X += Y;
					}
				}
			} else if(StringUtil.isString(args[X], _SWITCH_START, 0)) {
				final String T_STR = args[X].substring(_SWITCH_START.length());

				if(T_STR.length() > 0) {
					super.addCommand(T_STR);
				}
			} else {
				BLANK_CMD.addVariable(args[X]);
			}
		}

		return true;
	}

	/**
	 * 
	 * @return String CommandLine Arguments Start Line
	 */
	public String getArgumentStart() {
		return _ARGUMENT_START;
	}

	/**
	 * 
	 * @return swistart String Start Of SwitchArgument (ex. /)
	 */
	public String getSwitchStart() {
		return _SWITCH_START;
	}

	public String[] split(String str) {
		final ResizingArray<String> RETURN = new ResizingArray<String>();
		final MyStringBuffer BUFFER = new MyStringBuffer(str.length());

		boolean InQuotes = false;
		for(IntByRef X = new IntByRef(0); X.value < str.length(); X.value++) {
			if(str.charAt(X.value) == '"') {
				InQuotes = !InQuotes;
				continue;
			}

			if(!InQuotes) {
				if(str.charAt(X.value) == ' ') {
					if(BUFFER.length() > 0) {
						RETURN.put(BUFFER.toString(true));
					}
					continue;
				}

			}

			BUFFER.append(str.charAt(X.value));
		}

		if(BUFFER.length() > 0) {
			RETURN.put(BUFFER.toString(true));
		}

		for(int X = 0; X < RETURN.length(); X++) {
			System.out.println(RETURN.getAt(X));
		}

		return RETURN.toArray(new String[RETURN.length()]);
	}

	public static void main(String[] args) {
		final String[][] TESTS = {
				//Arg Start, Swt Start, Args
		new String[] {_DEFAULT_ARGUMENT_START_, _DEFAULT_SWITCH_START_, "-open-server server1 localhost 1234"}, new String[] {_DEFAULT_ARGUMENT_START_, _DEFAULT_SWITCH_START_, "-open-server server1 localhost:1234"}, new String[] {_DEFAULT_ARGUMENT_START_, _DEFAULT_SWITCH_START_, "-file \"C:\\text 1.txt\""}, new String[] {_DEFAULT_ARGUMENT_START_, _DEFAULT_SWITCH_START_, "/include"}, new String[] {_DEFAULT_ARGUMENT_START_, _DEFAULT_SWITCH_START_, "   /cmd   -file \"C:\\text 1.txt\"   /include   -openserver server1 local 1234    /gui"}, new String[] {_DEFAULT_ARGUMENT_START_, _DEFAULT_SWITCH_START_, "-file \"C:\\text 1.txt\" /include"},

		new String[] {_DEFAULT_ARGUMENT_START_, _DEFAULT_SWITCH_START_, "/cmd -in C:\\text 1.txt /gui -out \"C:\\text 2.txt\""},};

		for(int X = 0; X < TESTS.length; X++) {
			System.out.println("TEST " + X + " CommandLine[" + TESTS[X][2] + "]");
			final ACmdLineHandler CMDLINE = new SimpleParser(TESTS[X][0], TESTS[X][1]);

			CMDLINE.parse(TESTS[X][2]);

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