package utillib.io;

import utillib.arrays.buffer.AFixedInputByteBuffer;

import java.io.IOException;

public class FixedByteBufferInputStream extends AInputStream {
	private AFixedInputByteBuffer _Buffer = null;

	public FixedByteBufferInputStream(AFixedInputByteBuffer buffer) {
		if(buffer == null) {
			throw new RuntimeException("Variable[buffer] - Is Null");
		}

		_Buffer = buffer;
	}

	public AFixedInputByteBuffer getBuffer() {
		return _Buffer;
	}

	@Override
	public int read() throws IOException {
		return _Buffer.get();
	}

	@Override
	public boolean isClosed() {
		return _Buffer == null;
	}

	@Override
	public void close() throws IOException {
		_Buffer = null;
	}
}
