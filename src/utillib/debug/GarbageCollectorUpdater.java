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

package utillib.debug;

import utillib.interfaces.IGarbageUpdater;

import utillib.utilities.Convert;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * October 20, 2008 (Version 1.1.0)
 *     -Added
 *          -Option To Auto Collect Trash
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class GarbageCollectorUpdater {
	private static final Runtime _RUNTIME_ = Runtime.getRuntime();

	private final IGarbageUpdater _UPDATER;
	private final Timer _Timer;

	private long _PreviousFree = 0;
	private long _PreviousMax = 0;
	private long _PreviousTotal = 0;

	private final boolean _AUTO_COLLECT_TRASH;

	public GarbageCollectorUpdater() {
		this(true, true);
	}

	public GarbageCollectorUpdater(boolean autostart, boolean autocollect) {
		this(autostart, autocollect, -1, null);
	}

	public GarbageCollectorUpdater(boolean autostart, boolean autocollect, IGarbageUpdater update) {
		this(autostart, autocollect, -1, update);
	}

	public GarbageCollectorUpdater(boolean autostart, boolean autocollect, int delay, IGarbageUpdater update) {
		_UPDATER = (update == null ? new DefaultUpdater() : update);
		_AUTO_COLLECT_TRASH = autocollect;

		_Timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final long FREE = _RUNTIME_.freeMemory();
				final long MAX = _RUNTIME_.maxMemory();
				final long TOTAL = _RUNTIME_.totalMemory();

				if(FREE != _PreviousFree) {
					_UPDATER.updateFreeMemory(FREE);
					_PreviousFree = FREE;
				}

				if(MAX != _PreviousMax) {
					_UPDATER.updateMaxMemory(MAX);
					_PreviousFree = MAX;
				}

				if(TOTAL != _PreviousTotal) {
					_UPDATER.updateTotalMemory(TOTAL);
					_PreviousFree = TOTAL;
				}

				_UPDATER.updateAll(getAll(" - "));

				if(_AUTO_COLLECT_TRASH) {
					collectTrash();
				}
			}
		});

		//5 Seconds
		setDelay((delay < 0 ? 5 * 1000 : delay));

		if(autostart) {
			start();
		}
	}

	public void start() {
		_Timer.start();
	}

	public void stop() {
		_Timer.stop();
	}

	public void setDelay(int millivalue) {
		_Timer.setDelay(millivalue);
	}

	public boolean isRunning() {
		return _Timer.isRunning();
	}

	public IGarbageUpdater getUpdater() {
		return _UPDATER;
	}

	public static String getAll() {
		return getAll(' ');
	}

	public static String getAll(char separator) {
		return (getMaxMemoryStr() + separator + getTotalMemoryStr() + separator + getFreeMemoryStr());
	}

	public static String getAll(String separator) {
		return (getMaxMemoryStr() + separator + getTotalMemoryStr() + separator + getFreeMemoryStr());
	}

	public static String getMaxMemoryStr() {
		return "Max: " + Convert.convertBytes(_RUNTIME_.maxMemory());
	}

	public static String getTotalMemoryStr() {
		return "Total: " + Convert.convertBytes(_RUNTIME_.totalMemory());
	}

	public static String getFreeMemoryStr() {
		return "Free: " + Convert.convertBytes(_RUNTIME_.freeMemory());
	}

	public static long getMaxMemory() {
		return _RUNTIME_.maxMemory();
	}

	public static long getTotalMemory() {
		return _RUNTIME_.totalMemory();
	}

	public static long getFreeMemory() {
		return _RUNTIME_.freeMemory();
	}

	public static Runtime getRuntime() {
		return _RUNTIME_;
	}

	public static void collectTrash() {
		_RUNTIME_.runFinalization();
		_RUNTIME_.gc();
	}

	@Override
	protected void finalize() throws Throwable {
		if(isRunning()) {
			stop();
		}

		super.finalize();
	}

	private class DefaultUpdater implements IGarbageUpdater {
		public void updateAll(String value) {
			System.out.print(value);
		}

		public void updateFreeMemory(long value) {}

		public void updateMaxMemory(long value) {}

		public void updateTotalMemory(long value) {}
	}
}