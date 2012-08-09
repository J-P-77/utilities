package utillib.io;

import utillib.arrays.buffer.AFixedOutputByteBuffer;

import java.io.IOException;

public class FixedByteBufferOutputStream extends AOutputStream {
	private AFixedOutputByteBuffer _Buffer = null;

	public FixedByteBufferOutputStream(AFixedOutputByteBuffer buffer) throws IOException {
		if(buffer == null) {
			throw new RuntimeException("Variable[buffer] - Is Null");
		}

		_Buffer = buffer;
	}

	public AFixedOutputByteBuffer getBuffer() {
		return _Buffer;
	}

	public void reset() {
		_Buffer.reset();
	}

	@Override
	public void write(int b) throws IOException {
		_Buffer.put((byte)b);
	}

	@Override
	public void flush() throws IOException {
		_Buffer.empty();
	}

//	@Override
	public boolean isClosed() {
		return _Buffer == null;
	}

	public synchronized void close() throws IOException {
		_Buffer.empty();

		_Buffer = null;
	}
}
