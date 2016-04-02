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

import java.text.SimpleDateFormat;

import java.util.Date;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * April 20, 2009 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class DateUtil {
	public static enum Day {
		SUNDAY("Sunday"),
		MONDAY("Monday"),
		TUESDAY("Tuesday"),
		WEDNESDAY("Wednesday"),
		THURSDAY("Thursday"),
		FRIDAY("Friday"),
		SATURDAY("Saturday");

		private final String _Value;

		private Day(String value) {
			_Value = value;
		}

		public String getValue() {
			return _Value;
		}

		public static Day getCalendarDay(int calendarday) {
			switch(calendarday) {
				case 1:
					return SUNDAY;
				case 2:
					return MONDAY;
				case 3:
					return TUESDAY;
				case 4:
					return WEDNESDAY;
				case 5:
					return THURSDAY;
				case 6:
					return FRIDAY;
				case 7:
					return SATURDAY;
			}

			return null;
		}

		public static Day getCalendarDay(String day) {
			final String DAY = day.trim().toUpperCase();

			try {
				return Day.valueOf(DAY);
			} catch(Exception e) {}

			return null;
		}
	};

	public static String getDateAndTime(String pattern) {
		SimpleDateFormat DateFormat = new SimpleDateFormat(pattern);

		return DateFormat.format(new Date());
	}

	public static int to24hr(int hr12, boolean pm) {
		if(pm) {
			return (hr12 == 12 ? 12 : hr12 + 12);
//            if(hr12 == 12) {
//                return 12;
//            } else {
//                return hr12 + 12;
//            }
		} else {
			return (hr12 == 12 ? 0 : hr12);
//            if(hr12 == 12) {
//                return 0;
//            } else {
//                return hr12;
//            }
		}
	}

	public static int to12hr(int hr24) {
		if(hr24 == 0) {
			return 12;
		} else {
			return (hr24 <= 12 ? hr24 : hr24 - 12);
//            if(hr24 <= 12) {
//                return hr24;
//            } else {
//                return hr24 - 12;
//            }
		}
	}

	public static boolean isPm(int hr24) {
		return (hr24 < 12 ? false : true);
//        if(hr24 <= 12) {
//            return false;
//        } else {
//            return true;
//        }
	}
}
