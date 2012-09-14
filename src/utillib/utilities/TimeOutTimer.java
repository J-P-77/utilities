package utillib.utilities;

public class TimeOutTimer {
	private long _Start = System.currentTimeMillis();

	private long _Timeout = 0;

	private boolean _TimedOut = false;

	/**
	 * 
	 * @param timeout
	 *            millis before timeout
	 */
	public TimeOutTimer(int timeout) {
		setTimeOut(timeout);
	}

	public void reset() {
		_Start = System.currentTimeMillis();
		_TimedOut = false;
	}

	public void setTimeOut(long timeout) {
		if(timeout < 0) {
			throw new RuntimeException("Variable[timeout] - Must Be Equal Too Or Greater Than Zero");
		}

		if(timeout == 0) {
			_TimedOut = true;
		}

		_Timeout = timeout;
	}

	public long getTimeout() {
		return _Timeout;
	}

	public boolean hasTimedOut() {
		if(_TimedOut) {
			return true;
		}

		final long TIME = System.currentTimeMillis();

//		System.out.println((_Start + _Time) + " < " + TIME);

		if((_Start + _Timeout) < TIME) {
			return (_TimedOut = true);
		}

		return false;
	}

	public int timeLeft() {
		return (int)((_Start + _Timeout) - System.currentTimeMillis());
	}
}
