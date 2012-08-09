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
public class UnsignedByte {
    public static final short _MIN_VALUE_ = 0;
    public static final short _MAX_VALUE_ = 255;

    private short _Value = 0;

    public UnsignedByte(byte value) {
    	_Value = (short)((value >> 8) & 0xff);
    }
    
    public UnsignedByte(short value) {
        setValue(value);
    }
    
    public void setValue(short value) {
        if(value < 0 || value > _MAX_VALUE_) {
            throw new RuntimeException("Variable[value] - Must Be >= 0 and <= 255");
        }

        _Value = value;
    }
    
    public short getValue() {
        return _Value;
    }
    
    public void increment() {
        _Value++;
        
        checkRange(_Value);
    }
    
    public void increment(short value) {
    	checkRange(value);

        _Value += value;
    }
    
    
    public void decrement() {
        _Value--;
        
        checkRange(_Value);
    }
    
    public void decrement(short value) {
    	checkRange(value);

        _Value -= value;
    }
    
    public static void checkRange(short value) {
        if(value < _MIN_VALUE_ || value > _MAX_VALUE_) {
        	throw new RuntimeException("Variable[value] - Must Be >= 0 and <= " + _MAX_VALUE_);
        }
    }
}
