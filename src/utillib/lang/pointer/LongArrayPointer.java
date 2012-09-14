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
public class LongArrayPointer extends AArrayPointer {
	private long[] _Array = null;

	public LongArrayPointer() {
		this(5);
	}

	public LongArrayPointer(int length) {
		if(length < 0) {
			throw new RuntimeException("Class[" + getClass().getName() + "] - Method[Constructor] - Variable[length] - Negative Number Not Allowed");
		}

		_Array = new long[length];
	}

	public void set(long value) {
		_Array[_Pointer] = value;
	}

	public void set(int index, long value) {
		_Array[index] = value;
	}

	public long get() {
		return _Array[_Pointer];
	}

	public long get(int index) {
		return _Array[index];
	}

	public long[] toArray() {
		return _Array;
	}

	public long[] copy() {
		final long[] ARRAY = new long[length()];

		System.arraycopy(_Array, 0, ARRAY, 0, length());

		return ARRAY;
	}

	@Override
	public int length() {
		return _Array.length;
	}

	public void newLength(int length, boolean preserve) {
		if(length < 0) {
			throw new RuntimeException("Class[" + getClass().getName() + "] - Method[Constructor] - Variable[length] - Negative Number Not Allowed");
		}

		if(preserve) {
			long[] Temp = _Array;
			_Array = null;
			_Array = new long[length];

			System.arraycopy(Temp, 0, _Array, 0, (length > Temp.length ? Temp.length : length));
			Temp = null;
		} else {
			_Array = new long[length];
		}
	}

}
