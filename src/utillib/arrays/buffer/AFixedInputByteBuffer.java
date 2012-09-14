package utillib.arrays.buffer;

import utillib.arrays.ArraysUtil;

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
public abstract class AFixedInputByteBuffer {
	private final byte[] _BUFFER;
	private int _Pos = 0;
	private int _Top = 0;

	public AFixedInputByteBuffer(int size) {
		_BUFFER = new byte[size];
	}

	public void reset() {
		_Pos = 0;
	}

	public void reset(int value) {
		if(value >= 0 && value < _Top) {
			_Pos = value;
		}
	}

	public int fill() {
		final int LEN = _Top - _Pos;
		int X = 0;
		for(; X < LEN && _Pos < _Top; X++, _Pos++) {
			_BUFFER[X] = _BUFFER[_Pos];
		}
		_Top = X + fill(_BUFFER, _Pos, _BUFFER.length - _Pos);
		_Pos = 0;

		return _Top;
	}

	public synchronized int get() {
		if(_Top == -1) {
			return -1;
		}

		if(_Pos >= _Top) {
			_Top = fill(_BUFFER, 0, _BUFFER.length);
			_Pos = 0;

			if(_Top <= 0) {
				return (_Top = -1);
			}
		}

		return _BUFFER[_Pos++];
	}

	public synchronized int get(byte[] buffer) {
		return get(buffer, 0, buffer.length);
	}

	public synchronized int get(byte[] buffer, int offset, int length) {
		ArraysUtil.checkBufferBounds(buffer.length, offset, length);

		if(_Top == -1) {
			return -1;
		}

		int Read = 0;
		while(true) {
			if(_Pos >= _Top) {
				_Top = fill(_BUFFER, 0, _BUFFER.length);
				_Pos = 0;

				if(_Top <= 0) {
					_Top = -1;
					return Read;
				}
			}

			buffer[Read++ + offset] = _BUFFER[_Pos++];

			if(Read == length) {
				return Read;
			}
		}
	}

	public synchronized int get(int index) {
		if(validIndex(index)) {
			return _BUFFER[index];
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index);
		}
	}

	public synchronized void set(int index, byte object) {
		if(validIndex(index)) {
			_BUFFER[index] = object;
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index);
		}
	}

//    public synchronized void remove(int index) {
//        if(validIndex(index)) {
//            for(int X = (index + 1); X < (_Top); X++) {
//            	_BUFFER[index++] = _BUFFER[X];
//            }
//
//            _BUFFER[_Top - 1] = -1;
//
//            _Top = index;
//        } else {
//            throw new IndexOutOfBoundsException("Index Number: " + index + " Out Of Bounds");
//        }
//    }

	public synchronized boolean validIndex(int index) {
		return (index >= 0 && index < _Top);
	}

	public int length() {
		return _Top;
	}

	public int getCapacity() {
		return _BUFFER.length;
	}

	public boolean isEmpty() {
		return _Top == 0;
	}

	public abstract int fill(byte[] buffer, int offset, int length);

	//Just A Test Class
	private static class Final implements utillib.interfaces.IInputBuffer {
		private java.io.FileInputStream _IStream = null;

		public Final(java.io.FileInputStream istream) {
			_IStream = istream;
		}

		@Override
		public int fill(byte[] buffer, int offset, int length) {
			try {
				return _IStream.read(buffer, offset, length);
			} catch(Exception e) {
				e.printStackTrace();
			}

			return -1;
		}
	}

	public static void main(String[] args) {
		java.io.FileInputStream IStream = null;
		try {
			IStream = new java.io.FileInputStream("C:\\Documents and Settings\\Dalton Dell\\Desktop\\New Notepad++ Document.txt");

			final AFixedInputByteBuffer BUFFER = new FixedInputByteBuffer(6, new Final(IStream));
			final byte[] HEADER = new byte[2];
			int Length = 0;

			System.out.println("[HEADER]");
			Length = BUFFER.get(HEADER);
			for(int X = 0; X < Length; X++) {
				System.out.print((char)HEADER[X]);
			}

			BUFFER.fill();

			System.out.println();
			System.out.println("[DATA]");
			int Read = -1;
			while((Read = BUFFER.get()) > 0) {
				System.out.print((char)Read);
			}

//			int Counter = 0;
//			int Length = 0;
//			while((Length = BUFFER.get(BYTE_BUFFER)) > 0) {
//				for(int X = 0; X < Length; X++) {
//		        	System.out.print((char)BYTE_BUFFER[X]);
//		        }
//				
////				if(Counter++ > 10) {
////					break;
////				}
//			}
			System.out.println();
			System.out.println("[END]");
//			System.out.println(Integer.toString(Length));
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(IStream != null) {
				try {
					IStream.close();
				} catch(Exception e2) {}
				IStream = null;
			}
		}
	}
//    public static void main(String[] args) {
//        final byte[] BUFFER = ("               " + "Justin PalinkasA").getBytes();
//        
//        System.arraycopy(BUFFER, "Justin Palinkas".length(), BUFFER, 0, BUFFER.length - "Justin Palinkas".length());
//        
//        for(int X = 0; X < BUFFER.length; X++) {
//        	System.out.print((char)BUFFER[X]);
//        }
//    }
}
