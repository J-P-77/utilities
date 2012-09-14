package beta.utillib.queue;

/**
 * 
 * @author Justin Palinkas
 */
public interface IQueue {
	/**
	 * Task Has Been Added To Queue
	 * 
	 * @param task
	 */
	public void taskAdded(QueueTask task);

	/**
	 * Task Has Been Removed To Queue
	 * 
	 * @param task
	 */
	public void taskRemoved(QueueTask task);

	public boolean canAcceptTask();

	/**
	 * Started Working On Task
	 * 
	 * @param task
	 */
	public void startTask(QueueTask task);

	/**
	 * Task Has Completed (Canceled or Finished)
	 * 
	 * @param task
	 */
	public void endTask(QueueTask task);

	public void started();

	public void shutdowned();
}
