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

package jp77.beta.utillib.queue.v1;

import jp77.beta.utillib.queue.IQueue;
import jp77.beta.utillib.queue.QueueTask;

import jp77.utillib.utilities.ThreadUtil;
import jp77.utillib.utilities.TimeOutTimer;

import jp77.utillib.collections.MyStackAll;

public class ThreadQueue {
	private final Object __LOCK__ = new Object();

	private Worker _WorkerThread = null;
	private Cleanup _CleanupThread = null;

	private final MyStackAll<QueueTask> _QUEUE = new MyStackAll<QueueTask>();
	private boolean _ShutDown = false;
	private boolean _Pause = false;

	private final IQueue _LISTENERS;

	private final MyStackAll<QueueTask> _WORKINGON = new MyStackAll<QueueTask>();

	public ThreadQueue(IQueue listener) {
		if(listener == null) {
			throw new RuntimeException("Variable[listener] - Is Null");
		}

		_LISTENERS = listener;
	}

	public ThreadQueue(IQueue listener, QueueTask initialtask) {
		this(listener);

		_QUEUE.push(initialtask);
	}

	public ThreadQueue(IQueue listener, MyStackAll<QueueTask> initialtasks) {
		this(listener);

		for(int X = 0; X < initialtasks.length(); X++) {
			_QUEUE.push(initialtasks.getItemAt(X));
		}
	}

	public ThreadQueue(IQueue listener, QueueTask[] initialtasks) {
		this(listener);

		for(int X = 0; X < initialtasks.length; X++) {
			_QUEUE.push(initialtasks[X]);
		}
	}

	public void startup() {
		if(_WorkerThread == null && _CleanupThread == null) {
			_ShutDown = false;
			_WorkerThread = new Worker();

			_CleanupThread = new Cleanup();
			_CleanupThread.setPriority(Thread.MIN_PRIORITY);

			_WorkerThread.start();
			_CleanupThread.start();
		} else {
			throw new RuntimeException("Alreadly Started");
		}
	}

	public void shutdown() {
		shutdown(200);
	}

//  //TODO (Keep working on this)
//    public void shutdown(int maxtimetowait) {
//        if(maxtimetowait <= 0) {
//            throw new RuntimeException("Variable[maxtimetowait] - Must Be Greater Than Zero");
//        }
//        
//        if(!_ShutDown) {
//            _ShutDown = true;
//
//            final long START = System.currentTimeMillis();
//            final long ENDTIME = START + maxtimetowait;
//            long Current = ENDTIME;
//            System.out.println("QueueBeta1_shutdown_START  =" + START);
//            System.out.println("QueueBeta1_shutdown_ENDTIME=" + ENDTIME);
//            while(true) {            	
//                if(!_WorkerThread.isAlive() && !_CleanupThread.isAlive()) {
//                    break;
//                }
//    //START 1279246355421
//    //Current 1279246360427
//
//    //START   55421 + 5000
//    //Current 60427
//                if(Current > ENDTIME) {
//                    System.out.println("QueueBeta1_shutdown_interrupted");
//                    
//                    try {
//                        _WorkerThread.interrupt();
//                    } catch (Exception e) {}
//                    try {
//                        _CleanupThread.interrupt();
//                    } catch (Exception e) {}
//                    break;
//                }
//
//                try {
//                    Thread.sleep(10);
//                } catch (Exception e) {}
//
//                Current = (System.currentTimeMillis());
//            }
//            
//            System.out.println("Current=" + Current);
//
//            _WorkerThread = null;
//            _CleanupThread = null;
//        }
//    }

	public void shutdown(int maxtimetowait) {
		if(maxtimetowait <= 0) {
			throw new RuntimeException("Variable[maxtimetowait] - Must Be Greater Than Zero");
		}

		final TimeOutTimer TIMEOUT = new TimeOutTimer(maxtimetowait);

		if(!_ShutDown) {
			_ShutDown = true;

			while(!TIMEOUT.hasTimedOut()) {
				ThreadUtil.sleep(100);
			}

			if(_WorkerThread.isAlive()) {
				try {
					_WorkerThread.interrupt();
					System.out.println("[_WorkerThread] - interrupted");
				} catch(Exception e) {}
			}

			if(_CleanupThread.isAlive()) {
				try {
					_CleanupThread.interrupt();
					System.out.println("[_CleanupThread] - interrupted");
				} catch(Exception e) {}
			}

			_WorkerThread = null;
			_CleanupThread = null;
		}
	}

	public void addTask(QueueTask task) {
		synchronized(__LOCK__) {
			_QUEUE.push(task);
		}

		_LISTENERS.taskAdded(task);
	}

	public void removeTask(QueueTask task) {
		final QueueTask TEMP;
		synchronized(__LOCK__) {
			TEMP = _QUEUE.removeItem(task);
		}

		if(TEMP != null) {
			_LISTENERS.taskRemoved(TEMP);
		}
	}

	public void removeTask(int index) {
		final QueueTask TEMP;
		synchronized(__LOCK__) {
			TEMP = _QUEUE.removeItemAt(index);
		}

		if(TEMP != null) {
			_LISTENERS.taskRemoved(TEMP);
		}
	}

	public int taskCount() {
		return _QUEUE.length();
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
//            System.out.println("Worker Start");

			_LISTENERS.started();

			while(!_ShutDown) {
				if(pauseCancelCheck()) {
					break;
				}

				if(_QUEUE.isEmpty()) {
					continue;
				}

				final QueueTask WORKING_ON;
				synchronized(__LOCK__) {
					WORKING_ON = _QUEUE.popItemAt(0);
				}

				if(WORKING_ON == null) {
					System.out.println("!!!ERROR!!!");
				} else if(WORKING_ON.isCanceling()) {

				} else {
					System.out.println("start Task " + WORKING_ON.getName());

					_WORKINGON.push(WORKING_ON);

					_LISTENERS.startTask(WORKING_ON);
				}
			}

			_LISTENERS.shutdowned();

//            System.out.println("Worker Stop");
		}

		private boolean pauseCancelCheck() {
			while(_Pause || _QUEUE.isEmpty() || !_LISTENERS.canAcceptTask()) {
				try {
					if(_ShutDown) {
						return true;
					}
					Thread.sleep(200);
				} catch(Exception e) {}
			}

			return _ShutDown;
		}
	}

	private class Cleanup extends Thread {
		@Override
		public void run() {
//            System.out.println("Cleanup Start");
			while(!_ShutDown) {
				try {
					if(pauseCancelCheck()) {
						break;
					}

					for(int X = (_WORKINGON.length() - 1); X > -1; X--) {
						if(!_WORKINGON.getItemAt(X).isRunning()) {
							System.out.println("removed Task " + _WORKINGON.getItemAt(X).getName());

							final QueueTask TEMP;
							synchronized(__LOCK__) {
								TEMP = _WORKINGON.removeItemAt(X);
							}

							_LISTENERS.endTask(TEMP);
						}

						Thread.sleep(10);
					}
				} catch(Exception e) {}
			}
//            System.out.println("Cleanup Stop");
		}

		private boolean pauseCancelCheck() {
			while(_Pause || _WORKINGON.isEmpty()) {
				try {
					if(_ShutDown) {
						return true;
					}
					Thread.sleep(200);
				} catch(Exception e) {}
			}

			return _ShutDown;
		}
	}
}
