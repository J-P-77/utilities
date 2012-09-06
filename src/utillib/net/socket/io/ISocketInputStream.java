package utillib.net.socket.io;

import utillib.net.socket.MySocket;

import utillib.io.AInputStream;

import java.io.IOException;

public class ISocketInputStream extends AInputStream {
	private MySocket _Socket = null;

	public ISocketInputStream(MySocket socket) {
		if(socket == null) {
			throw new RuntimeException("Variable[socket] - Is Null");
		}

		_Socket = socket;
	}

	@Override
	public int read() throws IOException {
		final byte[] BYTES = new byte[1];

		if(_Socket.receive(BYTES, 0, 1) == 1) {
			return (int)BYTES[0];
		}

		return -1;
	}

	@Override
	public int read(byte[] buffer, int offset, int length) throws IOException {
		return _Socket.receive(buffer, offset, length);
	}

	@Override
	public boolean isClosed() {
		if(_Socket == null) {
			return true;
		}

		return _Socket.isClosed();
	}

	@Override
	public void close() throws IOException {
		if(_Socket != null) {
			try {
				_Socket.close();
			} catch(Exception e) {}
			_Socket = null;
		}
	}
}