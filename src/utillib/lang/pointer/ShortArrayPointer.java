package utillib.lang.pointer;

/**<pre>
 * <b>Current Version 1.0.0</b>
 *
 * April 12, 2010 (Version 1.0.0)
 *     -First Released
 *
 * @author Justin Palinkas
 *
 * </pre>
 */
public class ShortArrayPointer extends AArrayPointer {
    private short[] _Array = null;

    public ShortArrayPointer() {
        this(5);
    }

    public ShortArrayPointer(int length) {
        if(length < 0) {
            throw new RuntimeException("Variable[length] - Negative Number Not Allowed");
        }

        _Array = new short[length];
    }

    public void set(short value) {
        _Array[_Pointer] = value;
    }

    public void set(int index, short value) {
        _Array[index] = value;
    }

    public short get() {
        return _Array[_Pointer];
    }

    public short get(int index) {
        return _Array[index];
    }

    @Override
    public int length() {
        return _Array.length;
    }

    public short[] toArray() {
        return _Array;
    }

    public short[] copy() {
        final short[] ARRAY = new short[length()];

        System.arraycopy(_Array, 0, ARRAY, 0, length());

        return ARRAY;
    }

    public void newLength(int length, boolean preserve) {
        if(length < 0) {
            throw new RuntimeException("Variable[length] - Negative Number Not Allowed");
        }

        if(preserve) {
            short[] Temp = _Array;
            _Array = null;
            _Array = new short[length];

            System.arraycopy(Temp, 0, _Array, 0, (length > Temp.length ? Temp.length : length));
            Temp = null;
        } else {
            _Array = new short[length];
        }
    }
}
