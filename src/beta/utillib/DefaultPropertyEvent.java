package beta.utillib;

/**
 *
 * @author Dalton Dell
 */
public class DefaultPropertyEvent implements IPropertyEvent {	
    private final int _EVENT_ID;

    private final int _PROPERTY_ID;

    private final Object _SOURCE;
//    private final Object _OLD_VALUE;
    private final Object _NEW_VALUE;

    private boolean _Consume = false;

    public DefaultPropertyEvent(int eventid, int propertyid, Object source, /*Object oldvalue,*/ Object newvalue) {
        _EVENT_ID = eventid;

        _PROPERTY_ID = propertyid;

        _SOURCE = source;

//        _OLD_VALUE = oldvalue;
        _NEW_VALUE = newvalue;
    }

    public int getEventId() {
        return _EVENT_ID;
    }

    @Override
    public int getPropertyId() {
        return _PROPERTY_ID;
    }

    public Object getSource() {
        return _SOURCE;
    }

    public Object getNewValue() {
        return _NEW_VALUE;
    }

//    public Object getOldValue() {
//        return _OLD_VALUE;
//    }

    public void consume() {
        _Consume = true;
    }

    public boolean hasBeenConsumed() {
        return _Consume;
    }

//    /**
//     *
//     * @return If Old Value and New Value are null returns true<br />
//     *         If Old Value is null returns false
//     */
//    public boolean isSameAsOld() {
//        if(_OLD_VALUE == null && _NEW_VALUE == null) {
//            return true;
//        }
//
//        if(_OLD_VALUE == null) {
//            return false;
//        } else {
//            return _OLD_VALUE.equals(_NEW_VALUE);
//        }
//    }

    public static DefaultPropertyEvent eventAdded(Object source, int propertyid, /*Object oldvalue, */Object newvalue) {
        return new DefaultPropertyEvent(_EVENT_ADDED_, propertyid, source, /*oldvalue,*/ newvalue);
    }

    public static DefaultPropertyEvent eventRemoved(Object source, int propertyid, /*Object oldvalue, */Object newvalue) {
        return new DefaultPropertyEvent(_EVENT_REMOVED_, propertyid, source, /*oldvalue,*/ newvalue);
    }

    public static DefaultPropertyEvent eventChanging(Object source, int propertyid, /*Object oldvalue, */Object newvalue) {
        return new DefaultPropertyEvent(_EVENT_CHANGING_, propertyid, source, /*oldvalue,*/ newvalue);
    }

    public static DefaultPropertyEvent eventChanged(Object source, int propertyid, /*Object oldvalue, */Object newvalue) {
        return new DefaultPropertyEvent(_EVENT_CHANGED_, propertyid, source, /*oldvalue,*/ newvalue);
    }
}
