package utillib.arguments;

import utillib.strings.StringUtil;

/**<pre>
 * <b>Current Version 1.0.0</b>
 * 
 * April 01, 2009 (version 1.0.0)
 *     -Updated
 *         -EveryThing
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class ArgumentsUtil {
	@Deprecated
    public static final String[] _TRUE_ = {"true", "t", "yes","1"};
	@Deprecated
    public static final String[] _FALSE_ = {"false", "f", "no", "0"};

    /**
     *
     * @param value
     * @return
     * @deprecated Use utillib.strings.StringUtil Class
     */
    @Deprecated
    public static boolean isNumber(String value) {
        return StringUtil.isNumber(value);
    }

    /**
     *
     * @param value
     * @return
     * @deprecated Use StringUtil Class
     */
    @Deprecated
    public static boolean isWholeNumber(String value) {
        return StringUtil.isWholeNumber(value);
    }

    public static boolean isBoolean(String value) {
        if(value == null || value.length() == 0) {
            return false;
        } else {
            final String VALUE = value.toLowerCase();
            for(int X = 0; X < _TRUE_.length; X++) {
                if(VALUE.equals(_TRUE_[X]) || VALUE.equals(_FALSE_[X])) {
                    return true;
                }
            }

            return false;
        }
    }

    @Deprecated
    public static boolean getBooleanValue(String value) {
    	return StringUtil.getBooleanValue(value, false);
    }

    @Deprecated
    public static boolean getBooleanValue(String value, boolean defaultvalue) {
       return StringUtil.getBooleanValue(value, defaultvalue);
    }
}
