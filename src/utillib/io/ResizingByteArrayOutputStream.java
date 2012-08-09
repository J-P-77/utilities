package utillib.io;

import utillib.arrays.ResizingByteArray;

import java.io.IOException;

public class ResizingByteArrayOutputStream extends AOutputStream {
	private ResizingByteArray _Buffer;

	public ResizingByteArrayOutputStream(ResizingByteArray buffer) {
		this(buffer, 0);
	}

	public ResizingByteArrayOutputStream(ResizingByteArray buffer, int offset) {
		if(buffer == null) {
			throw new RuntimeException("Variable[buffer] - Is Null");
		}

		if(offset != 0 && buffer.length() != 0) {
			if(offset < 0 || offset >= buffer.length()) {
				throw new RuntimeException("Variable[offset] - Offset Must Be Greater Than Zero And Less Than Buffer Length");
			}
		}

		_Buffer = buffer;
	}

	public ResizingByteArray getResizingByteArray() {
		return _Buffer;
	}

	@Override
	public void write(int value) throws IOException {
		_Buffer.put((byte)value);
	}

	@Override
	public void flush() throws IOException {}

	@Override
	public boolean isClosed() {
		return _Buffer == null;
	}

	@Override
	public void close() throws IOException {
		_Buffer = null;
	}
}
