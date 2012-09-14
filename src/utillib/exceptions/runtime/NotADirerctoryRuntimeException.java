package utillib.exceptions.runtime;

import java.io.File;

/**
 * 
 * @author Justin Palinkas
 */
public class NotADirerctoryRuntimeException extends FileRuntimeException {

	public NotADirerctoryRuntimeException(File file) {
		this("", file);
	}

	public NotADirerctoryRuntimeException(String message, File file) {
		super(message, file);
	}
}
