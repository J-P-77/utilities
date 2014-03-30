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

package utillib.utilities;

import java.lang.Thread.State;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * May 05, 2009 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class ThreadUtil {
	private static Exception _LastException = null;

	/**
	 * Creates New Thread.
	 * 
	 * @param name
	 *            Name Of Thread
	 * @return new thread
	 */
	public static Thread create(String name) {
		return new Thread(name);
	}

	/**
	 * Creates New Thread.
	 * 
	 * @param target
	 *            implements Runnable target
	 * @return new thread
	 */
	public static Thread create(Runnable target) {
		return new Thread(target);
	}

	/**
	 * Creates New Thread.
	 * 
	 * @param name
	 *            Name Of Thread
	 * @param target
	 *            implements Runnable target
	 * @return new thread
	 */
	public static Thread create(String name, Runnable target) {
		return new Thread(target, name);
	}

	/**
	 * Pauses Thread.
	 * 
	 * @param millis
	 *            time for thread to sleep
	 * @return true if sleep was successful, false if failed
	 */
	public static boolean sleep(int millis) {
		try {
			Thread.sleep(millis);
			return true;
		} catch(Exception e) {
			_LastException = e;
			return false;
		}
	}

	/**
	 * Pauses Thread.
	 * 
	 * @param millis
	 *            millis seconds for thread to sleep
	 * @param nanos
	 *            nano seconds for thread to sleep
	 * @return true if sleep was successful, false if failed
	 */
	public static boolean sleep(long millis, int nanos) {
		try {
			Thread.sleep(millis, nanos);
			return true;
		} catch(Exception e) {
			_LastException = e;
			return false;
		}
	}

	/**
	 * Waits For Thread To Die.
	 * 
	 * @param thread
	 *            thread to join
	 * @return true if join was successful, false if failed
	 */
	public static boolean join(Thread thread) {
		try {
			thread.join();
			return true;
		} catch(Exception e) {
			_LastException = e;
			return false;
		}
	}

	/**
	 * Waits For Thread To Die.
	 * 
	 * @param thread
	 *            thread to join
	 * @param millis
	 *            Waits at most millis milliseconds for this thread to die. A
	 *            timeout of 0 means to wait forever.
	 * @return true if join was successful, false if failed
	 */
	public static boolean join(Thread thread, int millis) {
		try {
			thread.join(millis);
			return true;
		} catch(Exception e) {
			_LastException = e;
			return false;
		}
	}

	/**
	 * Waits For Thread To Die.
	 * 
	 * @param thread
	 *            thread to join
	 * @param millis
	 *            Waits at most millis milliseconds for this thread to die. A
	 *            timeout of 0 means to wait forever.
	 * @param nanos
	 *            nano seconds for thread to sleep
	 * @return true if join was successful, false if failed
	 */
	public static boolean join(Thread thread, long millis, int nanos) {
		try {
			thread.join(millis, nanos);
			return true;
		} catch(Exception e) {
			_LastException = e;
			return false;
		}
	}

	/**
	 * Waits For Thread To Die. If Thread Was Not Started Method Will Start
	 * Thread.
	 * 
	 * @param thread
	 *            to wait on
	 * @return true if wait was successful, false if failed
	 */
	public static boolean wait(Thread thread) {
		while(thread.isAlive()) {
			if(!sleep(10)) {
				return false;
			}
		}

		return true;
	}

	public static boolean startAndWait(Thread thread) {
		if(thread.getState() == Thread.State.NEW) {
			thread.start();
		}

		while(thread.isAlive()) {
			if(!sleep(10)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Gets Last Thread Error.
	 * 
	 * @return last thread error Exception
	 */
	public static Exception getLastError() {
		return _LastException;
	}
}
