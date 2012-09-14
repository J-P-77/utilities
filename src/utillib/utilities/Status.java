package utillib.utilities;

import utillib.interfaces.IStatus;

public class Status implements IStatus {
	private static final byte _STATUS_NOT_STARTED_ = -1;
	private static final byte _STATUS_CANCELING_ = 0;
	private static final byte _STATUS_CANCELED_ = 1;
	private static final byte _STATUS_DONE_ = 2;

	private static final byte _STATUS_RUNNING_ = 3;
	private static final byte _STATUS_PAUSED_ = 4;

	private byte _Status = _STATUS_NOT_STARTED_;

	public void start() {
		_Status = _STATUS_RUNNING_;
	}

	public boolean isRunning() {
		return _Status == _STATUS_RUNNING_ || _Status == _STATUS_PAUSED_;
	}

	public void pause(boolean value) {
		if(_Status == _STATUS_RUNNING_) {
			_Status = _STATUS_PAUSED_;
		} else if(_Status == _STATUS_PAUSED_) {
			_Status = _STATUS_RUNNING_;
		}
	}

	public boolean hasStarted() {
		return _Status != _STATUS_NOT_STARTED_;
	}

	public boolean isPaused() {
		return _Status == _STATUS_PAUSED_;
	}

	public void done() {
		if(_Status == _STATUS_RUNNING_) {
			_Status = _STATUS_DONE_;
		} else {
			throw new RuntimeException("Variable[_Status] - Done Error");
		}
	}

	public boolean isDone() {
		return _Status == _STATUS_DONE_;
	}

	public void cancel() {
		_Status = _STATUS_CANCELING_;
	}

	public boolean isCanceling() {
		return _Status == _STATUS_CANCELING_;
	}

	public void canceled() {
		if(_Status == _STATUS_CANCELING_) {
			_Status = _STATUS_CANCELED_;
		} else {
			throw new RuntimeException("Variable[_Status] - Canceling Error");
		}
	}

	public boolean isCanceled() {
		return _Status == _STATUS_CANCELED_;
	}
}
