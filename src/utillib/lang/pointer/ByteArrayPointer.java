/*
The MIT License (MIT)

Copyright (c) 2014 Justin Palinkas

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

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
