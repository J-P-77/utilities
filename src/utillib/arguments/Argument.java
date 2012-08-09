package utillib.arguments;

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
public class Argument {
    private String _Name;
    private String _Variable;
    
    public Argument() {
        this("", "");
    }   
    
    public Argument(String name, String variable) {
        if(variable == null) {
            throw new RuntimeException("Variable[variable] - Is Null");
        }

        _Name =  name;
        _Variable = variable;
    }

    public Argument(String argument) {
        if(argument == null) {
            throw new RuntimeException("Variable[argument] - Is Null");
        }

        final int INDEX = argument.indexOf('=');

        if(INDEX == -1) {
            _Name = argument;
        } else {
            _Name =  argument.substring(0, INDEX);
            _Variable =  argument.substring(INDEX + 1, argument.length());
        }
    }

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
        return _Name == null;
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
