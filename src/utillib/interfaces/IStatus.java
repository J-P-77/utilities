package utillib.interfaces;

public interface IStatus {
	public boolean hasStarted();

	public boolean isRunning();

	public void pause(boolean value);

	public boolean isPaused();

	public boolean isDone();

	public void cancel();

	public boolean isCanceling();

	public boolean isCanceled();
}
