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

package utillib.debug;

import utillib.interfaces.IDebugLogger.Log_Level;

/**
 * Log Start Message String (ex. "!!!ERROR!!! - ") - (LogEvent) + (Log Message)
 * 
 * @author Justin Palinkas
 */
public class LogEvent {
	private final long _TIME;
	private final Log_Level _LEVEL;
	private final String _MSG;

//    private final Class<?> _CLASS;
	private final Throwable _THROWABLE;

	public LogEvent(Log_Level level, String msg) {
		this(level, msg, null/*, null*/);
	}

//    public LogEvent(Log_Level level, String msg, Class<?> clazz) {
//    	this(level, msg, clazz, null);
//    }

	public LogEvent(Log_Level level, String msg,/* Class<?> clazz,*/Throwable throwable) {
//    	_CLASS = clazz;
		_TIME = System.currentTimeMillis();
		_MSG = msg;
		_LEVEL = level;
		_THROWABLE = throwable;
	}

	public long getTime() {
		return _TIME;
	}

	public Log_Level getLevel() {
		return _LEVEL;
	}

	public String getMsg() {
		return _MSG;
	}

	public Throwable getThrowable() {
		return _THROWABLE;
	}
//    public Class<?> getClazz() {return _CLASS;}
}
