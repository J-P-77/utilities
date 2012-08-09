package utillib.io;

import utillib.arrays.ArraysUtil;

import java.io.IOException;

public class ByteArrayInputStream extends AInputStream {
	private final int _LENGTH;

	private byte[] _Buffer;

	private int _Pos = 0;

	public ByteArrayInputStream(byte[] buffer) {
		this(buffer, 0, buffer.length);
	}

	public ByteArrayInputStream(byte[] buffer, int offset, int length) {
		if(buffer == null) {
			throw new RuntimeException("Variable[buffer] - Is Null");
		}

		ArraysUtil.checkBufferBounds(buffer.length, offset, length);

		_Buffer = buffer;
		_LENGTH = length;
		_Pos = offset;
	}

	public int length() {
		return _LENGTH;
	}

	public int getPosition() {
		return _Pos;
	}

	@Override
	public int read() {
		if(_Pos < _LENGTH) {
			return _Buffer[_Pos++];
		}

		return -1;
	}

	@Override
	public int read(byte[] buffer, int offset, int length) {
		ArraysUtil.checkBufferBounds(buffer.length, offset, length);

		int X = 0;
		while(X < length && _Pos < _LENGTH) {
			buffer[offset + X++] = _Buffer[_Pos++];
		}

		return X;
	}

	@Override
	public int available() throws IOException {
		return _LENGTH - _Pos;
	}

	@Override
	public boolean isClosed() {
		return _Buffer == null;
	}

	@Override
	public void close() throws IOException {
		_Buffer = null;
	}

	@Override
	public boolean markSupported() {
		return true;
	}

	@Override
	public synchronized void mark(int readlimit) {
		reset(readlimit);
	}

	public void reset() {
		_Pos = 0;
	}

	public void reset(int value) {
		if(value >= 0 && value < _LENGTH) {
			_Pos = value;
		} else {
			throw new RuntimeException("Variable[value] - Invalid Index");
		}
	}

	public static void main(String[] args) {
		ByteArrayInputStream BIStream = null;
		try {
			BIStream = new ByteArrayInputStream("Justin Palinkas".getBytes());

			System.out.println(BIStream.available());
			int Read = -1;
			while((Read = BIStream.read()) != -1) {
				System.out.print((char)Read);
			}
			System.out.println();
			System.out.println(BIStream.available());
		} catch(Exception e) {}
	}
}
