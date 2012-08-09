package utillib.io;

import utillib.arrays.ArraysUtil;

import java.io.IOException;

public class ByteArrayOutputStream extends AOutputStream {
	private final int _LENGTH;

	private byte[] _Buffer;

	private int _Pos = 0;

	public ByteArrayOutputStream(byte[] buffer) {
		this(buffer, 0, buffer.length);
	}

	public ByteArrayOutputStream(byte[] buffer, int offset, int length) {
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

	public void reset() {
		_Pos = 0;
	}

	public void reset(int value) {
		if(value >= 0 && value < _LENGTH) {
			_Pos = value;
		}
	}

	@Override
	public void write(int value) throws IOException {
		if(_Pos < _LENGTH) {
			_Buffer[_Pos++] = (byte)value;
		} else {
			throw new IOException();
		}
	}

	@Override
	public void write(byte[] buffer, int offset, int length) throws IOException {
		ArraysUtil.checkBufferBounds(buffer.length, offset, length);

		for(int X = 0; X < length && _Pos < _LENGTH; X++, _Pos++) {
			buffer[offset + X] = _Buffer[_Pos];
		}
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

	public static void main(String[] args) {
		final byte[] BUFFER = new byte[100];

		ByteArrayOutputStream BOStream = null;
		MyOutputStream OStream = null;
		try {
			BOStream = new ByteArrayOutputStream(BUFFER);
			OStream = new MyOutputStream(BOStream);

			OStream.writeString("Justin Palinkas");
		} catch(Exception e) {}

		ByteArrayInputStream BIStream = null;
		try {
			BIStream = new ByteArrayInputStream(BUFFER, 0, BOStream.getPosition());

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
