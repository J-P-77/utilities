package utillib.exceptions.runtime;

import java.io.File;

/**
 *
 * @author Dalton Dell
 */
public class FileNotFoundRuntimeException extends FileRuntimeException {

    public FileNotFoundRuntimeException(File file) {
        this("", file);
    }

    public FileNotFoundRuntimeException(String message, File file) {
        super(message, file);
    }
}
