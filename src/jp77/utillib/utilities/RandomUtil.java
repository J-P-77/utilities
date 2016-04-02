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

import java.security.SecureRandom;

/**
 * <pre>
 * <b>Current Version 1.0.1</b>
 * 
 * November 28, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * November 28, 2008 (Version 1.0.1)
 *     -Updated
 *         -Now Secure Random Is Now Seeded
 *     
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class RandomUtil {
	private static SecureRandom _Random_ = null;

	public static boolean random_50_50() {
		return (randomInt(0, 1) == 1);
	}

	public static int randomInt(int min, int max) {
		if(min < max) {
			final int RESULT = (int)((max - min + 1) * Math.random() + min);

			if(RESULT > max) {
				throw new RuntimeException("Random Number Error: Result Is Greater Than Max Number");
			} else if(RESULT < min) {
				throw new RuntimeException("Random Number Error: Result Is Less Than Min Number");
			} else {
				return RESULT;
			}
		} else if(min == max) {
			return min;
		} else {
			throw new RuntimeException();
		}
	}

	public static boolean secureRandom_50_50() {
		return (secureRandomInt(0, 1) == 1);
	}

	public static int secureRandomInt(int min, int max) {
		initSecureRandom();

		if(min < max) {
			final int RESULT = (int)((max - min + 1) * _Random_.nextDouble() + min);

			if(RESULT > max) {
				throw new RuntimeException("Secure Random Number Error: Result Is Greater Than Max Number");
			} else if(RESULT < min) {
				throw new RuntimeException("Secure Random Number Error: Result Is Less Than Min Number");
			} else {
				return RESULT;
			}
		} else if(min == max) {
			return min;
		} else {
			throw new RuntimeException();
		}
	}

	public static SecureRandom getSecureRandom() {
		initSecureRandom();

		return _Random_;
	}

	private static void initSecureRandom() {
		if(_Random_ == null) {
			_Random_ = new SecureRandom(BitOperations.longToBytes(System.currentTimeMillis()));
			_Random_.nextInt();
		}
	}
}
