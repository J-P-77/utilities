package utillib.commandline.parsers;

import utillib.strings.MyStringBuffer;
import utillib.strings.StringUtil;

import utillib.commandline.ACmdLineHandler;

import utillib.lang.byref.IntByRef;

/**
 * Examples.
 * -openserver server1:local:1234
 * -openserver:server1:local:1234
 * -file "text 1.txt" /include
 * 
 * @author Dalton Dell
 *
 */
public class DefaultParser extends ACmdLineHandler {
    public static final String _DEFAULT_ARGUMENT_START_ = "-";
    public static final String _DEFAULT_SWITCH_START_ = "/";
    
    private final String _ARGUMENT_START;
    private final String _SWITCH_START;
    private final char _VARIABLE_SEP;
    
    public DefaultParser() {
    	this(_DEFAULT_ARGUMENT_START_, _DEFAULT_SWITCH_START_, ':');
    }
    
    public DefaultParser(String argstart, String swistart) {
    	this(argstart, swistart, ':');
    }
    
    public DefaultParser(String argstart, String swistart, char variablesep) {
        if(argstart == null) {
            throw new NullPointerException("Variable[argstart] - Is Null");
        }

        if(swistart == null) {
            throw new NullPointerException("Variable[swistart] - Is Null");
        }

        _ARGUMENT_START = argstart;
		_SWITCH_START = swistart;
		_VARIABLE_SEP = variablesep;
	}
    
    public boolean parse(String args) {
        if(args == null) {
            throw new NullPointerException("Variable[args] - Is Null");
        }
        
        final MyStringBuffer BUFFER = new MyStringBuffer(args.length());

        boolean InQuotes = false;
        for(IntByRef X = new IntByRef(0); X.value < args.length();) {
            StringUtil.skipsWhiteSpaces(args, X);

            if(StringUtil.isString(args, _ARGUMENT_START, X)) {
                readCmdName(BUFFER, args, X);
                
                ACmdLineHandler.Cmd Cmd = super.addCommand(BUFFER.toString(true));

                for(;X.value < args.length(); X.value++) {
                    if(args.charAt(X.value) == '"') {
                        InQuotes = !InQuotes;
                        continue;
                    }

                    if(!InQuotes) {
                        if(args.charAt(X.value) == ' ') {
//                            Command.addVariable(CmdInfo.toString(true));
                            break;
                        } else if(args.charAt(X.value) == _VARIABLE_SEP) {
                            Cmd.addVariable(BUFFER.toString(true));
                            continue;
                        }
                    }

                    BUFFER.append(args.charAt(X.value));
                }

                if(BUFFER.length() > 0) {
                    Cmd.addVariable(BUFFER.toString(true));
                }
            } else {
                if(StringUtil.isString(args, _SWITCH_START, X)) {
                    readCmdName(BUFFER, args, X);
                    super.addCommand(BUFFER.toString(true));
                } else {
                	X.value++;//All Else Fails Move Pointer Ahead One Position
                }
            }
        }
        
        return true;
	}
	
	public boolean parse(String[] args) {
		return parse(combine(args));
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
	
    private void readCmdName(MyStringBuffer cmdinfo, String args, IntByRef offset) {
        for(; offset.value < args.length(); offset.value++) {                        
            if(args.charAt(offset.value) == ' ' || args.charAt(offset.value) == _VARIABLE_SEP) {
            	offset.value++;
                break;
            } else {
                cmdinfo.append(args.charAt(offset.value));
            }
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
            new String[] {_DEFAULT_ARGUMENT_START_, _DEFAULT_SWITCH_START_, "-open-server server1:localhost:1234"},
            new String[] {_DEFAULT_ARGUMENT_START_, _DEFAULT_SWITCH_START_, "-open-server:server2:localhost:7777"},
            new String[] {_DEFAULT_ARGUMENT_START_, _DEFAULT_SWITCH_START_, "-openserver server1:localhost:1234"},
            new String[] {_DEFAULT_ARGUMENT_START_, _DEFAULT_SWITCH_START_, "-openserver:server2:localhost:7777"},
            new String[] {_DEFAULT_ARGUMENT_START_, _DEFAULT_SWITCH_START_, "-file \"C:\\text 1.txt\""},
            new String[] {_DEFAULT_ARGUMENT_START_, _DEFAULT_SWITCH_START_, "-file:\"C:\\text 1.txt\""},
            new String[] {_DEFAULT_ARGUMENT_START_, _DEFAULT_SWITCH_START_, "/include"},
            new String[] {_DEFAULT_ARGUMENT_START_, _DEFAULT_SWITCH_START_, "   /cmd   -file:\"C:\\text 1.txt\"   /include   -openserver server1:local:1234    /gui"},
            new String[] {_DEFAULT_ARGUMENT_START_, _DEFAULT_SWITCH_START_, "-file:\"C:\\text 1.txt\" /include"},
            new String[] {_DEFAULT_ARGUMENT_START_, _DEFAULT_SWITCH_START_, "-file:\"C:\\text 1.txt\":\"C:\\text 2.txt\""},
            new String[] {_DEFAULT_ARGUMENT_START_, _DEFAULT_SWITCH_START_, "/cmd -file C:\\text 1.txt /gui -file \"C:\\Justin\\This\\Test\\text 2.txt\""},
            new String[] {_DEFAULT_ARGUMENT_START_, _DEFAULT_SWITCH_START_, "-file \"C:\\Justin\\This\\Test\\text 2.txt\""},
        };

        for(int X = 0; X < TESTS.length; X++) {
            System.out.println("TEST " + X + " CommandLine[" + TESTS[X][2] + "]");
            final ACmdLineHandler CMDLINE = new DefaultParser(TESTS[X][0], TESTS[X][1]);
            
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