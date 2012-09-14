package utillib.table;

import java.util.Comparator;

/**
 * November 27, 2008 (Version 1.0.0)<br>
 * -First Released<br>
 * <br>
 * 
 * @author Justin Palinkas<br>
 * <br>
 *         Current Version 1.0.0
 */

public class SortString implements Comparator {
	private boolean _MatchCase = false;

	public void matchCase(boolean value) {
		_MatchCase = value;
	}

	public int compare(Object o1, Object o2) {
		final String Str1 = (_MatchCase ? (String)o1 : ((String)o1).toLowerCase());
		final String Str2 = (_MatchCase ? (String)o2 : ((String)o2).toLowerCase());

		int Len1 = Str1.length();
		int Len2 = Str2.length();
		int Pos = 0;
		while(Pos < Len1 && Pos < Len2) {
			int C1 = Str1.charAt(Pos);
			int C2 = Str2.charAt(Pos);

			if(C1 < C2) {
				return -1;
			} else if(C1 > C2) {
				return 1;
			}

			Pos++;
		}

		return 0;
	}
}
