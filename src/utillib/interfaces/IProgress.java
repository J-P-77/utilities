package utillib.interfaces;

/**
 *
 * @author Dalton Dell
 */
public interface IProgress extends IText {

	public void increment();
	public void increment(int value);
/*
	public void decrement();
	public void decrement(int value);
*/
    public void setValue(int value);
	public int getValue();
	
	public void setMaximum(int value);
	public int getMaximum();
		
	public void setMinimum(int value);
	public int getMinimum();
}
