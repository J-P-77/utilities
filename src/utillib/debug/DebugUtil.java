package utillib.debug;

import utillib.strings.MyStringBuffer;

import java.text.SimpleDateFormat;

import java.util.Date;

/**
 * <pre>
 * <b>Current Version 1.1.0</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * April 13, 2009 (Version 1.0.1)
 *     -Updated
 *         -EveryThing
 * 
 * April 28, 2010 (Version 1.1.0)
 *     -Updated
 *         -EveryThing
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class DebugUtil {
	public static final String _DEFAULT_DATE_PATTERN_ = "MM,dd,yyyy";
	public static final String _DEFAULT_TIME_PATTERN_ = "hh:mm:ss aa";

	private static SimpleDateFormat _Date_Format = new SimpleDateFormat(_DEFAULT_DATE_PATTERN_);
	private static SimpleDateFormat _Time_Format = new SimpleDateFormat(_DEFAULT_TIME_PATTERN_);

	private DebugUtil() {}

	public static void setDatePattern(String pattern) {
		_Date_Format.applyPattern(pattern);
	}

	public static String getDatePattern() {
		return _Date_Format.toPattern();
	}

	public static SimpleDateFormat getDateFormat() {
		return _Date_Format;
	}

	public static void setTimePattern(String pattern) {
		_Time_Format.applyPattern(pattern);
	}

	public static String getTimePattern() {
		return _Time_Format.toPattern();
	}

	public static SimpleDateFormat getTimeFormat() {
		return _Time_Format;
	}

	public static Date getNow() {
		return new Date();
	}

	public static String getDateAndTime() {
		return getDateAndTime(new Date());
	}

	public static String getDateAndTime(long datevalue) {
		return getDateAndTime(new Date(datevalue));
	}

	public static String getDateAndTime(Date date) {
		return getDateAndTime(_Date_Format, _Time_Format, date);
	}

	public static String getDateAndTime(String datepatten, String timepattern) {
		return getDateAndTime(new SimpleDateFormat(datepatten), new SimpleDateFormat(timepattern), new Date());
	}

	public static String getDateAndTime(String datepatten, String timepattern, Date date) {
		return getDateAndTime(new SimpleDateFormat(datepatten), new SimpleDateFormat(timepattern), date);
	}

	public static String getDateAndTime(SimpleDateFormat dateformat, SimpleDateFormat timeformat, Date date) {
		return dateformat.format(date) + ' ' + timeformat.format(date);
	}

	public static void createTrace(StackTraceElement traceelement, MyStringBuffer outbuffer) {
		outbuffer.append(traceelement.getClassName());
		outbuffer.append('.');
		outbuffer.append(traceelement.getMethodName());

		if(traceelement.isNativeMethod()) {
			outbuffer.append("(Native Method)");
		} else {
			if(traceelement.getFileName() != null && traceelement.getLineNumber() > -1) {
				outbuffer.append('(');
				outbuffer.append(traceelement.getFileName());
				outbuffer.append(':');
				outbuffer.append(traceelement.getLineNumber(), true);
				outbuffer.append(')');
			} else {
				if(traceelement.getFileName() == null) {
					outbuffer.append("(Unknown Source)");
				} else {
					outbuffer.append('(');
					outbuffer.append(traceelement.getFileName());
					outbuffer.append(')');
				}
			}
		}
	}
}
/*
Letter 	Date or Time			Component Presentation 	Examples
G		Era designator			Text					AD
y		Year					Year					1996; 96
M		Month in year			Month					July; Jul; 07
w		Week in year			Number					27
W		Week in month			Number					2
D		Day in year				Number					189
d		Day in month			Number					10
F		Day of week in month 	Number					2
E		Day in week				Text					Tuesday; Tue
a		Am/pm marker			Text					PM
H		Hour in day (0-23)		Number					0
k		Hour in day (1-24)		Number					24
K		Hour in am/pm (0-11) 	Number					0
h		Hour in am/pm (1-12) 	Number					12
m		Minute in hour			Number					30
s		Second in minute		Number					55
S		Millisecond				Number					978
z		Time zone				General time zone		Pacific Standard Time; PST; GMT-08:00
Z		Time zone				RFC 822 time zone		-0800
*/