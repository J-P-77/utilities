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
public abstract class AArrayPointer {
    protected int _Pointer = 0;

    public void increment() {
        _Pointer++;
    }

    public void increment(int value) {
        _Pointer += value;
    }
    
    public void decrement() {
        --_Pointer;
    }
    
    public void decrement(int value) {
        _Pointer -= value;
    }

    public void reset() {
        _Pointer = 0;
    }

    public void reset(int value) {
        if(value < 0 || value >= length()) {
            throw new RuntimeException("Variable[value] - Index Out Of Bounds: " + value);
        }

        _Pointer = value;
    }

    public boolean hasNext() {
        return (_Pointer < length());
    }

    public boolean hasPrevious() {
        return (_Pointer > 0);
    }

    public void newLength(int length) {
        newLength(length, true);
    }

    public int getPointer() {
        return _Pointer;
    }

    public abstract int length();
    public abstract void newLength(int length, boolean preserve);

//    public abstract Object get();
//    public abstract void set(Object value);

//    public abstract Object get(int index);
//    public abstract void set(int index), Object value);
}
