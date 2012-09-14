package utillib.exceptions.runtime;

import java.io.File;

/**
 * 
 * @author Justin Palinkas
 */
public class FileNotFoundRuntimeException extends FileRuntimeException {

	public FileNotFoundRuntimeException(File file) {
		this("", file);
	}

	public FileNotFoundRuntimeException(String message, File file) {
		super(message, file);
	}
}
