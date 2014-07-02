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

import java.text.DecimalFormat;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 *     
 * September 12, 2011 (Version 1.0.0)
 *     -Added
 *        -Counter (Keeps Track of Past Time)
 *     
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class StopWatch {
	public enum Mode {
		SECONDS,
		MINUTES,
		HOURS;
	};

	private long _Start = 0;
	private long _Stop = 0;

	private long _Counter = 0;

	private boolean _IsRunning = false;

	public StopWatch() {
		this(false);
	}

	public StopWatch(boolean autostart) {
		if(autostart) {
			start();
		}
	}

	public void start() {
		if(!_IsRunning) {
			_IsRunning = true;
			_Stop = 0;
			_Start = System.currentTimeMillis();
		}
	}

	public void stop() {
		if(_IsRunning) {
			_Stop = System.currentTimeMillis();
			_Counter += _Stop;
			_IsRunning = false;
		}
	}

	public void reset() {
		_Start = 0;
		_Stop = 0;
		_Counter = 0;
		_IsRunning = false;
	}

	public String resultStr() {
		if(_IsRunning) {
			stop();
		}
		return (Double.toString(_Stop - _Start));
	}

	public boolean isRunning() {
		return _IsRunning;
	}

	public long getCounter() {
		return _Counter;
	}

	public long result() {
		if(_IsRunning) {
			stop();
		}

		return _Stop - _Start;
	}

	public double resultInSeconds() {
		if(_IsRunning) {
			stop();
		}

		final long RESULTS = _Stop - _Start;
//        double Seconds = RESULTS * 0.001;//1000.0;

		return (RESULTS * 0.001);
	}

	public double resultInMinutes() {
		if(_IsRunning) {
			stop();
		}

		return resultInSeconds() / 60;
	}

	public double resultInHours() {
		if(_IsRunning) {
			stop();
		}

		return resultInMinutes() / 60;
	}

	public String resultsln(Mode mode) {
		final DecimalFormat FORMAT = new DecimalFormat("######.###");

		switch(mode) {
			case MINUTES:
				return FORMAT.format(resultInMinutes()) + " Minute(s)";
			case HOURS:
				return FORMAT.format(resultInHours()) + " Hour(s)";
			case SECONDS:
			default:
				return FORMAT.format(resultInSeconds()) + " Second(s)";
		}
	}

	@Override
	public String toString() {
		if(_IsRunning) {
			return "Start: " + _Start;
		} else {
			return "Start: " + _Start + " Stop: " + _Stop;
		}
	}
/*
	public static void main(String[] args) {
		StopWatch Watch = new StopWatch();
		
		Watch.start();
		
		try {
			Thread.sleep(1000);
		} catch (Exception e) {}
		
		System.out.println(Watch.resultInSeconds());
	}
*/
}