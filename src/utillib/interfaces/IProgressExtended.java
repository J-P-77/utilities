package utillib.interfaces;

/**
 *
 * @author Dalton Dell
 */
public interface IProgressExtended extends IProgress {
/*
	public void decrement();
	public void decrement(int value);
*/
//
    @Deprecated
    public void setVisible(boolean value);
    @Deprecated
    public boolean isVisible();
//
    /**
     * Destory's This Instance Of This Oject
     */
    @Deprecated
    public void destroy();

    /**
     * Tells Operation to Cancel
     */
    public void cancel();
    /**
     * Operation Is Attempting to Cancel Current Progress
     * @return
     */
    public boolean isCanceling();

    /**
     * Flag To Tell The Operation That It Has Canceled Current Progress
     */
    public void canceled();
    /**
     * Operation Has Canceled Current Progress
     * @return
     */
    public boolean isCanceled();
}
