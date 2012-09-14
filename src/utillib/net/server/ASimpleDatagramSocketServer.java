package utillib.net.server;

import utillib.net.NetUtil;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
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
//XXX: This Class Might Have Some Blocking Problems
public abstract class ASimpleDatagramSocketServer extends ASimpleServer {
	public static final int _OPTION_SENDBUFFERSIZE_ = 3;
	public static final int _OPTION_BROADCAST_ = 4;
	public static final int _OPTION_PACKETSIZE_ = 5;

	private Exception _Last_Exception = null;

	private DatagramSocket _ServerSocket = null;

	//Options
	private boolean _ReuseAddress = false;
	private int _SoTimeout = -1;
	private int _ReceiveBufferSize = -1;

	private int _SendBufferSize = -1;
	private boolean _Broadcast = false;

	private int _Packet_Size = 1024;

	public ASimpleDatagramSocketServer() {
		super("UDP-Server");
	}

	public ASimpleDatagramSocketServer(int port) throws IOException {
		this("UDP-Server", NetUtil.getLoopbackAddress(), port, 0);
	}

	public ASimpleDatagramSocketServer(String address, int port) throws UnknownHostException, IOException {
		this("UDP-Server", InetAddress.getByName(address), port, 0);
	}

	public ASimpleDatagramSocketServer(String address, int port, int maxconnection) throws UnknownHostException, IOException {
		this("UDP-Server", InetAddress.getByName(address), port, maxconnection);
	}

	public ASimpleDatagramSocketServer(InetAddress address, int port) throws IOException {
		this("UDP-Server", address, port, 0);
	}

	public ASimpleDatagramSocketServer(String name, InetAddress address, int port, int maxconnection) throws IOException {
		super(name);

		if(!NetUtil.validPort(port)) {
			throw new RuntimeException("Variable[port] - Invalid Port");
		}

		bind(address, port);
	}

	public ASimpleDatagramSocketServer(String name, DatagramSocket serversocket, int maxconnection) throws IOException {
		super(name);

		setServerSocket(serversocket);
	}

	public void setServerSocket(DatagramSocket serversocket) {
		if(!super.isShutdown()) {
			throw new RuntimeException(getName() + " Is Not Shutdown");
		}

		if(serversocket == null) {
			throw new RuntimeException("Variable[serversocket] - Is Null");
		}

		_ServerSocket = serversocket;
	}

	public DatagramSocket getServerSocket() {
		return _ServerSocket;
	}

	@Override
	public void bind(InetAddress address, int port) throws IOException {
		if(!isClosed()) {
			throw new IOException("Is Not Closed");
		}

		if(isBound()) {
			throw new IOException("Allready Bound");
		}

		_ServerSocket = new DatagramSocket(new InetSocketAddress(address, port));

		_ServerSocket.setReuseAddress(_ReuseAddress);

		if(_SoTimeout != -1) {
			_ServerSocket.setSoTimeout(_SoTimeout);
		}

		if(_ReceiveBufferSize != -1) {
			_ServerSocket.setReceiveBufferSize(_ReceiveBufferSize);
		}

		if(_SendBufferSize != -1) {
			_ServerSocket.setSendBufferSize(_SendBufferSize);
		}

		_ServerSocket.setBroadcast(_Broadcast);
	}

	@Override
	public boolean isBound() {
		if(_ServerSocket != null) {
			return _ServerSocket.isBound();
		}

		return false;
	}

	@Override
	public InetAddress getLocalAddress() {
		if(_ServerSocket != null) {
			return _ServerSocket.getLocalAddress();
		}

		return null;
	}

	@Override
	public int getLocalPort() {
		if(_ServerSocket != null) {
			return _ServerSocket.getLocalPort();
		}

		return 0;
	}

//	_ServerSocket.setReuseAddress(on);
//	_ServerSocket.setSoTimeout(timeout);
//	_ServerSocket.setReceiveBufferSize(size);

//	_ServerSocket.setSendBufferSize(size);
//	_ServerSocket.setBroadcast(on);	
	public void setOption(int option, Object value) {
//		if(isClosed()) {
//			throw new RuntimeException(getName() + " Server Is Not Open Call open(InetAddress, int) First");
//		}

		try {
			switch(option) {
				case _OPTION_REUSEADDRESS_:
					if(_ServerSocket != null) {
						_ServerSocket.setBroadcast((boolean)(Boolean)value);
					}
					_ReuseAddress = (boolean)(Boolean)value;
					break;

				case _OPTION_SOTIMEOUT_:
					if(_ServerSocket != null) {
						_ServerSocket.setSoTimeout((int)(Integer)value);
					}
					_SoTimeout = (int)(Integer)value;
					break;

				case _OPTION_RECEIVEBUFFERSIZE_:
					if(_ServerSocket != null) {
						_ServerSocket.setReceiveBufferSize((int)(Integer)value);
					}
					_ReceiveBufferSize = (int)(Integer)value;
					break;

				case _OPTION_SENDBUFFERSIZE_:
					if(_ServerSocket != null) {
						_ServerSocket.setSendBufferSize((int)(Integer)value);
					}
					_SendBufferSize = (int)(Integer)value;
					break;

				case _OPTION_BROADCAST_:
					if(_ServerSocket != null) {
						_ServerSocket.setBroadcast((boolean)value);
					}
					_Broadcast = (boolean)value;
					break;

				case _OPTION_PACKETSIZE_:
					_Packet_Size = (int)(Integer)value;
					break;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Object getOption(int option) {
		if(isClosed()) {
			throw new RuntimeException(getName() + " Server Is Not Open Call open(InetAddress, int) First");
		}

		try {
			switch(option) {
				case _OPTION_REUSEADDRESS_:
//					return _ServerSocket.getReuseAddress();
					return _ReuseAddress;

				case _OPTION_SOTIMEOUT_:
//					return _ServerSocket.getSoTimeout();
					return _SoTimeout;

				case _OPTION_RECEIVEBUFFERSIZE_:
//					return _ServerSocket.getReceiveBufferSize();
					return _ReceiveBufferSize;

				case _OPTION_SENDBUFFERSIZE_:
//					return _ServerSocket.getSendBufferSize();
					return _SendBufferSize;

				case _OPTION_BROADCAST_:
//					return _ServerSocket.getBroadcast();
					return _Broadcast;

				case _OPTION_PACKETSIZE_:
					return _Packet_Size;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public DatagramPacket accept() throws IOException, SocketTimeoutException {
		final DatagramPacket PACKET = new DatagramPacket(new byte[_Packet_Size], _Packet_Size);

		_ServerSocket.receive(PACKET);

		return PACKET;
	}

	public void run() {
		if(!isBound()) {
			throw new RuntimeException("Variable[] - Is Not Bound");
		}

		_State = State.STARTED;

		_LOG_.printInformation("Started: " + Thread.currentThread().getName());
		_LOG_.printInformation("Listening On: " + NetUtil.toString(getLocalAddress(), getLocalPort()));

		while(_State != State.STOP/* || !isClosed() */) {
			try {
				final DatagramPacket CLIENT = accept();

				final String REMOTE_ADDRESS_PORT = NetUtil.toString(CLIENT.getAddress(), CLIENT.getPort());
				//Connection
				_LOG_.printInformation("Incoming From: " + REMOTE_ADDRESS_PORT);

				if(acceptPacket(CLIENT)) {
					_LOG_.printInformation("Accepted From: " + REMOTE_ADDRESS_PORT);

					handlePacket(CLIENT);
				} else {
					_LOG_.printInformation("Rejected From: " + REMOTE_ADDRESS_PORT);
				}
			} catch(SocketTimeoutException e) {

			} catch(Exception e) {
				if(_State != State.STOP) {
					_LOG_.printError(e);

					_Last_Exception = e;
					_State = State.STOP;
				}
			}
		}

		_LOG_.printInformation("Stopped: " + Thread.currentThread().getName());
		_State = State.STOPPED;
	}

	@Override
	public Exception getLastException() {
		return _Last_Exception;
	}

	@Override
	public boolean isClosed() {
		return _ServerSocket == null;
	}

	@Override
	public void close() {
		if(_ServerSocket != null/* && isRunning() */) {
			try {
				_ServerSocket.close();
			} catch(Exception e) {}
			_ServerSocket = null;

//			_LOG_.printInformation("Closed On " + NetUtil.toStringAddressNPort(getLocalAddress(), getLocalPort()));
		}
	}

	@Override
	protected void finalize() throws Throwable {
		close();

		super.finalize();
	}

	public abstract boolean acceptPacket(DatagramPacket packet);

	public abstract void handlePacket(DatagramPacket packet);
}
