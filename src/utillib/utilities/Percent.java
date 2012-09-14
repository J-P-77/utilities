package utillib.utilities;

import beta.utillib.Listener;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * October 28, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class Percent {
	private final Listener<IUpdatePercent> _LISTENERS = new Listener<IUpdatePercent>();

	private long _Total = 0l;
	private long _TotalCompleted = 0l;

	private int _Cur_Percent = 0;
	private int _Pre_Percent = 0;

	public Percent() {
		this(0);
	}

	public Percent(int total) {
		_Total = (long)total;
	}

	public Percent(long total) {
		_Total = total;
	}

	public void resetAll() {
		_Total = 0;
		_TotalCompleted = 0;
	}

	public void resetTotal() {
		_Total = 0;
	}

	public void resetCompleted() {
		_TotalCompleted = 0;
	}

	public void setTotal(long value) {
		_Total = value;
	}

	public long getTotal() {
		return _Total;
	}

	public void addTotal() {
		addTotal(1);
	}

	public void addTotal(int value) {
		_Total += value;
	}

	public void addTotal(long value) {
		_Total += value;
	}

	public void subTotal() {
		subTotal(1);
	}

	public void subTotal(int value) {
		_Total -= value;
	}

	public void subTotal(long value) {
		_Total -= value;
	}

	public void setCompleted(long value) {
		_TotalCompleted = value;
	}

	public long getCompleted() {
		return _TotalCompleted;
	}

	public void addCompleted() {
		addCompleted(1);
	}

	public void addCompleted(int value) {
		_TotalCompleted += value;
	}

	public void addCompleted(long value) {
		_TotalCompleted += value;
	}

	public void subCompleted() {
		subCompleted(1);
	}

	public void subCompleted(int value) {
		_TotalCompleted -= value;
	}

	public void subCompleted(long value) {
		_TotalCompleted -= value;
	}

	public String getStringPercent() {
		return getIntPercent() + "%";
	}

	public int getIntPercent() {
		final double PERCENT = getDblPercent();
		final double DBL_PERCENT = (PERCENT * 100);

		final int LNG_PERCENT = (int)DBL_PERCENT;

		return (LNG_PERCENT);
	}

	public double getDblPercent() {// * 100 To Get Percent
		if(_Total == 0 || _TotalCompleted == 0) {
			return 0;
		} else {
			final double TOTAL_COMPLETE = _TotalCompleted;
			final double PRECENT = (TOTAL_COMPLETE / _Total);

			return PRECENT;
		}
	}

	public void update() {
		_Cur_Percent = getIntPercent();
		if(_Cur_Percent != _Pre_Percent) {
			fire_updatePercent(_Cur_Percent + "%");
			_Pre_Percent = _Cur_Percent;
		}
	}

	public Listener<IUpdatePercent> getPropertyListener() {
		return _LISTENERS;
	}

	private void fire_updatePercent(String value) {
		for(int X = 0; X < _LISTENERS.listenerCount(); X++) {
			_LISTENERS.getListenerAt(X).updatePercent(value);
		}
	}

	public interface IUpdatePercent {
		public void updatePercent(String value);
	}
}
