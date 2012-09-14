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
public class IntegerArrayPointer extends AArrayPointer {
	private int[] _Array = null;

	public IntegerArrayPointer() {
		this(5);
	}

	public IntegerArrayPointer(int length) {
		if(length < 0) {
			throw new RuntimeException("Variable[length] - Negative Number Not Allowed");
		}

		_Array = new int[length];
	}

	public void set(int value) {
		_Array[_Pointer] = value;
	}

	public void set(int index, int value) {
		_Array[index] = value;
	}

	public int get() {
		return _Array[_Pointer];
	}

	public int get(int index) {
		return _Array[index];
	}

	public int[] toArray() {
		return _Array;
	}

	public int[] copy() {
		final int[] ARRAY = new int[length()];

		System.arraycopy(_Array, 0, ARRAY, 0, length());

		return ARRAY;
	}

	@Override
	public int length() {
		return _Array.length;
	}

	public void newLength(int length, boolean preserve) {
		if(length < 0) {
			throw new RuntimeException("Variable[length] - Negative Number Not Allowed");
		}

		if(preserve) {
			int[] Temp = _Array;
			_Array = null;
			_Array = new int[length];

			System.arraycopy(Temp, 0, _Array, 0, (length > Temp.length ? Temp.length : length));
			Temp = null;
		} else {
			_Array = new int[length];
		}
	}
}
