package utillib.lang.pointer;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * April 25, 2010 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
@SuppressWarnings("unchecked")
public class TArrayPointer<T> extends AArrayPointer {
	private Object[] _Array = null;

	public TArrayPointer() {
		this(5);
	}

	public TArrayPointer(int length) {
		if(length < 0) {
			throw new RuntimeException("Variable[length] - Negative Number Not Allowed");
		}

		_Array = new Object[length];
	}

	public void set(T value) {
		_Array[_Pointer] = value;
	}

	public void set(int index, T value) {
		_Array[index] = value;
	}

	public T get() {
		return (T)_Array[_Pointer];
	}

	public T get(int index) {
		return (T)_Array[index];
	}

	public Object[] toArray() {
		return _Array;
	}

	public Object[] copy() {
		final Object[] ARRAY = new Object[length()];

		System.arraycopy(_Array, 0, ARRAY, 0, length());

		return ARRAY;
	}

	@Override
	public int length() {
		return _Array.length;
	}

	@Override
	public void newLength(int length, boolean preserve) {
		if(length < 0) {
			throw new RuntimeException("Variable[length] - Negative Number Not Allowed");
		}

		if(preserve) {
			Object[] Temp = _Array;
			_Array = null;
			_Array = new Object[length];

			System.arraycopy(Temp, 0, _Array, 0, (length > Temp.length ? Temp.length : length));
			Temp = null;
		} else {
			_Array = new Object[length];
		}
	}
}
