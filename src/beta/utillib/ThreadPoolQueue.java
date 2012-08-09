package beta.utillib;

import beta.utillib.LinkedListExtended;

import utillib.utilities.ThreadUtil;
import utillib.utilities.TimeOutTimer;

/**
 * 
 * Not Quite Ready To Be Used
 * 
 * @author Dalton Dell
 */
public class ThreadPoolQueue {
	private static int _Id_ = 0;
	
    private final Object __QUEUE_LOCK__ = new Object();
    private final Object __IDLE_LOCK__ = new Object();
    
    private final LinkedListExtended<QueueTask> _QUEUE = new LinkedListExtended<QueueTask>();
    private int _Queue_Length = 0;
    
    private int _Min_Thread_Count = 1;
    private int _Max_Thread_Count = 1;
    private final LinkedListExtended<Thread> _THREADS = new LinkedListExtended<Thread>();
    
    private int _Current_Thread_Count = 0;
    private int _Idle_Thread_Count = 0;
    
    private boolean _ShutDown = false;
    private boolean _Started = false;
    
    private final Worker _WORKER = new Worker();
    
    private int _Idle_Time = 5 * 1000;

    private int _Threshold = 5;    

    public interface QueueTask extends Runnable {
    	public void run();
    }
    
    public ThreadPoolQueue(int min, int max, int threshold) {
    	_Min_Thread_Count = min;
    	_Max_Thread_Count = max;
    	_Threshold = threshold;
    }

//    public void setMin(int value) {}
  
    public int getMin() {
    	return _Min_Thread_Count;
    }

//    public void setMax(int value) {}

    public int getMax() {
    	return _Max_Thread_Count;
    }

//    public void setThreshold(int value) {}

    public int getThreshold() {
    	return _Threshold;
    }
    
    public int getQueueLength() {
    	return _Queue_Length;
    }
    
    
    public void start() {
		if(!_Started) {
			_Started = true;
			
			for(int X = 0; X < _Min_Thread_Count; X++) {
				startThread();
			}
		}
	}

	public void stop() {
		if(_Started) {
			_ShutDown = true;

			synchronized(__QUEUE_LOCK__) {
				while(!_QUEUE.isEmpty()) {
					_QUEUE.pop();
				}
			}
		}
	}

    public void addTask(QueueTask task) {
    	if(task == null) {
			throw new RuntimeException("Variable[task] - Is Null");
		}
    	
    	if(!_Started) {
    		throw new RuntimeException("Variable[_Started] - Not Started");
    	}
    	
        synchronized(__QUEUE_LOCK__) {
        	_QUEUE.push(task);
        	_Queue_Length++;
        }
    }
    
//    private boolean threasholdReached() {
//    	return (_QueueLength / _Current_Thread) > _Threshold;
//    }
    
    public void removeTask(QueueTask task) {
        synchronized(__QUEUE_LOCK__) {
        	_QUEUE.remove(task);
        	_Queue_Length--;
        }
    }

    public int taskCount() {
        return _Queue_Length;
    }

    public boolean isRunning() {
        return _Started;
    }

    @Override
    protected void finalize() throws Throwable {
        stop();

        super.finalize();
    }
    
	private void startThread() {
		final Thread T = new Thread(_WORKER);

		//    	T.setPriority(priority);
		T.setName("TQueue-" + _Id_++);

		System.out.println(T.getName() + " Created State");

		T.start();

		_THREADS.push(T);

		_Current_Thread_Count++;
	}
	
    //CLASSES
    private class Worker implements Runnable {
    	
    	@Override
        public void run() {    		
    		boolean Shutdown = false;
            while(!_ShutDown && !Shutdown) {
            	System.out.println(Thread.currentThread().getName() + " Running State");
            	
            	//Loop Thru Jobs
            	while(!_QUEUE.isEmpty() && !_ShutDown && !Shutdown) {
					synchronized(__IDLE_LOCK__) {
						if((_Queue_Length / _Current_Thread_Count) > _Threshold) {
							//Create More Thread If Necessary
							if(_Current_Thread_Count < _Max_Thread_Count && _Idle_Thread_Count == 0) {
								System.out.println("Tasks Per Thread: " + (_Queue_Length / _Current_Thread_Count));
								
								startThread();
							}
						} else {
							//Idle Threads If Necessary
							if(_Current_Thread_Count > _Min_Thread_Count && _Idle_Thread_Count == 0) {
								break;
							}
						}
            		}
            		
            		final QueueTask WORKING_ON;
	                synchronized(__QUEUE_LOCK__) {
	                    WORKING_ON = _QUEUE.pop();
	                    _Queue_Length--;
	                }
	                
                	WORKING_ON.run();
            	}
            	
            	boolean Idle = false;
            	boolean Timeout = false;
            	synchronized(__IDLE_LOCK__) {
            		System.out.println("Threads: " + _Current_Thread_Count + " Idle: " + _Idle_Thread_Count);
            		
	        		if(_Current_Thread_Count <= _Max_Thread_Count && (_Current_Thread_Count - _Idle_Thread_Count) > _Min_Thread_Count) {
	        			_Idle_Thread_Count++;
	        			Timeout = true;
	        		} else {
	        			_Idle_Thread_Count++;
	        			Idle = true;
	        		}
            	}
            	
				if(Timeout) {
					System.out.println(Thread.currentThread().getName() + " Timeout State");
					final TimeOutTimer TIMEOUT = new TimeOutTimer(_Idle_Time);
					
					while(true) {
						if(TIMEOUT.hasTimedOut()) {
							System.out.println(Thread.currentThread().getName() + " Timed Out");
							Shutdown = true;
							break;
						}

						if(!_QUEUE.isEmpty()) {
							if((_Queue_Length / _Current_Thread_Count) > _Threshold) {
								break;
							}
						}
						
						if(_ShutDown) {break;}

						ThreadUtil.sleep(50);
					}
				}

				if(Idle) {
					System.out.println(Thread.currentThread().getName() + " Idle State");
					while(true) {
						if(!_QUEUE.isEmpty()) {
							if((_Queue_Length / _Current_Thread_Count) > _Threshold) {
								break;
							}
						}
						
						if(_ShutDown) {break;}
						
						ThreadUtil.sleep(50);						
					}
				}
				
				if(/*!Shutdown &&*/ (Timeout || Idle)) {
					synchronized(__IDLE_LOCK__) {
						_Idle_Thread_Count--;
					}
				}
            }
            
			synchronized(__IDLE_LOCK__) {
				_Current_Thread_Count--;
			}
        }
    }
}
    
/*
					synchronized(__IDLE_LOCK__) {
						if((_Queue_Length / _Current_Thread) > _Threshold) {
							if(_Current_Thread < _Max_Thread && _Idle_Count == 0) {//Not Enough Threads So Create Some
								final int L = _Queue_Length;

								System.out.println("Len: " + L);
								System.out.println("Task/Thread: " + (L / _Current_Thread));

								startThread();
							}
						} else if(_Current_Thread > _Min_Thread) {//Too Many Thread Kill Some
							if((_Queue_Length / _Current_Thread) < _Threshold) {
								System.out.println(Thread.currentThread().getName() + " Kill Thread");
								break;
							}
						}
					}
					
					System.out.println(Thread.currentThread().getName() + " Timeout State");
					final TimeOutTimer TIMEOUT = new TimeOutTimer(_Idle_Time);
					while(_QUEUE.isEmpty() || (_Queue_Length / _Current_Thread) < _Threshold) {
						if(TIMEOUT.hasTimedOut()) {
							System.out.println(Thread.currentThread().getName() + " Timed Out");
							Shutdown = true;
							break;
						}

						if(_ShutDown) {
							break;
						}
						
						ThreadUtil.sleep(50);
					}
					
					System.out.println(Thread.currentThread().getName() + " Idle State");
					while(_QUEUE.isEmpty() || (_Queue_Length / _Current_Thread) < _Threshold) {
						ThreadUtil.sleep(50);
						
						if(_ShutDown) {
							break;
						}
						
					}
					System.out.println(Thread.currentThread().getName() + " Running State");
					
					if(!Shutdown) {
						System.out.println(Thread.currentThread().getName() + " Running State");
					}
 */
    
/*
public class IntSync {
private final Object __LOCK__ = new Object();

private int _Value = 0;

public IntSync(int value) {
	_Value = value;
}

public int increment() {
	synchronized(__LOCK__) {
		return _Value++;
	}
}

public int decrement() {
	synchronized(__LOCK__) {
		return _Value--;
	}
}

public int get() {
	synchronized(__LOCK__) {
		return _Value;
	}
}

public boolean lessthen(int value) {
	synchronized(__LOCK__) {
		return _Value < value;
	}
}

public boolean greaterthen(int value) {
	synchronized(__LOCK__) {
		return _Value < value;
	}
}

public boolean equals(int value) {
	synchronized(__LOCK__) {
		return _Value == value;
	}
}

public boolean lessthenOrEqualToo(int value) {
	synchronized(__LOCK__) {
		return _Value <= value;
	}
}

public boolean greaterthenOrEqualToo(int value) {
	synchronized(__LOCK__) {
		return _Value <= value;
	}
}
}
*/