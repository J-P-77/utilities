/*
The MIT License (MIT)

Copyright (c) 2014 Justin Palinkas

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package utillib.utilities;

public class ThreadExtended extends Thread {
//	private boolean _HasStarted = false;

	public ThreadExtended() {
		super();
	}

	public ThreadExtended(Runnable target) {
		super(target);
	}

	public ThreadExtended(ThreadGroup group, Runnable target) {
		super(group, target);
	}

	public ThreadExtended(String name) {
		super(name);
	}

	public ThreadExtended(ThreadGroup group, String name) {
		super(group, name);
	}

	public ThreadExtended(Runnable target, String name) {
		super(target, name);
	}

	public ThreadExtended(ThreadGroup group, Runnable target, String name) {
		super(group, target, name);
	}

	public ThreadExtended(ThreadGroup group, Runnable target, String name, long stackSize) {
		super(group, target, name, stackSize);
	}

//	public synchronized void blockingStart() {
//		super.start();
//		
//		while(!hasStarted()) {
//			ThreadUtil.sleep(10);
//		}
//	}

//    @Override
//    public void run() {
//		_HasStarted = true;
//
//		super.run();
//    }
//    
//    public boolean hasStarted() {
//    	return _HasStarted;
//    }

	/**
	 * Does Not Care If Thread Was Started
	 * 
	 * @return
	 */
	public boolean isRunning() {
		if(super.getState() == Thread.State.NEW) {
			return true;
		}

		return super.isAlive();
	}
}
