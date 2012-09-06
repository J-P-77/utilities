package utillib.net.socket.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ThreadedCopyStream extends Thread {
	private final CopyStream _COPYSTREAM;

	public ThreadedCopyStream(InputStream istream, OutputStream ostream) {
		_COPYSTREAM = new CopyStream(istream, ostream);
	}

	@Override
	public void run() {
		_COPYSTREAM.run();
	}

	public boolean isClosed() {
		return _COPYSTREAM.isClosed();
	}

	public void close() throws IOException {
		_COPYSTREAM.close();
	}
}
