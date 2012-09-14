package utillib.arrays.buffer;

import utillib.interfaces.IOutputBuffer;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 22, 2011 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class FixedOutputByteBuffer extends AFixedOutputByteBuffer {
	private final IOutputBuffer _ILISTENER;

	public FixedOutputByteBuffer(int size, IOutputBuffer listener) {
		super(size);

		if(listener == null) {
			throw new RuntimeException("Variable[listener] - Is Null");
		}

		_ILISTENER = listener;
	}

	@Override
	public void empty(byte[] buffer, int offset, int length) {
		_ILISTENER.empty(buffer, offset, length);
	}

/*
    public static void main(String[] args) {
        ResizingIntArray Bytes = new ResizingIntArray(1);
        
        int[] Arr = {0,1,2,3,4,5,6,7,8,9};

        Bytes.put(Arr, 0, Arr.length);
        Bytes.put(Arr, 0, Arr.length);
        Bytes.put(Arr, 0, Arr.length);
        Bytes.put(Arr, 0, Arr.length);
        Bytes.put(Arr, 0, Arr.length);
        Bytes.put(Arr, 0, Arr.length);
        Bytes.put(Arr, 0, Arr.length);
        Bytes.put(Arr, 0, Arr.length);
        Bytes.put(Arr, 0, Arr.length);
    }
*/
}
