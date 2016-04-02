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

public class TimeOutTimer {
	private long _Start = System.currentTimeMillis();

	private long _Timeout = 0;

	private boolean _TimedOut = false;

	/**
	 * 
	 * @param timeout
	 *            millis before timeout
	 */
	public TimeOutTimer(int timeout) {
		setTimeOut(timeout);
	}

	public void reset() {
		_Start = System.currentTimeMillis();
		_TimedOut = false;
	}

	public void setTimeOut(long timeout) {
		if(timeout < 0) {
			throw new RuntimeException("Variable[timeout] - Must Be Equal Too Or Greater Than Zero");
		}

		if(timeout == 0) {
			_TimedOut = true;
		}

		_Timeout = timeout;
	}

	public long getTimeout() {
		return _Timeout;
	}

	public boolean hasTimedOut() {
		if(_TimedOut) {
			return true;
		}

		final long TIME = System.currentTimeMillis();

//		System.out.println((_Start + _Time) + " < " + TIME);

		if((_Start + _Timeout) < TIME) {
			return (_TimedOut = true);
		}

		return false;
	}

	public int timeLeft() {
		return (int)((_Start + _Timeout) - System.currentTimeMillis());
	}
}
