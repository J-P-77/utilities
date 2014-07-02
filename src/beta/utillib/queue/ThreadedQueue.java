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

import utillib.arrays.ResizingArray;

import utillib.collections.MyStackAll;

public/*abstract*/class ThreadedQueue {
	private final Object __LOCK__ = new Object();

	private Worker _WorkerThread = null;

	private final MyStackAll<QueueTask> _QUEUE = new MyStackAll<QueueTask>();
	private boolean _ShutDown = false;
	private boolean _Pause = false;

	private final ResizingArray<IQueue> _LISTENERS = new ResizingArray<IQueue>();

	private final MyStackAll<QueueTask> _WORKINGON = new MyStackAll<QueueTask>();

	public ThreadedQueue() {}

	public void startup() {
		if(_WorkerThread == null) {
			_WorkerThread = new Worker();
			_ShutDown = false;
			_WorkerThread.start();
		} else {
			throw new RuntimeException("Alreadly Started");
		}
	}

	public void shutdown() {
		_ShutDown = true;

		_WorkerThread = null;

//        fireCanceled();
	}

	public void addTask(QueueTask task) {
		synchronized(__LOCK__) {
			_QUEUE.push(task);

			fireTaskAdded(task);
		}
	}

	public void removeTask(QueueTask task) {
		synchronized(__LOCK__) {
			final QueueTask TEMP = _QUEUE.removeItem(task);

			if(TEMP != null) {
				fireTaskRemoved(TEMP);
			}
		}
	}

	public void removeTask(int index) {
		synchronized(__LOCK__) {
			final QueueTask TEMP = _QUEUE.removeItemAt(index);

			if(TEMP != null) {
				fireTaskRemoved(TEMP);
			}
		}
	}

	public int taskCount() {
		return _QUEUE.length();
	}

	public ResizingArray<IQueue> getQueueListeners() {
		return _LISTENERS;
	}

	public void pause(boolean value) {
		_Pause = value;
	}

	public boolean isPaused() {
		return _Pause;
	}

	public boolean isRunning() {
		return !_ShutDown;
	}

	@Override
	protected void finalize() throws Throwable {
		shutdown();

		super.finalize();
	}

	//CLASSES
	private class Worker extends Thread {

		@Override
		public void run() {
			fireStarted();

			while(!_ShutDown) {
				if(pauseCancelCheck()) {
					break;
				}

				final QueueTask WORKING_ON;
				synchronized(__LOCK__) {
					WORKING_ON = _QUEUE.popItemAt(0);
				}

				if(WORKING_ON == null) {
					System.out.println("!!!ERROR!!!");
				} else {
//                    _SENDER.taskRemoved(WORKING_ON);

					System.out.println("start Task " + WORKING_ON.getName());

					_WORKINGON.push(WORKING_ON);

					fireStartTask(WORKING_ON);
				}
			}

			fireShutdowned();
		}

		private boolean pauseCancelCheck() {
			while(_Pause || _QUEUE.isEmpty() || !fireCanAcceptTask()) {
				try {
					for(int X = (_WORKINGON.length() - 1); X > -1; X--) {
						if(!_WORKINGON.getItemAt(X).isRunning()) {
							System.out.println("removed Task " + _WORKINGON.getItemAt(X).getName());

							final QueueTask TEMP = _WORKINGON.removeItemAt(X);

							fireEndTask(TEMP);
						}

						Thread.sleep(10);
					}

					if(_ShutDown) {
						return true;
					}
					Thread.sleep(200);
				} catch(Exception e) {}
			}

			return _ShutDown;
		}
	}

	private boolean fireCanAcceptTask() {
		for(int X = 0; X < _LISTENERS.length(); X++) {
			if(_LISTENERS.getAt(X).canAcceptTask()) {
				return true;
			}
		}

		return false;
	}

	private void fireTaskAdded(QueueTask task) {
		for(int X = 0; X < _LISTENERS.length(); X++) {
			_LISTENERS.getAt(X).taskAdded(task);
		}
	}

	private void fireTaskRemoved(QueueTask task) {
		for(int X = 0; X < _LISTENERS.length(); X++) {
			_LISTENERS.getAt(X).taskRemoved(task);
		}
	}

	private void fireStartTask(QueueTask task) {
		for(int X = 0; X < _LISTENERS.length(); X++) {
			if(_LISTENERS.getAt(X).canAcceptTask()) {
				_LISTENERS.getAt(X).startTask(task);
			}
		}
	}

	private void fireEndTask(QueueTask task) {
		for(int X = 0; X < _LISTENERS.length(); X++) {
			_LISTENERS.getAt(X).endTask(task);
		}
	}

	private void fireStarted() {
		for(int X = 0; X < _LISTENERS.length(); X++) {
			_LISTENERS.getAt(X).started();
		}
	}

	private void fireShutdowned() {
		for(int X = 0; X < _LISTENERS.length(); X++) {
			_LISTENERS.getAt(X).shutdowned();
		}
	}

//    private void fireCanceled() {
//        for(int X = 0; X < _LISTENERS.length(); X++) {
//            _LISTENERS.getAt(X).canceled();
//        }
//    }
//
//    private void firePaused() {
//        for(int X = 0; X < _LISTENERS.length(); X++) {
//            _LISTENERS.getAt(X).paused();
//        }
//    }
}
