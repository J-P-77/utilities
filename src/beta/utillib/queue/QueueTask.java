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

package beta.utillib.queue;

/**
 * 
 * @author Justin Palinkas
 */
public abstract class QueueTask implements Runnable {
	private static final int _STATUS_CANCELING_ = 0;
	private static final int _STATUS_CANCELED_ = 1;
	private static final int _STATUS_DONE_ = 2;

	private int _Status = -1;

	private boolean _Pause = false;

	private int _Priority = Thread.NORM_PRIORITY;

	public void setPriority(int priority) {
		if(priority > Thread.MAX_PRIORITY || priority < Thread.MIN_PRIORITY) {
			throw new RuntimeException("Variable[priority] - Illegal Argument");
		}

		_Priority = priority;
	}

	public int getPriority() {
		return _Priority;
	}

	/**
	 * Call To Cancel Task
	 */
	public void cancel() {
		_Status = _STATUS_CANCELING_;
	}

	public boolean isCanceling() {
		return _Status == _STATUS_CANCELING_;
	}

	/**
	 * Call When The Task Has Canceled
	 */
	protected void canceled() {
		_Status = _STATUS_CANCELED_;
	}

	public boolean isCanceled() {
		return _Status == _STATUS_CANCELED_;
	}

	/**
	 * Call If Task Has Successfully Completed
	 */
	public void done() {
		_Status = _STATUS_DONE_;
	}

	public boolean isDone() {
		return _Status == _STATUS_DONE_;
	}

	public boolean isRunning() {
		if(_Status == -1) {
			return true;
		}

		return (!isCanceling() && !isCanceled()) && !isDone();
	}

	public void pause(boolean value) {
		_Pause = value;
	}

	public boolean isPaused() {
		return _Pause;
	}

	public abstract String getName();

//    @Override
//    public abstract void run();

	@Override
	public String toString() {
		return getName();
	}

}
