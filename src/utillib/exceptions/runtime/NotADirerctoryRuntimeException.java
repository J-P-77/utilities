package utillib.exceptions.runtime;

import java.io.File;

/**
 *
 * @author Dalton Dell
 */
public class NotADirerctoryRuntimeException extends FileRuntimeException {

    public NotADirerctoryRuntimeException(File file) {
        this("", file);
    }

    public NotADirerctoryRuntimeException(String message, File file) {
        super(message, file);
    }
}
