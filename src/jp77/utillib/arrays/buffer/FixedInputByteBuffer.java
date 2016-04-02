/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Justin Palinkas
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package jp77.utillib.arrays.buffer;

import jp77.utillib.interfaces.IInputBuffer;

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
public class FixedInputByteBuffer extends AFixedInputByteBuffer {
	private final IInputBuffer _ILISTENER;

	public FixedInputByteBuffer(int size, IInputBuffer listener) {
		super(size);

		if(listener == null) {
			throw new RuntimeException("Variable[listener] - Is Null");
		}

		_ILISTENER = listener;
	}

	@Override
	public int fill(byte[] buffer, int offset, int length) {
		return _ILISTENER.fill(buffer, offset, length);
	}

	//Just A Test Class
	private static class Final implements IInputBuffer {
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
			IStream = new java.io.FileInputStream("C:\\Documents and Settings\\Justin\\My Documents\\TEST.txt");

			final FixedInputByteBuffer BUFFER = new FixedInputByteBuffer(9, new Final(IStream));
			final byte[] BYTE_BUFFER = new byte[2];
			int Length = 0;

			Length = BUFFER.get(BYTE_BUFFER);
			for(int X = 0; X < Length; X++) {
				System.out.print((char)BYTE_BUFFER[X]);
			}
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
