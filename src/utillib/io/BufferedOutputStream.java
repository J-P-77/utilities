package utillib.io;

import utillib.interfaces.IOutputBuffer;

import utillib.arrays.buffer.FixedOutputByteBuffer;

import java.io.IOException;
import java.io.OutputStream;

public class BufferedOutputStream extends AOutputStream {
	private FixedOutputByteBuffer _Buffer = null;
	private OutputStream _OStream = null;

	public BufferedOutputStream(OutputStream ostream, int size) {
		if(ostream == null) {
			throw new RuntimeException("Variable[ostream] - Is Null");
		}

		_OStream = ostream;
		_Buffer = new FixedOutputByteBuffer(size, new IOutputBuffer() {

			@Override
			public void empty(byte[] buffer, int offset, int length) {
				try {
					_OStream.write(buffer, offset, length);
				} catch(Exception e) {}
			}
		});
	}

	@Override
	public void write(int b) throws IOException {
		_Buffer.put((byte)b);
	}

	@Override
	public boolean isClosed() {
		return _OStream == null;
	}

	@Override
	public synchronized void close() throws IOException {
		if(_OStream != null) {
			_OStream.close();
		}
		_OStream = null;
		_Buffer = null;
	}
}
