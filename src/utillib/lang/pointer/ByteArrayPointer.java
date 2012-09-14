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
public class ByteArrayPointer extends AArrayPointer {
	private byte[] _Array = null;

	public ByteArrayPointer() {
		this(5);
	}

	public ByteArrayPointer(int length) {
		if(length < 0) {
			throw new RuntimeException("Variable[length] - Negative Number Not Allowed");
		}

		_Array = new byte[length];
	}

	public void set(byte value) {
		_Array[_Pointer] = value;
	}

	public void set(int index, byte value) {
		_Array[index] = value;
	}

	public byte get() {
		return _Array[_Pointer];
	}

	public byte get(int index) {
		return _Array[index];
	}

	public byte[] toArray() {
		return _Array;
	}

	public byte[] copy() {
		final byte[] ARRAY = new byte[length()];

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
			byte[] Temp = _Array;
			_Array = null;
			_Array = new byte[length];

			System.arraycopy(Temp, 0, _Array, 0, (length > Temp.length ? Temp.length : length));
			Temp = null;
		} else {
			_Array = new byte[length];
		}
	}

//    public static void main(String[] args) {
//        final ByteArrayPointer P_BYTES = new ByteArrayPointer(10);
//
//        byte X = 0;
//        while(P_BYTES.hasNext()) {
//            P_BYTES.next(X++);
//        }
//
//        P_BYTES.reset();
//        while(P_BYTES.hasNext()) {
//            System.out.println(P_BYTES.get());
//        }
//
//        while(P_BYTES.hasPrevious()) {
//            System.out.println(P_BYTES.get());
//        }
//
///*
//        System.out.println("--------------------------------");
//
//        P_BYTES.newLength(15, true);
//        while(P_BYTES.hasNext()) {
//            P_BYTES.next(X++);
//        }
//
//        P_BYTES.reset();
//
//        while(P_BYTES.hasNext()) {
//            System.out.println(P_BYTES.next());
//        }*/
//    }
}
