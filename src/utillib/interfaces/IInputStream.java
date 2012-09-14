package utillib.interfaces;

import java.io.IOException;

public interface IInputStream extends IClose {
	public int read() throws IOException;

	public int available() throws IOException;
}
