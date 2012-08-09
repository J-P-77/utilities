package utillib.arguments;

import utillib.collections.MyStackAll;

/**<pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class Title {
    private final String _NAME;
    private final boolean _IS_COMMENT;
    
    private MyStackAll<Argument> _Arguments = new MyStackAll<Argument>();

    public Title(String name) {
        this(name, false);
    }

    public Title(String name, boolean iscommenttitle) {
        if(name == null) {
            throw new RuntimeException("Variable[name] - Is Null");
        }

        _IS_COMMENT = iscommenttitle;
        _NAME = name;
    }
    
    public String getName() {
        return _NAME;
    }

    public boolean isComment() {
        return _IS_COMMENT;
    }

    public void addArgument(String argument) {
        if(argument.contains("=")) {
            String[] StrSplit = argument.split("=", 2);
            addArgument(StrSplit[0], StrSplit[1]);
        } else {
            addArgument(argument, "");
        }
    }

    public void addArgument(String name, boolean variable) {
        addArgument(name, Boolean.toString(variable));
    }

    public void addArgument(String name, byte variable) {
        addArgument(name, Byte.toString(variable));
    }

    public void addArgument(String name, short variable) {
        addArgument(name, Short.toString(variable));
    }

    public void addArgument(String name, int variable) {
        addArgument(name, Integer.toString(variable));
    }

    public void addArgument(String name, long variable) {
        addArgument(name, Long.toString(variable));
    }

    public void addArgument(String name, float variable) {
        addArgument(name, Float.toString(variable));
    }

    public void addArgument(String name, double variable) {
        addArgument(name, Double.toString(variable));
    }

    public void addArgument(String name, String variable) {
        addArgument(new Argument(name, variable));
    }

    public void addArgument(Argument a) {
        if(isComment()) {
            throw new RuntimeException("Variable[a] - Cannot Add Argument To Comment Title");
        }

        _Arguments.push(a);
    }

    public void addComment(String comment) {
        _Arguments.push(new Argument(null, comment));
    }

    public Argument getArgument(int index) {
        if(_Arguments.validIndex(index)) {
            return _Arguments.getItemAt(index);
        } else {
            return null;
        }
    }
    
    public Argument getArgument(String name) {
        for(int X = 0; X < _Arguments.length(); X++) {
            if(name.equals(_Arguments.getItemAt(X).getName())) {
                return _Arguments.getItemAt(X);
            }
        }
        
        return null;
    }
    
    public int length() {
		return _Arguments.length();
    }
    
//    public MyStackAll<Argument> getArguments() {
//        return _Arguments;
//    }

    @Override
    public String toString() {
        return _NAME;
    }
}