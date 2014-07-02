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

package utillib.lang.unsigned;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * April 08, 2010 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class UnsignedByte {
	public static final short _MIN_VALUE_ = 0;
	public static final short _MAX_VALUE_ = 255;

	private short _Value = 0;

	public UnsignedByte(byte value) {
		_Value = (short)((value >> 8) & 0xff);
	}

	public UnsignedByte(short value) {
		setValue(value);
	}

	public void setValue(short value) {
		if(value < 0 || value > _MAX_VALUE_) {
			throw new RuntimeException("Variable[value] - Must Be >= 0 and <= 255");
		}

		_Value = value;
	}

	public short getValue() {
		return _Value;
	}

	public void increment() {
		_Value++;

		checkRange(_Value);
	}

	public void increment(short value) {
		checkRange(value);

		_Value += value;
	}

	public void decrement() {
		_Value--;

		checkRange(_Value);
	}

	public void decrement(short value) {
		checkRange(value);

		_Value -= value;
	}

	public static void checkRange(short value) {
		if(value < _MIN_VALUE_ || value > _MAX_VALUE_) {
			throw new RuntimeException("Variable[value] - Must Be >= 0 and <= " + _MAX_VALUE_);
		}
	}
}
