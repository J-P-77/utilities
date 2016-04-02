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

package jp77.utillib.utilities;

import java.math.BigInteger;
import java.text.DecimalFormat;

/**
 * 
 * @author Justin Palinkas
 */
public class BruteForceChar {
	public static final char[] _NUMBER_POOL_ = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

	public static final char[] _LOWER_CASE_POOL_ = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

	public static final char[] _UPPER_CASE_POOL_ = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

	public static final char[] _UPPER_LOWER_CASE_POOL_ = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

	public static final char[] _BACKETS_POOL_ = {'[', ']', '{', '}', '<', '>', '(', ')', '"', '"', '\'', '\''};

	public static final char[] _SPECIAL_POOL_ = {'!', '@', '#', '$', '%', '^', '&', '*', '_', '-', '+', '=', '|', '\\', '/', ',', '.', '?', '`', '~', ',', '.'};

	public static final char[] _BINARY_POOL_ = {'0', '1'};

	private char[] _Pool = null;

	private final int _MIN;
	private final int _MAX;

	private final int[] _TRACKER;
	private int _CurrentSize = 0;

	private boolean _HasMore = true;

	private final boolean _DEBUGGING = false;

	public BruteForceChar(int min, int max) {
		if(min <= 0) {
			throw new RuntimeException("Variable[min] - Must Be Greater Than Zero");
		}

		if(min > max) {
			throw new RuntimeException("Variable[min] - Min Must Be Less Than Max");
		}

		_MIN = min;
		_MAX = max;

		_CurrentSize = _MIN;

		_TRACKER = new int[_MAX];
	}

	public BigInteger getNumberOfPossibilities() {
		final int TIMES = (_MAX == _MIN ? _MAX : _MAX - _MIN);

		final String LEN = Integer.toString(_Pool.length);

		BigInteger BigValue = new BigInteger(LEN);

		for(int X = 0; X < TIMES; X++) {
			BigValue = BigValue.multiply(new BigInteger(LEN));
		}

		return BigValue;
	}

	public void addToPool(char[] chars) {
		addToPool(chars, 0, chars.length);
	}

	public void addToPool(char[] chars, int offset, int length) {
		if(chars == null) {
			throw new RuntimeException("Variable[chars] - Is Null");
		}

		if(_Pool == null) {
			_Pool = new char[length];
			//Copy's [chars] Array Into _Pool Array
			System.arraycopy(chars, offset, _Pool, 0, length);
		} else {
			char[] Temp = _Pool;
			_Pool = null;
			_Pool = new char[Temp.length + length];
			//Copy's [Temp] Array Into [_Pool] Array
			System.arraycopy(Temp, 0, _Pool, 0, Temp.length);
			//Copy's [chars] Array Into [_Pool] Array
			System.arraycopy(chars, offset, _Pool, Temp.length, length);
		}
	}

	public boolean hasMore() {
		return (_HasMore);
	}

	public String next() {
		StringBuffer Buffer = new StringBuffer(_CurrentSize);

		for(int X = 0; X < _CurrentSize; X++) {
			Buffer.append(_Pool[_TRACKER[X]]);
		}

		if(_DEBUGGING) {//Debugging Information
			System.out.print("Tracker: ");
			for(int X = 0; X < _CurrentSize; X++) {
				System.out.print(_TRACKER[X]);

				if(X < (_CurrentSize - 1)) {
					System.out.print(", ");
				}
			}
			System.out.println();
		}

		int Count = 0;
		_TRACKER[_CurrentSize - 1]++;
		for(int Z = 1; Z <= _CurrentSize; Z++) {
			if(_TRACKER[_CurrentSize - Z] == _Pool.length) {
				Count++;
				if(Z < _CurrentSize) {
					_TRACKER[_CurrentSize - Z] = 0;
					_TRACKER[_CurrentSize - Z - 1] += 1;
				}
			}
		}

		if((Count == _CurrentSize)) {
			if(_CurrentSize == _MAX) {
				_HasMore = false;
			} else {
				_CurrentSize++;

				for(int X = 0; X < _TRACKER.length; X++) {
					_TRACKER[X] = 0;
				}
			}
		}

		return Buffer.toString();
	}

//1 = 0 1
//2 = 00 01 11 10

	public static String format(BigInteger value) {
		DecimalFormat D = new DecimalFormat("###,###");

		return D.format(value);
	}

	public static void main(String[] args) {
		BigInteger BigValue = new BigInteger("70");
		for(int X = 0; X < 16; X++) {
			BigValue = BigValue.multiply(new BigInteger("70"));
		}
		System.out.println(format(BigValue));
		System.out.println(BigValue);

		BruteForceChar Force = new BruteForceChar(16, 16);

		//Force.addToPool(_BINARY_POOL_);
		Force.addToPool(_NUMBER_POOL_);
		Force.addToPool(_UPPER_CASE_POOL_);
		Force.addToPool(_LOWER_CASE_POOL_);
		Force.addToPool(_SPECIAL_POOL_);
		Force.addToPool(_BACKETS_POOL_);

		System.out.println(Force._Pool.length);

		System.out.println("Number Of Possibilities: " + format(Force.getNumberOfPossibilities()));

//        while(Force.hasMore()) {
//            System.out.println(Force.next());
//        }

		//System.out.println("Actual Number Of Possibilities: " + Force._NumberOfPossibilities);
	}
}