package beta.utillib.queue;

/**
 *
 * @author Dalton Dell
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
