package utillib.net.socket.io;

import utillib.net.socket.MySocket;

import utillib.io.AOutputStream;

import java.io.IOException;

public class ISocketOutputStream extends AOutputStream {
	private MySocket _Socket = null;

	public ISocketOutputStream(MySocket socket) {
		if(socket == null) {
			throw new RuntimeException("Variable[socket] - Is Null");
		}

		_Socket = socket;
	}

	@Override
	public void write(int b) throws IOException {
		final byte[] BYTES = new byte[1];

		_Socket.send(BYTES, 0, 1);
	}

	@Override
	public void write(byte[] buffer, int offset, int length) throws IOException {
		_Socket.send(buffer, offset, length);
	}

	@Override
	public boolean isClosed() {
		return _Socket == null;
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