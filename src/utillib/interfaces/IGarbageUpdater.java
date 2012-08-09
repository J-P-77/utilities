package utillib.interfaces;

/**
 * November 02, 2008 (Version 1.0.0)<br>
 *     -First Released<br>
 * <br>
 * @author Justin Palinkas<br>
 * <br>
 * Current Version 1.0.0
 */
public interface IGarbageUpdater {
    public void updateMaxMemory(long value);
    public void updateTotalMemory(long value);
    public void updateFreeMemory(long value);
    
    public void updateAll(String value);
}
