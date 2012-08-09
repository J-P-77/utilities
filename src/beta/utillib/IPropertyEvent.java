package beta.utillib;

/**
 *
 * @author Dalton Dell
 */
public interface IPropertyEvent {
    /**
     * You Can Not Consume This Event Value Change
     */
    public static final int _EVENT_ADDED_ = 0;

    /**
     * You Can Not Consume This Event Value Change
     */
    public static final int _EVENT_REMOVED_= 1;

    /**
     * You Can Not Consume This Event Value Change
     */
    public static final int _EVENT_CHANGED_ = 2;

    /**<pre>
     * You Can Consume This Event Value Change
     *      (Use This Event To Test If New Value Is Valid or Not)
     * </pre>
     */
    public static final int _EVENT_CHANGING_ = 3;
    
    public int getEventId();
    public int getPropertyId();
    public Object getSource();

    public Object getNewValue();
//    public Object getOldValue();

    public void consume();
    public boolean hasBeenConsumed();
}
