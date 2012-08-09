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
     * @return
     */
    public boolean isRunning() {
        if(super.getState() == Thread.State.NEW) {
            return true;
        }

        return super.isAlive();
    }
}

