package utillib.lang.byref;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * April 14, 2010 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class TByRef<T> {
	public T value = null;

	public TByRef(T value) {
		this.value = value;
	}
}
