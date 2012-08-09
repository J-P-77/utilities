package utillib.interfaces;

import java.io.IOException;

public interface IOutputStream extends IClose {
	public void write(int value) throws IOException;
	
    public void flush() throws IOException;
}
