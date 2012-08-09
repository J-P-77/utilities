package utillib.exceptions.runtime;

import java.io.File;

/**
 *
 * @author Dalton Dell
 */
public class FileRuntimeException extends RuntimeException {
    private final File _FILE;

    public FileRuntimeException(File file) {
        this("", file);
    }

    public FileRuntimeException(String message, File file) {
        super(message);

        _FILE = file;
    }

    public File getFile() {return _FILE;}
}
