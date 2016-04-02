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

package jp77.utillib.interfaces;

/**
 * 
 * @author Justin Palinkas
 */
public interface IDebugLogger {
	public static final String _LOG_EVENT_INFORMATION_ = "I - ";
	public static final String _LOG_EVENT_WARNING_ = "W - ";
	public static final String _LOG_EVENT_ERROR_ = "E - ";
	public static final String _LOG_EVENT_UNKNOWN_ = "U - ";

	public enum Log_Level {
		NONE(0),
		ERROR(1),
		WARNING(2),
		INFORMATION(3),
		//PLAIN(4),
		ALL(4);

		private final int _Level;

		private Log_Level(int level) {
			_Level = level;
		}

		public int getLevel() {
			return _Level;
		}
	}

	public void setLevel(Log_Level level);

	public Log_Level getLevel();

	public void printInformation(String msg);

	public void printWarning(String msg);

	public void printError(String msg);

	public void printError(Throwable e);

	public void printBlank(String msg);

	public void printCustom(String name, String logtype, String msg);
}
