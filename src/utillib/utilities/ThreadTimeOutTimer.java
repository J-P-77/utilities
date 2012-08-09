package utillib.utilities;

import utillib.utilities.ThreadUtil;

public class ThreadTimeOutTimer extends Thread {
	private final int _TIME_CHECK;

	private boolean _TimedOut = false;

	private boolean _Cancel = false;

	private final TimeOutTimer _TIMER;

	public ThreadTimeOutTimer(int timeout) {
		this(timeout, 100, false);
	}

	public ThreadTimeOutTimer(int timeout, boolean autostart) {
		this(timeout, 100, autostart);
	}

	public ThreadTimeOutTimer(int timeout, int timecheck, boolean autostart) {
		if(timeout < 0) {
			throw new RuntimeException("Variable[timeout] - Must Be Equal Too Or Greater Than Zero");
		}

		if(timecheck < 0) {
			throw new RuntimeException("Variable[timecheck] - Must Be Greater Than Zero");
		}

		_TIMER = new TimeOutTimer(timeout);

		_TIME_CHECK = timecheck;

		if(autostart) {
			this.start();
		}
	}

	@Override
	public void run() {
		while(!_Cancel && !_TimedOut) {
			ThreadUtil.sleep(_TIME_CHECK);

			if(_TIMER.hasTimedOut()) {
				_TimedOut = true;
			}
		}
	}

	public boolean isCanceling() {
		return _Cancel;
	}

	public void cancel() {
		_Cancel = true;
	}

	public int timeLeft() {
		return _TIMER.timeLeft();
	}

	public boolean hasTimedOut() {
		return _TimedOut;
	}

	@Override
	protected void finalize() throws Throwable {
		cancel();

		super.finalize();
	}
}
