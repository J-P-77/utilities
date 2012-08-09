package utillib.exceptions.runtime;

import java.io.File;

/**
 *
 * @author Dalton Dell
 */
public class NotAFileRuntimeException extends FileRuntimeException {

    public NotAFileRuntimeException(File file) {
        this("", file);
    }

    public NotAFileRuntimeException(String message, File file) {
        super(message, file);
    }
}
