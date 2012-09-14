package utillib.lang.pointer;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * April 12, 2010 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class BooleanArrayPointer extends AArrayPointer {
	private boolean[] _Array = null;

	public BooleanArrayPointer() {
		this(5);
	}

	public BooleanArrayPointer(int length) {
		if(length < 0) {
			throw new RuntimeException("Variable[length] - Negative Number Not Allowed");
		}

		_Array = new boolean[length];
	}

	public void set(boolean value) {
		_Array[_Pointer] = value;
	}

	public void set(int index, boolean value) {
		_Array[index] = value;
	}

	public boolean get() {
		return _Array[_Pointer];
	}

	public boolean get(int index) {
		return _Array[index];
	}

	public boolean[] toArray() {
		return _Array;
	}

	public boolean[] copy() {
		final boolean[] ARRAY = new boolean[length()];

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
			boolean[] Temp = _Array;
			_Array = null;
			_Array = new boolean[length];

			System.arraycopy(Temp, 0, _Array, 0, (length > Temp.length ? Temp.length : length));
			Temp = null;
		} else {
			_Array = new boolean[length];
		}
	}
}
