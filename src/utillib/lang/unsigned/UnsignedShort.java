package utillib.lang.unsigned;

/**<pre>
 * <b>Current Version 1.0.0</b>
 *
 * April 08, 2010 (Version 1.0.0)
 *     -First Released
 *
 * @author Justin Palinkas
 *
 * </pre>
 */
public class UnsignedShort {
    public static final int _MIN_VALUE_ = 0;
    public static final int _MAX_VALUE_ = 65535;

    private int _Value = 0;

    public UnsignedShort(short value) {
    	_Value = (int)((value >> 16) & 0xff);
    }
    
    public UnsignedShort(int value) {
        setValue(value);
    }

    public void setValue(int value) {
    	checkRange(_Value);

        _Value = value;
    }

    public int getValue() {
        return _Value;
    }
    
    public void increment() {
        _Value++;
        
        checkRange(_Value);
    }
    
    public void increment(int value) {
    	checkRange(value);

        _Value += value;
    }
    
    
    public void decrement() {
        _Value--;
        
        checkRange(_Value);
    }
    
    public void decrement(int value) {
    	checkRange(value);

        _Value -= value;
    }
    
    public static void checkRange(int value) {
        if(value < _MIN_VALUE_ || value > _MAX_VALUE_) {
            throw new RuntimeException("Variable[value] - Must Be >= 0 and <= " + _MAX_VALUE_);
        }
    }
}
