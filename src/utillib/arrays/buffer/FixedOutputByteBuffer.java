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
