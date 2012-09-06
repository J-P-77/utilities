package utillib.net.socket.io;

import java.io.InputStream;
import java.io.OutputStream;

public class CopyStream implements Runnable {
	private InputStream _IStream = null;
	private OutputStream _OStream = null;

	public CopyStream(InputStream istream, OutputStream ostream) {
		if(istream == null) {
			throw new RuntimeException("Variable[istream] - Is Null");
		}

		if(ostream == null) {
			throw new RuntimeException("Variable[ostream] - Is Null");
		}

		_IStream = istream;
		_OStream = ostream;
	}

	@Override
	public void run() {
		try {
			final byte[] BUFFER = new byte[512];

			int Read = 0;
			while((Read = _IStream.read(BUFFER)) > 0) {
				_OStream.write(BUFFER, 0, Read);
			}
		} catch(Exception e) {

		} finally {
			close();
		}
	}

	public boolean isClosed() {
		return (_IStream == null && _OStream == null);
	}

	public void close() {
		if(_IStream != null) {
			try {
				_IStream.close();
			} catch(Exception e) {}
			_IStream = null;
		}

		if(_OStream != null) {
			try {
				_OStream.flush();
				_OStream.close();
			} catch(Exception e) {}
			_OStream = null;
		}
	}
}
