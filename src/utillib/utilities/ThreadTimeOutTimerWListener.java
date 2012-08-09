package utillib.utilities;

import utillib.interfaces.ITimeOut;

import utillib.utilities.ThreadUtil;

public class ThreadTimeOutTimerWListener extends Thread {
	private final ITimeOut _LISTENER;

	private final int _TIME_CHECK;

	private final TimeOutTimer _TIMER;

	private boolean _Cancel = false;

	public ThreadTimeOutTimerWListener(int timeout, ITimeOut listener) {
		this(timeout, 100, false, listener);
	}

	public ThreadTimeOutTimerWListener(int timeout, boolean autostart, ITimeOut listener) {
		this(timeout, 100, autostart, listener);
	}

	public ThreadTimeOutTimerWListener(int timeout, int timecheck, boolean autostart, ITimeOut listener) {
		if(timeout < 0) {
			throw new RuntimeException("Variable[timeout] - Must Be Equal Too Or Greater Than Zero");
		}

		if(timecheck < 0) {
			throw new RuntimeException("Variable[timecheck] - Must Be Greater Than Zero");
		}

		if(listener == null) {
			throw new RuntimeException("Variable[listener] - Is Null");
		}

		_TIMER = new TimeOutTimer(timeout);
		_TIME_CHECK = timecheck;
		_LISTENER = listener;

		if(autostart) {
			this.start();
		}
	}

	@Override
	public void run() {
		while(!_Cancel) {
			ThreadUtil.sleep(_TIME_CHECK);

			if(_TIMER.hasTimedOut()) {
				_LISTENER.timedout();
				break;
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

	@Override
	protected void finalize() throws Throwable {
		cancel();

		super.finalize();
	}
}
