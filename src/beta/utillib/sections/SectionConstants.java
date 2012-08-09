package beta.utillib.sections;

import java.util.regex.Pattern;

/**
 *
 * @author Dalton Dell
 */
public interface SectionConstants {
    public static final String _DEFAULT_COMMENT_START_ = "//";
    
    public static final Pattern _SECTION_PATTERN_ = Pattern.compile("^\\[(.+)\\]$");
    public static final Pattern _PROPERTY_PATTERN_ = Pattern.compile("^(.+)=(.*)$");
}
