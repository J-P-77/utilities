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

import jp77.utillib.interfaces.ITimeOut;

import jp77.utillib.utilities.ThreadUtil;

public class ThreadTimeOutTimerWListener extends Thread {
	private final ITimeOut _LISTENER;

	private final int _TIME_CHECK;

	private final TimeOutTimer _TIMER;

	private boolean _Cancel = false;

	public ThreadTimeOutTimerWListener(int timeout, ITimeOut listener) {
		this(timeout, 100, false, listener);
	}

	public ThreadTimeOutTimerWListener(int timeout, boolean autostart, ITimeOut listener) {
		this(timeout, 100, autostart, listener);
	}

	public ThreadTimeOutTimerWListener(int timeout, int timecheck, boolean autostart, ITimeOut listener) {
		if(timeout < 0) {
			throw new RuntimeException("Variable[timeout] - Must Be Equal Too Or Greater Than Zero");
		}

		if(timecheck < 0) {
			throw new RuntimeException("Variable[timecheck] - Must Be Greater Than Zero");
		}

		if(listener == null) {
			throw new RuntimeException("Variable[listener] - Is Null");
		}

		_TIMER = new TimeOutTimer(timeout);
		_TIME_CHECK = timecheck;
		_LISTENER = listener;

		if(autostart) {
			this.start();
		}
	}

	@Override
	public void run() {
		while(!_Cancel) {
			ThreadUtil.sleep(_TIME_CHECK);

			if(_TIMER.hasTimedOut()) {
				_LISTENER.timedout();
				break;
			}
		}
	}

	public boolean isCanceling() {
		return _Cancel;
	}

	public void cancel() {
		_Cancel = true;
	}

	public int timeLeft() {
		return _TIMER.timeLeft();
	}

	@Override
	protected void finalize() throws Throwable {
		cancel();

		super.finalize();
	}
}
