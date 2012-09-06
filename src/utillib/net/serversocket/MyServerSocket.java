package utillib.net.serversocket;

import utillib.net.NetUtil;

import utillib.net.interfaces.IServerSocket;
import utillib.net.interfaces.ISocket;

import utillib.net.socket.MySocket;

import java.io.IOException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 13, 2011 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class MyServerSocket implements IServerSocket {
	public static final int _OPTION_KEEPALIVE_ = 3;
	public static final int _OPTION_SOLINGER_ = 4;
	public static final int _OPTION_TCPNODELAY_ = 5;

	private InetAddress _Local_Address;
	private int _Local_Port;

	private ServerSocket _Socket = null;

	public MyServerSocket() throws IOException {
		_Socket = new ServerSocket();
	}

	public MyServerSocket(int port) throws UnknownHostException, IOException {
		this(NetUtil.getLoopbackAddress(), port);
	}

	public MyServerSocket(String localaddress, int localport) throws UnknownHostException, IOException {
		this(new ServerSocket(localport, 50, InetAddress.getByName(localaddress)));
	}

	public MyServerSocket(InetAddress localaddress, int localport) throws UnknownHostException, IOException {
		this(new ServerSocket(localport, 50, localaddress));
	}

	public MyServerSocket(ServerSocket socket) throws IOException {
		if(socket == null) {
			throw new RuntimeException("Variable[socket] - Is Null");
		}

		if(!socket.isBound()) {
			throw new RuntimeException("Variable[socket] - Not Bound");
		}

		_Socket = socket;
		_Local_Address = _Socket.getInetAddress();
		_Local_Port = _Socket.getLocalPort();
	}

	public ServerSocket getSocket() {
		return _Socket;
	}

	@Override
	public void bind(String address, int port) throws IOException {
		bind(new InetSocketAddress(address, port));
	}

	@Override
	public void bind(InetAddress address, int port) throws IOException {
		bind(new InetSocketAddress(address, port));
	}

	private void bind(InetSocketAddress address) throws IOException {
		if(!isBound()) {
			_Socket.bind(address);

			_Local_Address = _Socket.getInetAddress();
			_Local_Port = _Socket.getLocalPort();
		}
	}

	@Override
	public boolean isBound() {
		return _Socket.isBound();
	}

	@Override
	public ISocket accept() throws IOException {
		return new MySocket(_Socket.accept());
	}

	@Override
	public InetAddress getLocalAddress() {
		return _Local_Address;
	};

	@Override
	public int getLocalPort() {
		return _Local_Port;
	}

	@Override
	public void setOption(int option, Object value) {
		try {
			switch(option) {
				case _OPTION_SOTIMEOUT_:
					_Socket.setSoTimeout((Integer)value);
					break;

				case _OPTION_RECEIVEBUFFERSIZE_:
					_Socket.setReceiveBufferSize((Integer)value);
					break;
			}
		} catch(Exception e) {}
	}

	@Override
	public Object getOption(int option) {
		try {
			switch(option) {
				case _OPTION_SOTIMEOUT_:
					return _Socket.getSoTimeout();

				case _OPTION_RECEIVEBUFFERSIZE_:
					return _Socket.getReceiveBufferSize();

			}
		} catch(Exception e) {}

		return null;
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
			_Socket.close();
			_Socket = null;
		}
	}
}
