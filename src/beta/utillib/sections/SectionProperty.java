package beta.utillib.sections;

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
public class SectionProperty implements SectionConstants {
    private final String _COMMENT_START;

    private String _Name  = "";
    private String _Variable = "";

    public SectionProperty() {
        this("", "", _DEFAULT_COMMENT_START_);
    }

    public SectionProperty(String name, String variable) {
        this(name, variable, _DEFAULT_COMMENT_START_);
    }

    public SectionProperty(String name, String variable, String commentstart) {
        if(name == null) {
            throw new RuntimeException("Variable[name] - Is Null");
        }

        if(variable == null) {
            throw new RuntimeException("Variable[variable] - Is Null");
        }

        if(commentstart == null) {
            throw new RuntimeException("Variable[commentstart] - Is Null");
        }

        _Name =  name;
        _Variable = variable;

        _COMMENT_START = commentstart;
    }
/*
    public SectionProperty(String argument, String commentstart) {
        if(argument == null) {
            throw new RuntimeException("Class[" + getClass().getName() + "] - Method[Constructor] - Variable[argument] - Is Null");
        }

        final int INDEX = argument.indexOf('=');

        if(INDEX == -1) {
            _Name = argument;
        } else {
            _Name =  argument.substring(0, INDEX);
            _Variable =  argument.substring(INDEX + 1, argument.length());
        }
        _COMMENT_START = commentstart;
    }
*/
    public void setName(String name) {
        _Name = (name == null ? "" : name);
    }
    
    public String getName() {
        return _Name;
    }
    
    public void setVariable(String variable) {
        _Variable = (variable == null ? "" : variable);
    }
    
    public String getVariable() {
        return _Variable;
    }
    
    public boolean isComment() {
        return _Name.startsWith(_COMMENT_START);
    }
    
    @Override
    public String toString() {
        if(isComment()) {//Comment
            return _Name + _Variable;
        } else {//Non Comment
            return _Name + '=' + _Variable;
        }
    }
}
