package utillib.interfaces;

import java.io.IOException;

public interface IClose {
	public void close() throws IOException;

	public boolean isClosed();
}
