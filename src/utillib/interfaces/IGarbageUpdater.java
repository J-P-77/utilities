package utillib.interfaces;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public interface IGarbageUpdater {
	public void updateMaxMemory(long value);

	public void updateTotalMemory(long value);

	public void updateFreeMemory(long value);

	public void updateAll(String value);
}
