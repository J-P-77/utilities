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

package utillib.arguments;

import utillib.strings.StringUtil;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * April 01, 2009 (version 1.0.0)
 *     -Updated
 *         -EveryThing
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class ArgumentsUtil {
	@Deprecated
	public static final String[] _TRUE_ = {"true", "t", "yes", "1"};
	@Deprecated
	public static final String[] _FALSE_ = {"false", "f", "no", "0"};

	/**
	 * 
	 * @param value
	 * @return
	 * @deprecated Use utillib.strings.StringUtil Class
	 */
	@Deprecated
	public static boolean isNumber(String value) {
		return StringUtil.isNumber(value);
	}

	/**
	 * 
	 * @param value
	 * @return
	 * @deprecated Use StringUtil Class
	 */
	@Deprecated
	public static boolean isWholeNumber(String value) {
		return StringUtil.isWholeNumber(value);
	}

	public static boolean isBoolean(String value) {
		if(value == null || value.length() == 0) {
			return false;
		} else {
			final String VALUE = value.toLowerCase();
			for(int X = 0; X < _TRUE_.length; X++) {
				if(VALUE.equals(_TRUE_[X]) || VALUE.equals(_FALSE_[X])) {
					return true;
				}
			}

			return false;
		}
	}

	@Deprecated
	public static boolean getBooleanValue(String value) {
		return StringUtil.getBooleanValue(value, false);
	}

	@Deprecated
	public static boolean getBooleanValue(String value, boolean defaultvalue) {
		return StringUtil.getBooleanValue(value, defaultvalue);
	}
}
