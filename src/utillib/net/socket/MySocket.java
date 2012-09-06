package utillib.net.socket;

import utillib.net.factory.SocketFactories;
import utillib.net.interfaces.ISocket;
import utillib.net.interfaces.ISocketFactory;

import java.io.IOException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
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
public class MySocket implements ISocket {
	public static final int _OPTION_KEEPALIVE_ = 3;
	public static final int _OPTION_SOLINGER_ = 4;
	public static final int _OPTION_TCPNODELAY_ = 5;

	private InetAddress _Local_Address;
	private int _Local_Port;

	private InetAddress _Remote_Address;
	private int _Remote_Port;

	private Socket _Socket = null;

	public MySocket() throws IOException {
		_Socket = new Socket();
	}

	public MySocket(int port) throws UnknownHostException, IOException {
		this(InetAddress.getLoopbackAddress(), port, InetAddress.getLocalHost(), 0, SocketFactories.getPlainSocketFactory());
	}

	public MySocket(String address, int port) throws UnknownHostException, IOException {
		this(address, port, null, 0, SocketFactories.getPlainSocketFactory());
	}

	public MySocket(String address, int port, ISocketFactory socketfactory) throws UnknownHostException, IOException {
		this(address, port, null, 0, socketfactory);
	}

	public MySocket(String address, int port, InetAddress localaddress, int localport, ISocketFactory socketfactory) throws UnknownHostException, IOException {
		this(socketfactory.createSocket(address, port, localaddress, localport));
	}

	public MySocket(InetAddress address, int port, InetAddress localaddress, int localport, ISocketFactory socketfactory) throws UnknownHostException, IOException {
		this(socketfactory.createSocket(address, port, localaddress, localport));
	}

	public MySocket(Socket socket) throws IOException {
		if(socket == null) {
			throw new RuntimeException("Variable[socket] - Is Null");
		}

		if(!socket.isBound()) {
			throw new RuntimeException("Variable[socket] - Not Bound");
		}

		if(!socket.isConnected()) {
			throw new RuntimeException("Variable[socket] - Not Connected");
		}

		_Socket = socket;
		_Local_Address = _Socket.getLocalAddress();
		_Local_Port = _Socket.getLocalPort();
		_Remote_Address = ((InetSocketAddress)_Socket.getRemoteSocketAddress()).getAddress();
		_Remote_Port = ((InetSocketAddress)_Socket.getRemoteSocketAddress()).getPort();
	}

	public Socket getSocket() {
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

			_Local_Address = _Socket.getLocalAddress();
			_Local_Port = _Socket.getLocalPort();
		}
	}

	@Override
	public boolean isBound() {
		return _Socket.isBound();
	}

	@Override
	public void connect(String address, int port) throws IOException {
		connect(new InetSocketAddress(address, port));
	}

	@Override
	public void connect(InetAddress address, int port) throws IOException {
		connect(new InetSocketAddress(address, port));
	}

	private void connect(InetSocketAddress address) throws IOException {
		if(!isConnected()) {
			_Socket.connect(address);

			if(_Local_Address == null) {
				_Local_Address = _Socket.getLocalAddress();
				_Local_Port = _Socket.getLocalPort();
			}

			_Remote_Address = ((InetSocketAddress)_Socket.getRemoteSocketAddress()).getAddress();
			_Remote_Port = ((InetSocketAddress)_Socket.getRemoteSocketAddress()).getPort();
		}
	}

	@Override
	public boolean isConnected() {
		return _Socket.isConnected();
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
	public InetAddress getRemoteAddress() {
		return _Remote_Address;
	}

	@Override
	public int getRemotePort() {
		return _Remote_Port;
	}

	public void send(byte[] buffer, int offset, int length) throws IOException {
		_Socket.getOutputStream().write(buffer, offset, length);
	}

	public int receive(byte[] buffer, int offset, int length) throws IOException {
		return _Socket.getInputStream().read(buffer, offset, length);
	}

	public int available() {
		if(_Socket != null) {
			try {
				return _Socket.getInputStream().available();
			} catch(Exception e) {}
		}

		return 0;
	}

	@Override
	public void setOption(int option, Object value) {
		try {
			switch(option) {
				case _OPTION_SOTIMEOUT_:
					_Socket.setSoTimeout((Integer)value);
					break;
				case _OPTION_SENDBUFFERSIZE_:
					_Socket.setSendBufferSize((Integer)value);
					break;

				case _OPTION_RECEIVEBUFFERSIZE_:
					_Socket.setReceiveBufferSize((Integer)value);
					break;

				case _OPTION_KEEPALIVE_:
					_Socket.setKeepAlive((Boolean)value);
					break;

				case _OPTION_SOLINGER_:
					if(((Integer)value) == 0) {
						_Socket.setSoLinger(false, 0);
					} else {
						_Socket.setSoLinger(true, ((Integer)value));
					}
					break;

				case _OPTION_TCPNODELAY_:
					_Socket.setTcpNoDelay((Boolean)value);
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

				case _OPTION_SENDBUFFERSIZE_:
					return _Socket.getSendBufferSize();

				case _OPTION_RECEIVEBUFFERSIZE_:
					return _Socket.getReceiveBufferSize();

				case _OPTION_KEEPALIVE_:
					return _Socket.getKeepAlive();

				case _OPTION_SOLINGER_:
					return _Socket.getSoLinger();

				case _OPTION_TCPNODELAY_:
					return _Socket.getTcpNoDelay();
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
