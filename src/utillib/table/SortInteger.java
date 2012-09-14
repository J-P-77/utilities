package utillib.table;

import java.util.Comparator;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 27, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class SortInteger implements Comparator {
	public int compare(Object o1, Object o2) {
		final int INT1 = (Integer)o1;
		final int INT2 = (Integer)o2;

		if(INT1 == INT2) {
			return 0;
		} else if(INT1 < INT2) {
			return -1;
		} else {
			return 1;
		}
	}
}
