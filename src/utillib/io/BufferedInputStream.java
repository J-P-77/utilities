package utillib.io;

import utillib.interfaces.IInputBuffer;

import utillib.arrays.buffer.FixedInputByteBuffer;

import java.io.IOException;
import java.io.InputStream;

public class BufferedInputStream extends AInputStream {
	private FixedInputByteBuffer _Buffer = null;
	private InputStream _IStream = null;

	public BufferedInputStream(InputStream istream, int size) {
		if(istream == null) {
			throw new RuntimeException("Variable[istream] - Is Null");
		}

		_IStream = istream;
		_Buffer = new FixedInputByteBuffer(size, new IInputBuffer() {

			@Override
			public int fill(byte[] buffer, int offset, int length) {
				try {
					return _IStream.read(buffer, offset, length);
				} catch(Exception e) {}

				return -1;
			}
		});
	}

	@Override
	public int read() throws IOException {
		return _Buffer.get();
	}

	@Override
	public boolean isClosed() {
		return _IStream == null;
	}

	@Override
	public synchronized void close() throws IOException {
		if(_IStream != null) {
			_IStream.close();
		}
		_IStream = null;
		_Buffer = null;
	}
}
