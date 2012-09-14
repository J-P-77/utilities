package beta.utillib.classloader.v2.wrappers;

/**
 * 
 * @author Dalton Dell
 */
public class MethodCallError {
	private Throwable _THROWABLE;

	public MethodCallError() {
		this(null);
	}

	public MethodCallError(Throwable exception) {
		_THROWABLE = exception;
	}

	public Throwable getThrowable() {
		return _THROWABLE;
	}
}
